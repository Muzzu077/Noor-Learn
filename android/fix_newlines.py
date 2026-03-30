with open('app/src/main/kotlin/com/noorlearn/ui/screens/ToolsScreen.kt', 'r', encoding='utf-8') as f:
    text = f.read()

# Fix the literal newlines introduced by re.sub
text = text.replace(', "\n\n', ', "\\n\\n')
text = text.replace('\n\nAlhamdu', '\\n\\nAlhamdu')
text = text.replace('\n\nAsbahna', '\\n\\nAsbahna')
text = text.replace('DuaItem("Waking Up", "\n', 'DuaItem("Waking Up", "\\n')

# Actually, the simplest way to fix it is to just re-run inject_kaggle.py 
# but pass `kaggle_content.replace("\\", "\\\\")` so `re.sub` doesn't evaluate the backslashes!
