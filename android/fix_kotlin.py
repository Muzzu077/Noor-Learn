import re

with open("app/src/main/kotlin/com/noorlearn/ui/screens/ToolsScreen.kt", "r", encoding="utf-8") as f:
    text = f.read()

# I know exactly what broke:
# DuaItem("...", "
# 
# Arabic...
# We need to replace "\n\n with "\\n\\n

# A simple regex to find `", "\n\n` and replace with `", "\\n\\n`
text = re.sub(r'\", \"\n\n', r'", "\\n\\n', text)

with open("app/src/main/kotlin/com/noorlearn/ui/screens/ToolsScreen.kt", "w", encoding="utf-8") as f:
    f.write(text)

print("Fixed ToolsScreen.kt literal newlines!")
