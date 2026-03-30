import kagglehub
import os
import pandas as pd
import math

try:
    path = kagglehub.dataset_download("ahsanneural/islamic-dua-and-adhkar-72-verified-duas")
    print("Downloaded to:", path)
    for f in os.listdir(path):
        if f.endswith('.csv'):
            df = pd.read_csv(os.path.join(path, f))
            # Some CSVs have trailing/leading spaces in column names
            df.columns = df.columns.str.strip()
            
            with open("duas_kotlin.txt", "w", encoding="utf-8") as out:
                out.write("val KAGGLE_DUAS = listOf(\n")
                for _, row in df.iterrows():
                    # Helper to cleanly get string and escape
                    def g(col):
                        if col in row and not pd.isna(row[col]):
                            return str(row[col]).replace('"', '\\"').replace('\n', ' ').replace('\r', '')
                        return ""
                    
                    # Columns in the 72-verified-duas dataset typically are:
                    # 'id', 'Dua Title', 'Arabic', 'Transliteration', 'Translation', 'Reference'
                    # Let's dynamically check based on what exists
                    title = g('Dua Title') or g('Title') or g('Arabic Name') or g('title') or "Dua"
                    arabic = g('Arabic') or g('Arabic text') or g('arabic')
                    transliteration = g('Transliteration') or g('Transliteration / Pronunciation') or g('transliteration')
                    translation = g('Translation') or g('English Translation') or g('translation')
                    reference = g('Reference') or g('reference')
                    
                    out.write(f'    Dua("{title}", "{arabic}\\n\\n{transliteration}", "{translation}", "{reference}"),\n')
                out.write(")\n")
            print("Successfully exported Kotlin array to duas_kotlin.txt")
except Exception as e:
    print("Error:", e)
