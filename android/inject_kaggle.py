import re

with open("duas_kotlin.txt", "r", encoding="utf-8") as f:
    kaggle_content = f.read()
    
# Convert Dua(...) to DuaItem(...)
kaggle_content = kaggle_content.replace('Dua(', 'DuaItem(')

with open("app/src/main/kotlin/com/noorlearn/ui/screens/ToolsScreen.kt", "r", encoding="utf-8") as f:
    text = f.read()

# 1. Insert data class DuaItem at the top imports
if "data class DuaItem" not in text:
    text = text.replace("package com.noorlearn.ui.screens\n", "package com.noorlearn.ui.screens\n\ndata class DuaItem(val title: String, val arabic: String, val translation: String, val reference: String = \"\")\n")

# 2. Replace val duas = listOf(...) block
pattern = re.compile(r"val duas = listOf\(\s+Triple[\s\S]*?\)[\s]*\)", re.MULTILINE)

# Replace it with the KAGGLE_DUAS block
# Escape backslashes so re.sub doesn't convert \\n into a literal newline
text = pattern.sub(kaggle_content.replace("\\", "\\\\") + "\n    val duas = KAGGLE_DUAS\n", text)

# 3. Replace Triple usages in filteredDuas and LazyColumnScope
text = text.replace("it.first.lowercase()", "it.title.lowercase()")
text = text.replace("it.second.contains", "it.arabic.contains")
text = text.replace("it.third.lowercase()", "it.translation.lowercase()")

# LazyColumnScope signature
text = text.replace("private fun LazyColumnScope(duas: List<Triple<String, String, String>>)", "private fun LazyColumnScope(duas: List<DuaItem>)")

# items(duas.size) extraction
text = text.replace("val (title, arabic, translation) = duas[index]", "val item = duas[index]\n            val title = item.title\n            val arabic = item.arabic\n            val translation = item.translation")

with open("app/src/main/kotlin/com/noorlearn/ui/screens/ToolsScreen.kt", "w", encoding="utf-8") as f:
    f.write(text)

print("Injected Kaggle Duas directly into ToolsScreen.kt!")
