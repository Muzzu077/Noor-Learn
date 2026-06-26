package com.noorlearn.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
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
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.noorlearn.domain.model.Prophet
import com.noorlearn.domain.model.ParaStory
import com.noorlearn.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProphetStoriesScreen(
    navController: NavController,
    viewModel: ProphetStoriesViewModel = hiltViewModel()
) {
    val prophets by viewModel.prophets.collectAsState()
    val isProphetLoading by viewModel.isLoading.collectAsState()
    val explainProphetId by viewModel.explainProphetId.collectAsState()
    val explanation by viewModel.explanation.collectAsState()
    val isExplaining by viewModel.isExplaining.collectAsState()

    val paraStories by viewModel.paraStories.collectAsState()
    val isParaLoading by viewModel.isLoading.collectAsState()

    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Islamic Stories", fontWeight = FontWeight.Bold, color = PrimaryGreen)
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .gridBackground()
                .padding(padding)
        ) {
            // TabRow selector
            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(12.dp)),
                containerColor = CardWhite,
                contentColor = PrimaryGreen,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = PrimaryGreen
                    )
                }
            ) {
                Tab(
                    selected = selectedTabIndex == 0,
                    onClick = { selectedTabIndex = 0 },
                    text = { Text("Prophets", fontWeight = if (selectedTabIndex == 0) FontWeight.Bold else FontWeight.Normal) }
                )
                Tab(
                    selected = selectedTabIndex == 1,
                    onClick = { selectedTabIndex = 1 },
                    text = { Text("Juz / Para", fontWeight = if (selectedTabIndex == 1) FontWeight.Bold else FontWeight.Normal) }
                )
            }

            if (selectedTabIndex == 0) {
                // Prophets Tab
                if (isProphetLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = PrimaryGreen)
                    }
                } else if (prophets.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No stories available yet", color = GrayText)
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(prophets, key = { it.id }) { prophet ->
                            ProphetCard(
                                prophet = prophet,
                                onExplainClick = { viewModel.explainProphetStory(prophet) },
                                isExplainLoading = isExplaining && explainProphetId == prophet.id,
                                explanationText = if (explainProphetId == prophet.id) explanation else null,
                                onDismissExplanation = { viewModel.dismissExplanation() }
                            )
                        }
                    }
                }
            } else {
                // Juz Stories Tab
                if (isParaLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = PrimaryGreen)
                    }
                } else if (paraStories.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No Juz stories available", color = GrayText)
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(paraStories, key = { it.juzNumber }) { story ->
                            ParaStoryCard(story = story)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProphetCard(
    prophet: Prophet,
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
                Text(
                    text = prophet.name,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = DarkText
                )
                Text(
                    text = prophet.arabicName,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontFamily = ArabicFontFamily,
                        fontWeight = FontWeight.Bold
                    ),
                    color = PrimaryGreen
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = LightGreen
            ) {
                Text(
                    text = prophet.period,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
                    color = PrimaryGreen,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = DividerLight)
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = prophet.summary,
                style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 26.sp),
                color = GrayText
            )

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

@Composable
fun ParaStoryCard(story: ParaStory) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
        colors = CardDefaults.cardColors(containerColor = Color.White)
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
                    color = PrimaryGreen
                ) {
                    Text(
                        text = "Juz ${story.juzNumber}",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
                Text(
                    text = story.arabicTitle,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = ArabicFontFamily,
                        fontWeight = FontWeight.Bold
                    ),
                    color = PrimaryGreen,
                    textAlign = TextAlign.Right
                )
            }

            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = story.title,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = DarkText
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = story.story,
                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 24.sp),
                color = DarkText
            )

            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "Key Themes",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = PrimaryGreen
            )
            Spacer(modifier = Modifier.height(6.dp))

            // Badges row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                story.themes.forEach { theme ->
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = LightGreen
                    ) {
                        Text(
                            text = theme,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = PrimaryGreen,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "Lessons & Takeaways",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = OrangeAccent
            )
            Spacer(modifier = Modifier.height(6.dp))

            story.lessons.forEach { lesson ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "• ",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = OrangeAccent
                    )
                    Text(
                        text = lesson,
                        style = MaterialTheme.typography.bodyMedium,
                        color = DarkText
                    )
                }
            }
        }
    }
}
