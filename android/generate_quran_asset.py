import kagglehub
import os
import pandas as pd
import json

path2 = kagglehub.dataset_download("imrankhan197/the-quran-dataset")
imran_file = ""
for r, d, f in os.walk(path2):
    if len(f) > 0 and f[0].endswith(".csv"):
        imran_file = os.path.join(r, f[0])
        break

df = pd.read_csv(imran_file)

surah_map = {}
ayahs = []

ayah_id_counter = 1

for idx, row in df.iterrows():
    surah_id = int(row['surah_no'])
    
    if surah_id not in surah_map:
        surah_map[surah_id] = {
            "id": surah_id,
            "name_english_translation": str(row.get('surah_name_en', '')),
            "name_arabic": str(row.get('surah_name_ar', '')),
            "name_roman": str(row.get('surah_name_roman', '')),
            "revelation_type": str(row.get('place_of_revelation', '')),
            "number_of_ayahs": int(row.get('total_ayah_surah', 0)),
            "meaning": str(row.get('surah_name_en', ''))
        }
        
    ayahs.append({
        "id": ayah_id_counter,
        "surah_id": surah_id,
        "ayah_number": int(row['ayah_no_surah']),
        "text_arabic": str(row.get('ayah_ar', '')),
        "text_translation": str(row.get('ayah_en', '')),
        "audio_url": ""
    })
    ayah_id_counter += 1

output = {
    "surahList": list(surah_map.values()),
    "ayahList": ayahs
}

assets_dir = os.path.join('app', 'src', 'main', 'assets')
os.makedirs(assets_dir, exist_ok=True)
output_path = os.path.join(assets_dir, 'quran_data.json')

with open(output_path, 'w', encoding='utf-8') as f:
    json.dump(output, f, ensure_ascii=False)

print(f"Successfully generated {output_path} with {len(surah_map)} surahs and {len(ayahs)} ayahs.")
