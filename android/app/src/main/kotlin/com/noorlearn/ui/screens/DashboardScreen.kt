package com.noorlearn.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.noorlearn.domain.model.RecitationLog
import com.noorlearn.ui.theme.*
import java.util.Calendar

// ─── Inspirational quotes pool ─────────────────────────────────
private val DASHBOARD_QUOTES = listOf(
    "Indeed, with hardship will come ease." to "Qur'an 94:5",
    "Allah does not burden a soul beyond that it can bear." to "Qur'an 2:286",
    "And He found you lost and guided you." to "Qur'an 93:7",
    "Verily, in the remembrance of Allah hearts find rest." to "Qur'an 13:28",
    "So remember Me; I will remember you." to "Qur'an 2:152",
    "The best of you are those who learn the Qur'an and teach it." to "Bukhari",
    "Knowledge is a treasure and practice is its key." to "Ali ibn Abi Talib"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavController,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val streakCount by viewModel.streakCount.collectAsState()
    val userName by viewModel.userName.collectAsState()
    val dailyHadith by viewModel.dailyHadith.collectAsState()
    val dailyJourneyDay by viewModel.dailyJourneyDay.collectAsState()
    val recitationLogs by viewModel.recitationLogs.collectAsState()
    val journeyProgress by viewModel.journeyProgressPercent.collectAsState()
    val completedJourneyTasks by viewModel.completedJourneyTasks.collectAsState()
    val lastReadSurahId by viewModel.lastReadSurahId.collectAsState()
    val lastReadSurahName by viewModel.lastReadSurahName.collectAsState()

    val completedCount = completedJourneyTasks.size.coerceAtMost(4)
    val journeyComplete = completedCount >= 4
    val progressLabel = "${(journeyProgress * 100).toInt()}%"

    // Pick a stable quote for the day
    val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
    val quote = DASHBOARD_QUOTES[dayOfYear % DASHBOARD_QUOTES.size]

    // Animated greeting tasks
    val allTasks = listOf("Morning Adhkar", "Surah Reading", "Hadith of the Day", "Reflection Journal")
    val currentTask = if (completedCount < 4) allTasks[completedCount] else "Journey Complete ✓"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .gridLineBackground()
            .verticalScroll(rememberScrollState())
    ) {
        // ─── HEADER ──────────────────────────────────────────────
        DashboardHeader(
            userName = userName,
            streakCount = streakCount,
            onProfileClick = { navController.navigate("profile") }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // ─── QUOTE OF THE DAY ─────────────────────────────────────
        QuoteCard(quote = quote.first, source = quote.second)

        Spacer(modifier = Modifier.height(20.dp))

        // ─── TODAY'S JOURNEY CARD ────────────────────────────────
        TodayJourneyCard(
            currentTask = currentTask,
            completedCount = completedCount,
            journeyProgress = journeyProgress,
            progressLabel = progressLabel,
            journeyComplete = journeyComplete,
            onContinue = { navController.navigate("daily_journey") }
        )

        Spacer(modifier = Modifier.height(20.dp))

        // ─── CONTINUE READING ────────────────────────────────────
        ContinueReadingCard(
            surahId = lastReadSurahId,
            surahName = lastReadSurahName,
            onNavigate = { id, name ->
                navController.navigate("ayah_reader/$id/$name")
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        // ─── PRAYER TIMES ────────────────────────────────────────
        PrayerTimesCard()

        Spacer(modifier = Modifier.height(20.dp))

        // ─── STATS ROW ────────────────────────────────────────────
        StatsRow(
            streakCount = streakCount,
            completedTasks = completedCount,
            logsCount = recitationLogs.size
        )

        Spacer(modifier = Modifier.height(24.dp))

        // ─── SECTION TITLE: EXPLORE ───────────────────────────────
        SectionHeader(title = "Explore", modifier = Modifier.padding(horizontal = 20.dp))
        Spacer(modifier = Modifier.height(12.dp))

        // ─── BENTO GRID ──────────────────────────────────────────
        BentoGrid(navController = navController)

        Spacer(modifier = Modifier.height(24.dp))

        // ─── DAILY HADITH ─────────────────────────────────────────
        SectionHeader(title = "Daily Wisdom", modifier = Modifier.padding(horizontal = 20.dp))
        Spacer(modifier = Modifier.height(12.dp))

        DailyHadithCard(hadith = dailyHadith)

        Spacer(modifier = Modifier.height(20.dp))

        // ─── AI CHATBOT CTA ───────────────────────────────────────
        AiChatCard(onClick = { navController.navigate("chatbot") })

        Spacer(modifier = Modifier.height(24.dp))

        // ─── RECITATION HISTORY ──────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SectionHeader(title = "Recent Activity")
            TextButton(onClick = { navController.navigate("recitation_history") }) {
                Text("View All", color = PrimaryGreen, fontWeight = FontWeight.Bold)
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        if (recitationLogs.isNotEmpty()) {
            recitationLogs.take(3).forEach { log ->
                RecitationLogItem(log = log)
                Spacer(modifier = Modifier.height(8.dp))
            }
        } else {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                border = BorderStroke(1.dp, DividerLight),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(LightGreen, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Mic,
                            contentDescription = null,
                            tint = PrimaryGreen,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "No practice sessions yet",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = DarkText
                        )
                        Text(
                            text = "Your recitation scores will appear here",
                            style = MaterialTheme.typography.bodySmall.copy(color = GrayText)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

// ══════════════════════════════════════════════════════════════════
// DASHBOARD HEADER
// ══════════════════════════════════════════════════════════════════
@Composable
private fun DashboardHeader(
    userName: String,
    streakCount: Int,
    onProfileClick: () -> Unit
) {
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val greeting = when {
        hour < 12 -> "Good Morning"
        hour < 17 -> "Good Afternoon"
        else -> "Good Evening"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = greeting,
                style = MaterialTheme.typography.bodyMedium.copy(color = GrayText)
            )
            Text(
                text = userName.ifBlank { "Learner" },
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Streak badge
            if (streakCount > 0) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = OrangeLight,
                    border = BorderStroke(1.dp, OrangeAccent.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("🔥", fontSize = 14.sp)
                        Text(
                            "$streakCount",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = GoldAccent
                            )
                        )
                    }
                }
            }
            // Profile avatar
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(LightGreen)
                    .border(1.5.dp, PrimaryGreen.copy(alpha = 0.3f), CircleShape)
                    .clickable { onProfileClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Profile",
                    tint = PrimaryGreen,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════════
// QUOTE CARD
// ══════════════════════════════════════════════════════════════════
@Composable
private fun QuoteCard(quote: String, source: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, DividerLight)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(LightGreen, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("✦", color = PrimaryGreen, fontSize = 18.sp)
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "\"$quote\"",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontStyle = FontStyle.Italic,
                        color = DarkText,
                        lineHeight = 22.sp
                    )
                )
                Text(
                    text = "— $source",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = AccentTeal,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════════
// TODAY'S JOURNEY CARD — Premium gradient style
// ══════════════════════════════════════════════════════════════════
@Composable
private fun TodayJourneyCard(
    currentTask: String,
    completedCount: Int,
    journeyProgress: Float,
    progressLabel: String,
    journeyComplete: Boolean,
    onContinue: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clickable { onContinue() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(PrimaryGreen, AccentTeal)
                    ),
                    shape = RoundedCornerShape(24.dp)
                )
        ) {
            // Decorative geometric element
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(120.dp)
                    .offset(x = 20.dp, y = (-20).dp)
                    .background(Color.White.copy(alpha = 0.05f), CircleShape)
            )
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(80.dp)
                    .offset(x = 10.dp, y = 20.dp)
                    .background(Color.White.copy(alpha = 0.08f), CircleShape)
            )

            Column(modifier = Modifier.padding(24.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color.White.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "TODAY'S JOURNEY",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (journeyComplete) "All Done! 🎉" else currentTask,
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "$completedCount / 4 tasks completed",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        )
                    }

                    // Circular progress
                    Box(
                        modifier = Modifier.size(72.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            progress = { journeyProgress },
                            modifier = Modifier.fillMaxSize(),
                            color = OrangeAccent,
                            strokeWidth = 6.dp,
                            trackColor = Color.White.copy(alpha = 0.2f),
                            strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                        )
                        Text(
                            text = progressLabel,
                            style = MaterialTheme.typography.titleSmall.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Progress bar
                LinearProgressIndicator(
                    progress = { journeyProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = OrangeAccent,
                    trackColor = Color.White.copy(alpha = 0.2f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onContinue,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = when {
                            journeyComplete -> "View Journey"
                            completedCount == 0 -> "Begin Today"
                            else -> "Continue →"
                        },
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = PrimaryGreen,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════════
// STATS ROW
// ══════════════════════════════════════════════════════════════════
@Composable
private fun StatsRow(streakCount: Int, completedTasks: Int, logsCount: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(
            icon = "🔥",
            value = "$streakCount",
            label = "Day Streak",
            modifier = Modifier.weight(1f)
        )
        StatCard(
            icon = "✅",
            value = "$completedTasks",
            label = "Today",
            modifier = Modifier.weight(1f)
        )
        StatCard(
            icon = "📖",
            value = "$logsCount",
            label = "Recitations",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun StatCard(icon: String, value: String, label: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        border = BorderStroke(1.dp, DividerLight),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(icon, fontSize = 22.sp)
            Text(
                value,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )
            )
            Text(
                label,
                style = MaterialTheme.typography.labelSmall.copy(color = GrayText),
                maxLines = 1
            )
        }
    }
}

// ══════════════════════════════════════════════════════════════════
// SECTION HEADER
// ══════════════════════════════════════════════════════════════════
@Composable
fun SectionHeader(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            color = DarkText
        ),
        modifier = modifier
    )
}

// ══════════════════════════════════════════════════════════════════
// BENTO GRID — 2×3 library grid
// ══════════════════════════════════════════════════════════════════
@Composable
private fun BentoGrid(navController: NavController) {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Row 1: Qur'an (2 cols) + Hadith (1 col)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            BentoItemLarge(
                label = "Qur'an",
                subtitle = "Browse & recite 114 Surahs",
                icon = Icons.AutoMirrored.Filled.MenuBook,
                modifier = Modifier.weight(2f),
                onClick = { navController.navigate("surah_list") }
            )
            BentoItemSquare(
                label = "Hadith",
                icon = Icons.Filled.HistoryEdu,
                modifier = Modifier.weight(1f),
                onClick = { navController.navigate("hadith_hub") }
            )
        }

        // Row 2: Stories (1 col) + Journal (2 cols)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            BentoItemSquare(
                label = "Stories",
                icon = Icons.Filled.AutoStories,
                modifier = Modifier.weight(1f),
                onClick = { navController.navigate("prophet_stories") }
            )
            BentoItemLarge(
                label = "Journal",
                subtitle = "Record daily spiritual reflections",
                icon = Icons.Filled.EditNote,
                modifier = Modifier.weight(2f),
                onClick = { navController.navigate("reflection_journal") }
            )
        }

        // Row 3: Qaida (1 col) + Vocab (2 cols)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            BentoItemSquare(
                label = "Qaida",
                icon = Icons.Filled.School,
                modifier = Modifier.weight(1f),
                onClick = { navController.navigate("qaida") }
            )
            BentoItemLarge(
                label = "Vocab",
                subtitle = "Master essential Arabic words",
                icon = Icons.Filled.Translate,
                modifier = Modifier.weight(2f),
                onClick = { navController.navigate("vocabulary_builder") }
            )
        }
    }
}

