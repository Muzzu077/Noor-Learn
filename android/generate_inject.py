import os
import csv

def escape_kotlin_string(s):
    if not s:
        return ""
    # Remove leading/trailing quotes and spaces
    s = s.strip().strip('"')
    # Replace newlines with \n
    s = s.replace("\n", " ")
    # Escape quotes
    s = s.replace('"', '\\"')
    return s

csv_file = os.path.join(open('dataset_path.txt').read(), 'islamic_dua_dataset_final.csv')
reader = csv.reader(open(csv_file, 'r', encoding='utf-8', errors='ignore'))
header = next(reader)

# Map columns
col_title = header.index("title") if "title" in header else 2
col_arabic = header.index("arabic_text") if "arabic_text" in header else 3
col_translit = header.index("transliteration") if "transliteration" in header else 4
col_meaning = header.index("english_meaning") if "english_meaning" in header else 5
col_ref = header.index("reference") if "reference" in header else 8

items = []
for row in reader:
    if len(row) < 6:
        continue
    title = escape_kotlin_string(row[col_title])
    arabic = escape_kotlin_string(row[col_arabic])
    translit = escape_kotlin_string(row[col_translit])
    meaning = escape_kotlin_string(row[col_meaning])
    ref = escape_kotlin_string(row[col_ref])
    
    # Generate Kotlin DuaItem line
    items.append('    DuaItem("%s", "%s", "%s", "%s", "%s")' % (title, arabic, translit, meaning, ref))

kotlin_code = "    val KAGGLE_DUAS = listOf(\n" + ",\n".join(items) + "\n    )"

# Write injection code
with open('extracted_duas.txt', 'w', encoding='utf-8') as f:
    f.write(kotlin_code)

print("Kotlin code file generated successfully at extracted_duas.txt")
print("Total Duas parsed:", len(items))
