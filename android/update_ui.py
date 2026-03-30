import os

# 1. Read extracted duas
with open('extracted_duas.txt', 'r', encoding='utf-8') as f:
    extracted_duas = f.read()

# 2. Read ToolsScreen.kt
with open('app/src/main/kotlin/com/noorlearn/ui/screens/ToolsScreen.kt', 'r', encoding='utf-8') as f:
    content = f.read()

# 3. Insert Imports
imports_to_add = """import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.ui.text.font.FontStyle
"""
target_import = "import com.noorlearn.ui.theme.*"
# Use a safe split to insert before theme imports
if target_import in content:
    content = content.replace(target_import, imports_to_add + target_import)

# 4. Update DuaItem struct
old_dua_item = 'data class DuaItem(val title: String, val arabic: String, val translation: String, val reference: String = "")'
new_dua_item = """data class DuaItem(
    val title: String,
    val arabic: String,
    val transliteration: String,
    val translation: String,
    val reference: String = ""
)"""
content = content.replace(old_dua_item, new_dua_item)

# 5. Replace KAGGLE_DUAS
start_idx = content.find("val KAGGLE_DUAS = listOf(")
end_idx = content.find("    val duas = KAGGLE_DUAS")

if start_idx != -1 and end_idx != -1:
    # We want to replace everything from "val KAGGLE_DUAS = listOf(" up to of the close parenthesis just BEFORE "val duas"
    # To find the exact close parenthesis position
    sub = content[start_idx:end_idx]
    close_paren_idx = sub.rfind(")")
    if close_paren_idx != -1:
        # Replace the subset
        content = content[:start_idx] + extracted_duas + "\n" + content[start_idx + close_paren_idx + 1:]

# 6. Update LazyColumnScope layout
old_lazy_column = """@Composable
private fun LazyColumnScope(duas: List<DuaItem>) {
    androidx.compose.foundation.lazy.LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(duas.size) { index ->
            val item = duas[index]
            val title = item.title
            val arabic = item.arabic
            val translation = item.translation
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = PrimaryGreen
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = arabic,
                        style = MaterialTheme.typography.titleLarge.copy(lineHeight = 36.sp),
                        color = DarkText,
                        textAlign = TextAlign.Right,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(color = DividerLight)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = translation,
                        style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                        color = GrayText
                    )
                }
            }
        }
    }
}"""

new_lazy_column = """@Composable
private fun LazyColumnScope(duas: List<DuaItem>) {
    androidx.compose.foundation.lazy.LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(duas.size) { index ->
            val item = duas[index]
            var isExpanded by remember { mutableStateOf(false) }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = PrimaryGreen,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = if (isExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                            contentDescription = "Expand",
                            tint = GrayText
                        )
                    }

                    AnimatedVisibility(visible = isExpanded) {
                        Column {
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            // Arabic Text - Large, Right Aligned
                            if (item.arabic.isNotEmpty()) {
                                Text(
                                    text = item.arabic,
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        lineHeight = 42.sp,
                                        fontWeight = FontWeight.Medium
                                    ),
                                    color = DarkText,
                                    textAlign = TextAlign.Right,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                            
                            HorizontalDivider(color = DividerLight.copy(alpha = 0.5f))
                            Spacer(modifier = Modifier.height(12.dp))

                            // Transliteration - Italic
                            if (item.transliteration.isNotEmpty()) {
                                Text(
                                    text = item.transliteration,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontStyle = FontStyle.Italic,
                                        lineHeight = 24.sp
                                    ),
                                    color = DarkText.copy(alpha = 0.8f)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }

                            // English Translation
                            if (item.translation.isNotEmpty()) {
                                Text(
                                    text = item.translation,
                                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                                    color = GrayText
                                )
                            }

                            if (item.reference.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Ref: ${item.reference}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = GrayText.copy(alpha = 0.5f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}"""

if old_lazy_column in content:
    content = content.replace(old_lazy_column, new_lazy_column)
else:
    # Try with different formats or print fail
    print("WARNING: Exact match for LazyColumnScope NOT FOUND inside content! Reverting to manual check.")

# 7. Write back
with open('app/src/main/kotlin/com/noorlearn/ui/screens/ToolsScreen.kt', 'w', encoding='utf-8') as f:
    f.write(content)

print("ToolsScreen.kt modified successfully via script.")