@Composable
private fun BentoItemLarge(
    label: String,
    subtitle: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(110.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        border = BorderStroke(1.dp, DividerLight),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(LightGreen, RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        tint = PrimaryGreen,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Column {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = DarkText
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = GrayText
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = GrayText,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun BentoItemSquare(
    label: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(110.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        border = BorderStroke(1.dp, DividerLight),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(LightGreen, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = PrimaryGreen,
                    modifier = Modifier.size(20.dp)
                )
            }
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = DarkText
                )
            )
        }
    }
}

// ══════════════════════════════════════════════════════════════════
// DAILY HADITH CARD
// ══════════════════════════════════════════════════════════════════
@Composable
private fun DailyHadithCard(hadith: com.noorlearn.domain.model.Hadith?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = OrangeLight),
        border = BorderStroke(1.dp, OrangeAccent.copy(alpha = 0.2f)),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(OrangeAccent.copy(alpha = 0.15f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("☀", fontSize = 16.sp)
                }
                Text(
                    text = "HADITH OF THE DAY",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = GoldAccent,
                        letterSpacing = 1.sp
                    )
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            if (hadith != null) {
                Text(
                    text = "\"${hadith.translationEn}\"",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontStyle = FontStyle.Italic,
                        color = DarkText,
                        lineHeight = 22.sp
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "— ${hadith.source}",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = GoldAccent,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            } else {
                Text(
                    text = "Loading daily wisdom...",
                    style = MaterialTheme.typography.bodyMedium.copy(color = GrayText)
                )
            }
        }
    }
}

