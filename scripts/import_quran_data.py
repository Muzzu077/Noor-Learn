"""
Imports all 114 Surahs and ~6,236 Ayahs into Supabase via Management API.
Uses Quran.com API v4 (free, no auth) for Arabic text and English translation.

Usage: python scripts/import_quran_data.py
"""

import os
import re
import requests
import time
import sys

SUPABASE_PROJECT_REF = "zbtweubvxltwtysxfzua"
SUPABASE_ACCESS_TOKEN = os.environ.get("SUPABASE_ACCESS_TOKEN", "")
if not SUPABASE_ACCESS_TOKEN:
    print("ERROR: Set SUPABASE_ACCESS_TOKEN environment variable")
    sys.exit(1)
MGMT_API = f"https://api.supabase.com/v1/projects/{SUPABASE_PROJECT_REF}/database/query"
MGMT_HEADERS = {
    "Authorization": f"Bearer {SUPABASE_ACCESS_TOKEN}",
    "Content-Type": "application/json",
}

QURAN_API = "https://api.quran.com/api/v4"

session = requests.Session()
session.headers.update({"User-Agent": "NoorLearn-DataImport/1.0"})


def run_sql(query: str):
    """Execute SQL via Supabase Management API (bypasses RLS)."""
    resp = requests.post(MGMT_API, headers=MGMT_HEADERS, json={"query": query})
    if resp.status_code not in (200, 201):
        print(f"  SQL ERROR: {resp.status_code} {resp.text[:300].encode('ascii', 'replace').decode()}")
        return None
    return resp.json()


def escape_sql(s: str) -> str:
    """Escape single quotes for SQL."""
    return s.replace("'", "''")


def fetch_and_import_surahs():
    """Fetch surah metadata from Quran.com API and insert into Supabase."""
    print("=== Importing Surahs ===")
    resp = session.get(f"{QURAN_API}/chapters")
    resp.raise_for_status()
    chapters = resp.json()["chapters"]

    # Build batch INSERT
    rev_type_map = {"makkah": "Meccan", "madinah": "Medinan"}
    values = []
    for ch in chapters:
        name = escape_sql(ch["translated_name"]["name"])
        arabic_name = escape_sql(ch["name_arabic"])
        rev_type = rev_type_map.get(ch["revelation_place"], ch["revelation_place"])
        total = ch["verses_count"]
        values.append(
            f"({ch['id']}, '{name}', '{arabic_name}', '{rev_type}', {total}, '{name}')"
        )

    sql = f"""
    INSERT INTO surahs (id, name, arabic_name, revelation_type, total_ayah, meaning)
    VALUES {', '.join(values)}
    ON CONFLICT (id) DO UPDATE SET
        name = EXCLUDED.name,
        arabic_name = EXCLUDED.arabic_name,
        revelation_type = EXCLUDED.revelation_type,
        total_ayah = EXCLUDED.total_ayah,
        meaning = EXCLUDED.meaning;
    """
    result = run_sql(sql)
    if result is not None:
        print(f"  Inserted {len(chapters)} surahs.")
    return chapters


def fetch_and_import_ayahs():
    """Fetch Arabic text + English translation for all 114 surahs."""
    print("\n=== Importing Ayahs ===")

    global_ayah_id = 0

    for surah_id in range(1, 115):
        print(f"  Surah {surah_id}/114 ... ", end="", flush=True)

        # Fetch Arabic (Uthmani script)
        arabic_resp = session.get(
            f"{QURAN_API}/quran/verses/uthmani",
            params={"chapter_number": surah_id},
        )
        arabic_resp.raise_for_status()
        arabic_verses = arabic_resp.json()["verses"]

        # Fetch English translation (Sahih International = resource 131)
        eng_resp = session.get(
            f"{QURAN_API}/quran/translations/131",
            params={"chapter_number": surah_id},
        )
        eng_resp.raise_for_status()
        eng_translations = eng_resp.json()["translations"]

        eng_map = {}
        for t in eng_translations:
            eng_map[t["verse_key"]] = t["text"]

        values = []
        for verse in arabic_verses:
            global_ayah_id += 1
            verse_key = verse["verse_key"]
            ayah_number = int(verse_key.split(":")[1])

            translation = eng_map.get(verse_key, "")
            translation = translation.replace("<sup", " <sup")
            translation = re.sub(r"<[^>]+>", "", translation).strip()

            arabic = escape_sql(verse["text_uthmani"])
            trans_en = escape_sql(translation)
            audio = f"https://cdn.islamic.network/quran/audio/128/ar.alafasy/{global_ayah_id}.mp3"

            values.append(
                f"({global_ayah_id}, {surah_id}, {ayah_number}, '{arabic}', '{trans_en}', '', '', '{audio}')"
            )

        sql = f"""
        INSERT INTO ayahs (id, surah_id, ayah_number, arabic_text, translation_en, translation_ur, tafsir_short, audio_url)
        VALUES {', '.join(values)}
        ON CONFLICT (id) DO UPDATE SET
            surah_id = EXCLUDED.surah_id,
            ayah_number = EXCLUDED.ayah_number,
            arabic_text = EXCLUDED.arabic_text,
            translation_en = EXCLUDED.translation_en,
            audio_url = EXCLUDED.audio_url;
        """
        result = run_sql(sql)
        if result is not None:
            print(f"{len(values)} ayahs")
        else:
            print("FAILED")

        # Be polite to the Quran.com API
        time.sleep(0.3)

    print(f"\n  Total ayahs imported: {global_ayah_id}")


def add_rls_select_policies():
    """Add RLS policies so the anon role can SELECT (needed for the Android app)."""
    print("\n=== Adding RLS SELECT policies ===")
    for table in ("surahs", "ayahs"):
        sql = f"""
        DO $$ BEGIN
            IF NOT EXISTS (
                SELECT 1 FROM pg_policies WHERE tablename = '{table}' AND policyname = 'anon_select_{table}'
            ) THEN
                CREATE POLICY anon_select_{table} ON {table} FOR SELECT TO anon USING (true);
            END IF;
        END $$;
        """
        run_sql(sql)
        print(f"  {table}: SELECT policy ensured.")


if __name__ == "__main__":
    fetch_and_import_surahs()
    fetch_and_import_ayahs()
    add_rls_select_policies()
    print("\nDone! All Quran data imported into Supabase.")
