package com.noorlearn.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.noorlearn.domain.model.VocabularyWord
import com.noorlearn.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VocabularyBuilderScreen(
    navController: NavController,
    viewModel: VocabularyBuilderViewModel = hiltViewModel()
) {
    val words by viewModel.words.collectAsState()
    var currentIndex by remember { mutableIntStateOf(0) }
    var isFlipped by remember { mutableStateOf(false) }

    val activeWord = words.getOrNull(currentIndex)

    // Reset flip status when word changes
    LaunchedEffect(currentIndex) {
        isFlipped = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Quranic Vocabulary", fontWeight = FontWeight.Bold, color = PrimaryGreen)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = PrimaryGreen)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White, titleContentColor = PrimaryGreen)
            )
        },
        containerColor = Color.White
    ) { padding ->
        if (words.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PrimaryGreen)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White).gridBackground()
                    .padding(padding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Progress indicator
                val masteredCount = words.count { it.isMastered }
                val progress = masteredCount.toFloat() / words.size

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "Overall Mastery",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = PrimaryGreen
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            LinearProgressIndicator(
                                progress = { progress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = SuccessGreen,
                                trackColor = BeigeBackground
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            "$masteredCount / ${words.size}",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = PrimaryGreen
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(0.5f))

                // Interactive Flipping Card
                activeWord?.let { word ->
                    val rotation by animateFloatAsState(
                        targetValue = if (isFlipped) 180f else 0f,
                        animationSpec = tween(durationMillis = 400),
                        label = "cardFlip"
                    )

                    Card(
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .height(320.dp)
                            .graphicsLayer {
                                rotationY = rotation
                                cameraDistance = 12f * density
                            }
                            .clickable { isFlipped = !isFlipped },
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = CardWhite),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        if (rotation <= 90f) {
                            // Card Front
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = LightGreen
                                ) {
                                    Text(
                                        text = "${word.occurrences} Occurrences",
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = PrimaryGreen
                                    )
                                }

                                Spacer(modifier = Modifier.height(32.dp))

                                Text(
                                    text = word.arabic,
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        fontFamily = ArabicFontFamily,
                                        fontSize = 54.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = PrimaryGreen,
                                    textAlign = TextAlign.Center
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = "Tap to reveal meaning",
                                    style = MaterialTheme.typography.labelMedium.copy(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic),
                                    color = GrayText
                                )
                            }
                        } else {
                            // Card Back (Rotated 180 deg to prevent mirroring)
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .graphicsLayer { rotationY = 180f }
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = word.transliteration,
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                    color = OrangeAccent
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = word.english,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DarkText,
                                    textAlign = TextAlign.Center
                                )

                                Spacer(modifier = Modifier.height(16.dp))
                                HorizontalDivider(color = DividerLight)
                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = "Example:",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = GrayText,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = word.exampleArabic,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontFamily = ArabicFontFamily,
                                        fontSize = 18.sp
                                    ),
                                    color = PrimaryGreen,
                                    textAlign = TextAlign.Right,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = word.exampleTranslation,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = DarkText,
                                    textAlign = TextAlign.Left,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(0.5f))

                // Action buttons row
                activeWord?.let { word ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Mastery toggle button
                        Button(
                            onClick = { viewModel.toggleMastery(word.id) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (word.isMastered) SuccessGreen else PrimaryGreen
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                if (word.isMastered) Icons.Default.Star else Icons.Default.StarOutline,
                                contentDescription = null,
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(if (word.isMastered) "Mastered!" else "Mark Mastered", color = Color.White)
                        }

                        // Next word button
                        OutlinedButton(
                            onClick = {
                                currentIndex = (currentIndex + 1) % words.size
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = PrimaryGreen)
                        ) {
                            Text("Next Word")
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.AutoMirrored.Filled.NavigateNext, contentDescription = null)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