// ══════════════════════════════════════════════════════════════════
// AI CHAT CTA
// ══════════════════════════════════════════════════════════════════
@Composable
private fun AiChatCard(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        border = BorderStroke(1.dp, PrimaryGreen.copy(alpha = 0.3f)),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(LightGreen, RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.AutoAwesome,
                    contentDescription = null,
                    tint = PrimaryGreen,
                    modifier = Modifier.size(24.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Ask Noor AI",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )
                )
                Text(
                    "Islamic answers from Qur'an & Hadith",
                    style = MaterialTheme.typography.bodySmall.copy(color = GrayText)
                )
            }
            Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = GrayText)
        }
    }
}

// ══════════════════════════════════════════════════════════════════
// RECITATION LOG ITEM
// ══════════════════════════════════════════════════════════════════
@Composable
private fun RecitationLogItem(log: RecitationLog) {
    val isGood = log.accuracyScore >= 85f
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        border = BorderStroke(1.dp, DividerLight),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .background(if (isGood) LightGreen else OrangeLight, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.Mic,
                    contentDescription = null,
                    tint = if (isGood) PrimaryGreen else OrangeAccent,
                    modifier = Modifier.size(18.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Ayah ${log.ayahId}",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = DarkText
                    )
                )
                Text(
                    log.transcribedText.take(35).let {
                        if (log.transcribedText.length > 35) "$it…" else it
                    },
                    style = MaterialTheme.typography.bodySmall.copy(color = GrayText),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (isGood) LightGreen else OrangeLight
            ) {
                Text(
                    "%.0f%%".format(log.accuracyScore),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (isGood) PrimaryGreen else GoldAccent
                    )
                )
            }
        }
    }
}

