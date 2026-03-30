import kagglehub
import os
import pandas as pd
import json
import ast

def merge_transliteration():
    # 1. Load current asset
    asset_path = os.path.join('app', 'src', 'main', 'assets', 'quran_data.json')
    if not os.path.exists(asset_path):
        print(f"Error: {asset_path} not found.")
        return
        
    with open(asset_path, 'r', encoding='utf-8') as f:
        quran_data = json.load(f)
        
    ayahs = quran_data['ayahList']  # New key
    
    # 2. Map existing ayahs for easy lookup: (surah_id, ayah_number) -> ayah_object
    ayah_map = {}
    for a in ayahs:
        ayah_map[(a['surah_id'], a['ayah_number'])] = a
        
    # 3. Download Yasir dataset
    path = kagglehub.dataset_download("yasirabdaali/the-holy-quran-dataset")
    print(f"Kaggle path: {path}")
    
    # 4. Find all CSV files in Yasir dataset
    csv_files = []
    for root, dirs, files in os.walk(path):
        for file in files:
            if file.endswith('.csv'):
                full_path = os.path.join(root, file)
                csv_files.append(full_path)
                
    # 5. Extract transliteration from Yasir dataset
    updated_count = 0
    for csv_file in csv_files:
        try:
            fname = os.path.basename(csv_file)
            name_part = fname.split('.')[0]
            if not name_part.isdigit():
                continue
                
            surah_id = int(name_part)
            
            try:
                df = pd.read_csv(csv_file)
            except:
                continue
                
            if 'verses' not in df.columns:
                continue
                
            for _, row in df.iterrows():
                verses_str = row['verses']
                try:
                    try:
                        verses_list = json.loads(verses_str.replace("'", '"'))
                    except:
                        verses_list = ast.literal_eval(verses_str)
                        
                    if isinstance(verses_list, dict):
                        verses_list = [verses_list]
                        
                    for i, v in enumerate(verses_list, start=1):
                        ayah_num = i
                        transliteration = v.get('transliteration', '')
                        
                        if (surah_id, ayah_num) in ayah_map:
                            ayah_map[(surah_id, ayah_num)]['transliteration'] = transliteration
                            updated_count += 1
                                
                except Exception as e:
                    continue
                        
        except Exception as e:
            print(f"Error processing {csv_file}: {e}")

    # 6. Save updated asset
    with open(asset_path, 'w', encoding='utf-8') as f:
        json.dump(quran_data, f, ensure_ascii=False)
        
    print(f"Successfully merged transliteration for {updated_count} ayahs.")

if __name__ == "__main__":
    merge_transliteration()
