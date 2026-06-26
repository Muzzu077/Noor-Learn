package com.noorlearn.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.noorlearn.domain.model.Hadith
import com.noorlearn.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HadithHubScreen(
    navController: NavController,
    viewModel: HadithHubViewModel = hiltViewModel()
) {
    val hadiths by viewModel.hadiths.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    val explainHadithId by viewModel.explainHadithId.collectAsState()
    val explanation by viewModel.explanation.collectAsState()
    val isExplaining by viewModel.isExplaining.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Hadith Collection", fontWeight = FontWeight.Bold, color = PrimaryGreen)
                },
                navigationIcon = {
                    IconButton(onClick = { 
                        if (!navController.popBackStack()) {
                            navController.navigate("dashboard") {
                                popUpTo("dashboard") { inclusive = true }
                            }
                        }
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = PrimaryGreen)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White, titleContentColor = PrimaryGreen)
            )
        },
        containerColor = Color.White
    ) { padding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PrimaryGreen)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White).gridBackground()
                    .padding(padding)
            ) {
                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = viewModel::onSearchQueryChanged,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    placeholder = { Text("Search Hadiths...", color = GrayText) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = PrimaryGreen) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.onSearchQueryChanged("") }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear", tint = GrayText)
                            }
                        }
                    },
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = CardWhite,
                        unfocusedContainerColor = CardWhite,
                        focusedBorderColor = PrimaryGreen,
                        unfocusedBorderColor = DividerLight
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
                )

                if (hadiths.isEmpty() && searchQuery.isNotEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No hadiths found for '$searchQuery'", color = GrayText)
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(hadiths, key = { it.id }) { hadith ->
                            HadithCard(
                                hadith = hadith,
                                onExplainClick = { viewModel.explainHadith(hadith) },
                                isExplainLoading = isExplaining && explainHadithId == hadith.id,
                                explanationText = if (explainHadithId == hadith.id) explanation else null,
                                onDismissExplanation = { viewModel.dismissExplanation() }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HadithCard(
    hadith: Hadith,
    onExplainClick: () -> Unit = {},
    isExplainLoading: Boolean = false,
    explanationText: String? = null,
    onDismissExplanation: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = LightGreen
                ) {
                    Text(
                        text = hadith.source,
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        color = PrimaryGreen,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (hadith.grade.contains("Sahih", ignoreCase = true)) SuccessGreen.copy(alpha = 0.1f) else OrangeLight
                ) {
                    Text(
                        text = hadith.grade,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = if (hadith.grade.contains("Sahih", ignoreCase = true)) SuccessGreen else OrangeAccent,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = hadith.arabicText,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontFamily = ArabicFontFamily,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 36.sp
                ),
                color = PrimaryGreen,
                textAlign = TextAlign.Right,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = DividerLight)
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = hadith.translationEn,
                style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 26.sp),
                color = DarkText
            )

            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = hadith.narratorChain,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
                    color = GrayText
                )
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = OrangeLight
                ) {
                    Text(
                        text = hadith.topic,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = OrangeAccent,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }

            // AI Explanation Section
            if (explanationText == null && !isExplainLoading) {
                Spacer(modifier = Modifier.height(16.dp))
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { onExplainClick() },
                    shape = RoundedCornerShape(20.dp),
                    color = OrangeLight
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.AutoAwesome,
                            contentDescription = null,
                            tint = OrangeAccent,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            "Explain This",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = OrangeAccent
                        )
                    }
                }
            }

            if (isExplainLoading) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp,
                        color = PrimaryGreen
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Thinking...", style = MaterialTheme.typography.bodySmall, color = GrayText)
                }
            }

            if (explanationText != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = LightGreenSoft
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Filled.AutoAwesome,
                                    contentDescription = null,
                                    tint = PrimaryGreen,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    "AI Explanation",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = PrimaryGreen
                                )
                            }
                            IconButton(
                                onClick = onDismissExplanation,
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    Icons.Filled.Close,
                                    contentDescription = "Close",
                                    tint = GrayText,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = explanationText,
                            style = MaterialTheme.typography.bodySmall.copy(lineHeight = 20.sp),
                            color = PrimaryGreenDark
                        )
                    }
                }
            }
        }
    }
}