// Legacy composables kept for backward compatibility
@Composable
fun ExploreCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    BentoItemSquare(label = title, icon = icon, modifier = modifier.height(160.dp), onClick = onClick)
}

@Composable
fun RecitationHistorySection(logs: List<RecitationLog>) {
    logs.take(3).forEach { log ->
        RecitationLogItem(log = log)
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun RecitationProgressChart(logs: List<RecitationLog>) {
    // Embedded in main screen now
}

@Composable
private fun ContinueReadingCard(
    surahId: Int?,
    surahName: String?,
    onNavigate: (Int, String) -> Unit
) {
    val finalId = surahId ?: 67
    val finalName = surahName ?: "Al-Mulk"
    val isNew = surahId == null

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        border = BorderStroke(1.dp, DividerLight),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(LightGreen, RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.MenuBook,
                    contentDescription = null,
                    tint = PrimaryGreen,
                    modifier = Modifier.size(24.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "CONTINUE READING",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = AccentTeal,
                        letterSpacing = 1.sp
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Surah $finalName",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )
                )
                Text(
                    text = if (isNew) "Start reading today's recommended Surah" else "Resume from Ayah 1",
                    style = MaterialTheme.typography.bodySmall.copy(color = GrayText)
                )
            }
            Button(
                onClick = { onNavigate(finalId, finalName) },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = if (isNew) "Start" else "Resume",
                    color = Color.White,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@Composable
private fun PrayerTimesCard() {
    val calendar = remember { Calendar.getInstance() }
    val prayerTimes = remember(calendar) { com.noorlearn.data.local.PrayerTimesHelper.getPrayerTimes(calendar) }
    val (nextPrayer, countdown) = remember(calendar, prayerTimes) {
        com.noorlearn.data.local.PrayerTimesHelper.getNextPrayer(calendar, prayerTimes)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        border = BorderStroke(1.dp, DividerLight),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("⏰", fontSize = 16.sp)
                    Text(
                        text = "PRAYER TIMES",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = DarkText,
                            letterSpacing = 1.sp
                        )
                    )
                }
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = OrangeLight,
                    border = BorderStroke(0.5.dp, OrangeAccent.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = "Next: ${nextPrayer.name} in $countdown",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = GoldAccent
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                prayerTimes.forEach { prayer ->
                    val isNext = prayer.name == nextPrayer.name
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isNext) LightGreen else Color.Transparent)
                            .border(
                                width = if (isNext) 1.dp else 0.dp,
                                color = if (isNext) PrimaryGreen.copy(alpha = 0.5f) else Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(vertical = 8.dp)
                    ) {
                        Text(
                            text = prayer.icon,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = prayer.name,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (isNext) FontWeight.Bold else FontWeight.Medium,
                                color = if (isNext) PrimaryGreen else GrayText
                            )
                        )
                        Text(
                            text = prayer.time.substringBefore(" "),
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = if (isNext) FontWeight.Bold else FontWeight.Normal,
                                color = if (isNext) PrimaryGreen else DarkText
                            )
                        )
                        Text(
                            text = prayer.time.substringAfter(" "),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 8.sp,
                                color = if (isNext) PrimaryGreen.copy(alpha = 0.7f) else LightGrayText
                            )
                        )
                    }
                }
            }
        }
    }
}
