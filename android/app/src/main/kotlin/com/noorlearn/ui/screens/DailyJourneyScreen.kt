package com.noorlearn.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.noorlearn.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyJourneyScreen(
    navController: NavController,
    viewModel: DailyJourneyViewModel = hiltViewModel()
) {
    val streakCount by viewModel.streakCount.collectAsState()
    val noorPoints by viewModel.noorPoints.collectAsState()
    val completedTasks by viewModel.completedTasks.collectAsState()

    // Determine task states dynamically in order
    val taskStates = remember(completedTasks) {
        val states = mutableMapOf<String, String>() // "completed", "current", "locked"
        var foundCurrent = false

        viewModel.tasks.forEach { task ->
            if (completedTasks.contains(task.id)) {
                states[task.id] = "completed"
            } else if (!foundCurrent) {
                states[task.id] = "current"
                foundCurrent = true
            } else {
                states[task.id] = "locked"
            }
        }
        states
    }

    val completedCount = completedTasks.size
    val progressPercent = if (viewModel.tasks.isEmpty()) 0 else (completedCount * 100 / viewModel.tasks.size)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Daily Journey", fontWeight = FontWeight.Bold, color = PrimaryGreen)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = PrimaryGreen)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = PrimaryGreen
                )
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
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Title
            Text(
                text = "The Path of Light",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = DarkText,
                fontFamily = androidx.compose.ui.text.font.FontFamily.Serif
            )
            
            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle
            Text(
                text = "Nurture your soul with consistent daily steps.\nYour journey today is $progressPercent% complete.",
                fontSize = 15.sp,
                color = GrayText,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Stats row
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$streakCount",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )
                    Text(
                        text = "DAY STREAK",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = GrayText
                    )
                }

                // Vertical divider line
                Box(
                    modifier = Modifier
                        .height(36.dp)
                        .width(1.dp)
                        .background(DividerLight)
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$noorPoints",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )
                    Text(
                        text = "NOOR POINTS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = GrayText
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Timeline Items
            viewModel.tasks.forEachIndexed { index, task ->
                val state = taskStates[task.id] ?: "locked"
                val isLast = index == viewModel.tasks.size - 1

                TimelineNode(
                    task = task,
                    state = state,
                    isLast = isLast,
                    onStartClick = {
                        // Mark as in-progress/complete and navigate
                        viewModel.toggleTask(task.id)
                        if (task.route.isNotEmpty()) {
                            navController.navigate(task.route)
                        }
                    },
                    onToggleComplete = {
                        viewModel.toggleTask(task.id)
                    }
                )
            }

            // Finish Goal Node (Trophy)
            val allCompleted = completedCount == viewModel.tasks.size
            TimelineTrophyNode(isCompleted = allCompleted)

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun TimelineNode(
    task: JourneyTask,
    state: String, // "completed", "current", "locked"
    isLast: Boolean,
    onStartClick: () -> Unit,
    onToggleComplete: () -> Unit
) {
    val isCompleted = state == "completed"
    val isCurrent = state == "current"
    val isLocked = state == "locked"

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        // 1. Icon Circle (Node)
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(
                    color = when {
                        isCompleted -> SuccessGreen.copy(alpha = 0.15f)
                        isCurrent -> GoldAccent.copy(alpha = 0.15f)
                        else -> Color.Transparent
                    },
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = when {
                            isCompleted -> SuccessGreen
                            isCurrent -> GoldAccent
                            else -> Color(0xFFF0EFEA)
                        },
                        shape = CircleShape
                    )
                    .border(
                        width = 2.dp,
                        color = when {
                            isCompleted -> Color.White
                            isCurrent -> Color.White
                            else -> DividerLight
                        },
                        shape = CircleShape
                    )
                    .clickable {
                        // Allow clicking node to toggle/complete task
                        if (!isLocked) {
                            onToggleComplete()
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when {
                        isCompleted -> Icons.Default.Check
                        isLocked -> Icons.Default.Lock
                        else -> getIconForType(task.iconType)
                    },
                    contentDescription = null,
                    tint = if (isLocked) GrayText else Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 2. Card Content
        Box(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .padding(horizontal = 8.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        if (isCurrent) Modifier.border(
                            1.dp,
                            GoldAccent,
                            RoundedCornerShape(20.dp)
                        ) else Modifier
                    ),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isCompleted || isLocked) Color(0xFFF2F0EA) else CardWhite
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = if (isCurrent) 4.dp else 0.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (isCompleted) {
                        Text(
                            text = "COMPLETED",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = SuccessGreen
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = task.title,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkText,
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Serif
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = task.description,
                            fontSize = 14.sp,
                            fontStyle = FontStyle.Italic,
                            color = GrayText,
                            textAlign = TextAlign.Center
                        )
                    } else if (isCurrent) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFF7A601E),
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(10.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "CURRENT TASK",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                        Text(
                            text = task.title,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkText,
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Serif
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = task.description,
                            fontSize = 14.sp,
                            color = GrayText,
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = onStartClick,
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                            shape = RoundedCornerShape(20.dp),
                            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 10.dp)
                        ) {
                            val buttonText = when (task.id) {
                                "morning_adhkar" -> "Start Adhkar"
                                "surah_reading" -> "Start Reading"
                                "hadith_day" -> "Read Hadith"
                                "reflection_journal" -> "Write Reflection"
                                else -> "Start"
                            }
                            Text(buttonText, color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        // Locked
                        Text(
                            text = if (task.id == "reflection_journal") "EVENING" else "UP NEXT",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = GrayText
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = task.title,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = GrayText.copy(alpha = 0.7f),
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Serif
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                Icons.Default.AccessTime,
                                contentDescription = null,
                                tint = GrayText.copy(alpha = 0.6f),
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (task.id == "reflection_journal") "Reflect on your growth today." else "Unlocks after previous task",
                                fontSize = 13.sp,
                                color = GrayText.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
            }
        }

        // 3. Connector line below the card
        if (!isLast) {
            val lineColor = if (isCompleted) SuccessGreen else DividerLight
            val isDashed = !isCompleted

            Box(
                modifier = Modifier
                    .height(60.dp)
                    .width(24.dp),
                contentAlignment = Alignment.Center
            ) {
                if (isDashed) {
                    Canvas(modifier = Modifier.fillMaxHeight().width(3.dp)) {
                        val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                        drawLine(
                            color = Color(0xFFCCCCCC),
                            start = Offset(size.width / 2, 0f),
                            end = Offset(size.width / 2, size.height),
                            strokeWidth = 2.dp.toPx(),
                            pathEffect = pathEffect
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(3.dp)
                            .background(lineColor)
                    )
                }
            }
        }
    }
}

@Composable
fun TimelineTrophyNode(isCompleted: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Connector line above trophy
        Box(
            modifier = Modifier
                .height(40.dp)
                .width(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxHeight().width(3.dp)) {
                val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                drawLine(
                    color = if (isCompleted) SuccessGreen else Color(0xFFCCCCCC),
                    start = Offset(size.width / 2, 0f),
                    end = Offset(size.width / 2, size.height),
                    strokeWidth = 2.dp.toPx(),
                    pathEffect = if (isCompleted) null else pathEffect
                )
            }
        }

        // Trophy Circle
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(
                    color = if (isCompleted) GoldAccent.copy(alpha = 0.15f) else Color.Transparent,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = if (isCompleted) GoldAccent else Color(0xFFF0EFEA),
                        shape = CircleShape
                    )
                    .border(
                        width = 2.dp,
                        color = if (isCompleted) Color.White else DividerLight,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = if (isCompleted) Color.White else GrayText,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Footnote
        Text(
            text = "Complete all tasks to achieve your\nDaily Focus goal.",
            fontSize = 14.sp,
            color = GrayText,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
    }
}

private fun getIconForType(type: String): ImageVector {
    return when (type) {
        "adhkar" -> Icons.Default.LightMode
        "quran" -> Icons.AutoMirrored.Filled.MenuBook
        "hadith" -> Icons.Default.AutoAwesome
        "reflection" -> Icons.Default.Create
        else -> Icons.Default.Star
    }
}
