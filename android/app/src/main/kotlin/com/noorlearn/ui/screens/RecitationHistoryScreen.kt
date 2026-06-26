package com.noorlearn.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.noorlearn.domain.model.RecitationLog
import com.noorlearn.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecitationHistoryScreen(
    navController: NavController,
    viewModel: RecitationHistoryViewModel = hiltViewModel()
) {
    val recitationLogs by viewModel.recitationLogs.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    var selectedLog by remember { mutableStateOf<RecitationLog?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Recitation History",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = PrimaryGreen
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = PrimaryGreen
                        )
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .gridBackground()
                .padding(padding)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = PrimaryGreen
                )
            } else if (error != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Something went wrong",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = DarkText
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = error ?: "Unknown error",
                        style = MaterialTheme.typography.bodyMedium,
                        color = GrayText,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { viewModel.loadRecitationLogs() },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                    ) {
                        Text("Retry", color = Color.White)
                    }
                }
            } else if (recitationLogs.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .background(LightGreen, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Mic,
                            contentDescription = null,
                            tint = PrimaryGreen,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "No Recitations Recorded",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = DarkText
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Go to the Qur'an reader and tap the microphone icon next to any verse to start practicing your recitation.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = GrayText,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { navController.navigate("surah_list") },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Open Qur'an", color = Color.White)
                    }
                }
            } else {
                val averageAccuracy = recitationLogs.map { it.accuracyScore }.average().toFloat()
                val highestAccuracy = recitationLogs.map { it.accuracyScore }.maxOrNull() ?: 0f

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        // Summary Stats Card
                        Card(
                            modifier = Modifier.fillMaxWidth(),
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
                                    .padding(20.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Text(
                                            text = "AVERAGE ACCURACY",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = Color.White.copy(alpha = 0.8f),
                                                fontWeight = FontWeight.Bold,
                                                letterSpacing = 1.sp
                                            )
                                        )
                                        Text(
                                            text = "%.1f%%".format(averageAccuracy),
                                            style = MaterialTheme.typography.headlineLarge.copy(
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold
                                            )
                                        )
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                             Icon(
                                                 imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                                                 contentDescription = null,
                                                 tint = OrangeAccent,
                                                 modifier = Modifier.size(16.dp)
                                             )
                                            Text(
                                                text = "Highest: %.0f%%".format(highestAccuracy),
                                                style = MaterialTheme.typography.bodySmall.copy(color = OrangeLight)
                                            )
                                        }
                                    }
                                    Box(
                                        modifier = Modifier
                                            .size(64.dp)
                                            .background(Color.White.copy(alpha = 0.15f), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "${recitationLogs.size}",
                                            style = MaterialTheme.typography.titleLarge.copy(
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Text(
                            text = "All Recitation Attempts",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = DarkText,
                            modifier = Modifier.padding(top = 12.dp)
                        )
                    }

                    items(recitationLogs) { log ->
                        val isGood = log.accuracyScore >= 85f
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedLog = log },
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
                                        .size(44.dp)
                                        .background(if (isGood) LightGreen else OrangeLight, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Mic,
                                        contentDescription = null,
                                        tint = if (isGood) PrimaryGreen else OrangeAccent,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Ayah ID: ${log.ayahId}",
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = DarkText
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = log.transcribedText.ifBlank { "No speech detected" },
                                        style = MaterialTheme.typography.bodyMedium.copy(color = GrayText),
                                        maxLines = 1,
                                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = log.createdAt.substringBefore("T"),
                                        style = MaterialTheme.typography.bodySmall.copy(color = LightGrayText)
                                    )
                                }
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isGood) LightGreen else OrangeLight
                                ) {
                                    Text(
                                        text = "%.0f%%".format(log.accuracyScore),
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
                }
            }
        }
    }

    // Detail dialog
    selectedLog?.let { log ->
        AlertDialog(
            onDismissRequest = { selectedLog = null },
            confirmButton = {
                TextButton(onClick = { selectedLog = null }) {
                    Text("Close", color = PrimaryGreen, fontWeight = FontWeight.Bold)
                }
            },
            title = {
                Text(
                    text = "Recitation Details",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = PrimaryGreen
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Ayah Practice Session",
                            style = MaterialTheme.typography.bodySmall.copy(color = GrayText)
                        )
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (log.accuracyScore >= 85f) LightGreen else OrangeLight
                        ) {
                            Text(
                                text = "Score: %.0f%%".format(log.accuracyScore),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (log.accuracyScore >= 85f) PrimaryGreen else GoldAccent
                                )
                            )
                        }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "TRANSCRIBED SPEECH",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = AccentTeal,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = log.transcribedText.ifBlank { "No speech detected" },
                            style = MaterialTheme.typography.bodyLarge,
                            color = DarkText
                        )
                    }

                    if (!log.feedbackText.isNullOrBlank()) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "AI PRONUNCIATION TIPS",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = GoldAccent,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = log.feedbackText,
                                style = MaterialTheme.typography.bodyMedium,
                                color = DarkText,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(24.dp)
        )
    }
}
