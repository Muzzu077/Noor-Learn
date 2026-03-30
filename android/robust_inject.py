import os
import pandas as pd
import re
import kagglehub

path = kagglehub.dataset_download("ahsanneural/islamic-dua-and-adhkar-72-verified-duas")
csv_file = [f for f in os.listdir(path) if f.endswith('.csv')][0]
df = pd.read_csv(os.path.join(path, csv_file))
df.columns = df.columns.str.strip()

kotlin_items = []
for _, row in df.iterrows():
    def g(col):
        if col in df.columns and not pd.isna(row[col]):
            return str(row[col]).replace('\\', '\\\\').replace('"', '\\"').replace('\n', ' ').replace('\r', '')
        return ""
    
    title = g('Dua Title') or g('Title') or g('Arabic Name') or g('title') or "Dua"
    arabic = g('Arabic') or g('Arabic text') or g('arabic')
    transliteration = g('Transliteration') or g('Transliteration / Pronunciation') or g('transliteration')
    translation = g('Translation') or g('English Translation') or g('translation')
    reference = g('Reference') or g('reference')
    
    # Use triple-quoted raw strings for peace of mind!
    arabic_text = ""
    if arabic: arabic_text += arabic
    if transliteration: arabic_text += "\\n\\n" + transliteration
    
    kotlin_items.append(f'    DuaItem("{title}", "{arabic_text}", "{translation}", "{reference}")')

new_block = "val KAGGLE_DUAS = listOf(\n" + ",\n".join(kotlin_items) + "\n)\n"

# Now inject it into ToolsScreen.kt
with open("app/src/main/kotlin/com/noorlearn/ui/screens/ToolsScreen.kt", "r", encoding="utf-8") as f:
    text = f.read()

# Remove the broken block if it exists
# It either starts with `val KAGGLE_DUAS = listOf(` and ends at `)` followed by `val duas = KAGGLE_DUAS`
# Or it was something else.
start_idx = text.find("val KAGGLE_DUAS = listOf(")
if start_idx != -1:
    end_idx = text.find("val duas = KAGGLE_DUAS")
    text = text[:start_idx] + new_block + "    val duas = KAGGLE_DUAS" + text[end_idx + len("val duas = KAGGLE_DUAS"):]
else:
    print("Could not find KAGGLE_DUAS block. Did it get overwritten?")

with open("app/src/main/kotlin/com/noorlearn/ui/screens/ToolsScreen.kt", "w", encoding="utf-8") as f:
    f.write(text)

print("Regenerated KAGGLE_DUAS with clean literal string escaping.")
