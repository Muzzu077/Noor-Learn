package com.noorlearn.ui.screens

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Replay10
import androidx.compose.material.icons.filled.Forward10
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.noorlearn.domain.model.Ayah
import com.noorlearn.data.speech.SpeechState
import com.noorlearn.data.speech.AlignedWord
import com.noorlearn.data.speech.WordStatus
import com.noorlearn.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AyahReaderScreen(
    navController: NavController,
    surahId: Int,
    surahName: String,
    viewModel: AyahReaderViewModel = hiltViewModel()
) {
    val ayahs by viewModel.ayahs.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val audioUrl by viewModel.audioUrl.collectAsState()
    val isAudioLoading by viewModel.isAudioLoading.collectAsState()
    val selectedReciter by viewModel.selectedReciter.collectAsState()
    val bookmarkedAyahIds by viewModel.bookmarkedAyahIds.collectAsState()
    val explainAyahId by viewModel.explainAyahId.collectAsState()
    val explanation by viewModel.explanation.collectAsState()
    val isExplaining by viewModel.isExplaining.collectAsState()

    val recitationState by viewModel.recitationState.collectAsState()
    val alignedWords by viewModel.alignedWords.collectAsState()
    val recitationScore by viewModel.recitationScore.collectAsState()
    val recitationFeedback by viewModel.recitationFeedback.collectAsState()
    val isFeedbackLoading by viewModel.isFeedbackLoading.collectAsState()
    val activeRecitationAyahId by viewModel.activeRecitationAyahId.collectAsState()

    val context = LocalContext.current
    var permissionTargetAyahId by remember { mutableStateOf<Int?>(null) }

    val micPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            val ayahId = permissionTargetAyahId
            if (isGranted && ayahId != null) {
                viewModel.startRecitation(ayahId)
            } else if (!isGranted) {
                Toast.makeText(context, "Microphone permission is required for recitation feedback.", Toast.LENGTH_SHORT).show()
            }
            permissionTargetAyahId = null
        }
    )

    LaunchedEffect(surahId, surahName) {
        viewModel.loadAyahs(surahId, surahName)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            surahName,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryGreen,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            "Surah $surahId",
                            color = GrayText,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
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
        ) {
            // Audio Player Bar
            SurahAudioPlayer(
                audioUrl = audioUrl,
                isAudioLoading = isAudioLoading,
                selectedReciter = selectedReciter,
                onReciterSelected = { viewModel.selectReciter(it) }
            )

            when {
                isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = PrimaryGreen)
                    }
                }
                error != null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = error ?: "",
                                color = GrayText,
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(32.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { viewModel.loadAyahs(surahId, surahName) },
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                            ) {
                                Text("Retry")
                            }
                        }
                    }
                }
                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Bismillah header (except for Surah At-Tawbah #9)
                        if (surahId != 9) {
                            item {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = PrimaryGreen)
                                ) {
                                    Text(
                                        text = "\u0628\u0650\u0633\u0652\u0645\u0650 \u0627\u0644\u0644\u0651\u064E\u0647\u0650 \u0627\u0644\u0631\u0651\u064E\u062D\u0652\u0645\u064E\u0670\u0646\u0650 \u0627\u0644\u0631\u0651\u064E\u062D\u0650\u064A\u0645\u0650",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(20.dp),
                                        style = MaterialTheme.typography.headlineSmall.copy(
                                            fontFamily = ArabicFontFamily,
                                            fontWeight = FontWeight.Medium,
                                            lineHeight = 40.sp
                                        ),
                                        color = Color.White,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }

                        items(ayahs, key = { it.id }) { ayah ->
                            val isActive = activeRecitationAyahId == ayah.id
                            AyahCard(
                                ayah = ayah,
                                isBookmarked = ayah.id in bookmarkedAyahIds,
                                onBookmarkToggle = { viewModel.toggleBookmark(ayah.id) },
                                onExplainClick = { viewModel.explainAyah(ayah) },
                                isExplainLoading = isExplaining && explainAyahId == ayah.id,
                                explanationText = if (explainAyahId == ayah.id) explanation else null,
                                onDismissExplanation = { viewModel.dismissExplanation() },
                                
                                recitationState = if (isActive) recitationState else SpeechState.Idle,
                                isActiveRecitation = isActive,
                                alignedWords = if (isActive) alignedWords else null,
                                recitationScore = if (isActive) recitationScore else null,
                                recitationFeedback = if (isActive) recitationFeedback else null,
                                isFeedbackLoading = isActive && isFeedbackLoading,
                                onReciteClick = {
                                    permissionTargetAyahId = ayah.id
                                    micPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                },
                                onStopRecitation = { viewModel.stopRecitation() },
                                onDismissRecitation = { viewModel.resetRecitation() }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SurahAudioPlayer(
    audioUrl: String?,
    isAudioLoading: Boolean,
    selectedReciter: Reciter,
    onReciterSelected: (Reciter) -> Unit
) {
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(false) }
    var showReciterPicker by remember { mutableStateOf(false) }
    var progress by remember { mutableFloatStateOf(0f) }
    var duration by remember { mutableLongStateOf(0L) }

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(C.USAGE_MEDIA)
                .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                .build()
            setAudioAttributes(audioAttributes, true)
            
            addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(state: Int) {
                    if (state == Player.STATE_ENDED) {
                        isPlaying = false
                        progress = 0f
                    }
                }

                override fun onIsPlayingChanged(playing: Boolean) {
                    isPlaying = playing
                }

                override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
                    android.util.Log.e("AyahReaderScreen", "ExoPlayer playback error", error)
                    Toast.makeText(context, "Audio error: ${error.localizedMessage}", Toast.LENGTH_SHORT).show()
                    isPlaying = false
                }
            })
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE || event == Lifecycle.Event.ON_STOP) {
                exoPlayer.pause()
                isPlaying = false
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // Update progress while playing
    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            val pos = exoPlayer.currentPosition
            val dur = exoPlayer.duration
            if (dur > 0) {
                progress = pos.toFloat() / dur.toFloat()
                duration = dur
            }
            kotlinx.coroutines.delay(500)
        }
    }

    // Load new audio when URL changes
    LaunchedEffect(audioUrl) {
        if (audioUrl != null) {
            exoPlayer.stop()
            exoPlayer.clearMediaItems()
            exoPlayer.setMediaItem(MediaItem.fromUri(audioUrl))
            exoPlayer.prepare()
            isPlaying = false
            progress = 0f
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = LightGreenSoft),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Reciter selector row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Reciter",
                        style = MaterialTheme.typography.labelSmall,
                        color = GrayText
                    )
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { showReciterPicker = !showReciterPicker }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            selectedReciter.name,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = PrimaryGreen
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            Icons.Filled.KeyboardArrowDown,
                            contentDescription = "Change reciter",
                            tint = GrayText,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                if (isAudioLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(32.dp),
                        color = PrimaryGreen,
                        strokeWidth = 2.dp
                    )
                }
            }

            // Reciter picker dropdown
            AnimatedVisibility(
                visible = showReciterPicker,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column {
                        RECITERS.forEach { reciter ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onReciterSelected(reciter)
                                        showReciterPicker = false
                                    }
                                    .padding(horizontal = 16.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = reciter.id == selectedReciter.id,
                                    onClick = {
                                        onReciterSelected(reciter)
                                        showReciterPicker = false
                                    },
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = OrangeAccent,
                                        unselectedColor = GrayText.copy(alpha = 0.5f)
                                    )
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    reciter.name,
                                    color = DarkText,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = if (reciter.id == selectedReciter.id) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Progress bar
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = OrangeAccent,
                trackColor = BorderLight
            )

            // Time labels
            if (duration > 0) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        formatTime((progress * duration).toLong()),
                        style = MaterialTheme.typography.labelSmall,
                        color = GrayText
                    )
                    Text(
                        formatTime(duration),
                        style = MaterialTheme.typography.labelSmall,
                        color = GrayText
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Playback controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Rewind 10s
                IconButton(
                    onClick = {
                        exoPlayer.seekTo(maxOf(0, exoPlayer.currentPosition - 10_000))
                    },
                    enabled = audioUrl != null
                ) {
                    Icon(
                        Icons.Filled.Replay10,
                        contentDescription = "Rewind 10s",
                        tint = PrimaryGreen.copy(alpha = if (audioUrl != null) 0.9f else 0.3f),
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Play / Pause
                IconButton(
                    onClick = {
                        if (audioUrl == null) return@IconButton
                        if (isPlaying) {
                            exoPlayer.pause()
                        } else {
                            exoPlayer.play()
                        }
                    },
                    enabled = audioUrl != null && !isAudioLoading,
                    modifier = Modifier
                        .size(56.dp)
                        .background(
                            if (audioUrl != null) OrangeAccent else OrangeAccent.copy(alpha = 0.3f),
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Forward 10s
                IconButton(
                    onClick = {
                        exoPlayer.seekTo(minOf(exoPlayer.duration, exoPlayer.currentPosition + 10_000))
                    },
                    enabled = audioUrl != null
                ) {
                    Icon(
                        Icons.Filled.Forward10,
                        contentDescription = "Forward 10s",
                        tint = PrimaryGreen.copy(alpha = if (audioUrl != null) 0.9f else 0.3f),
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}

private fun formatTime(ms: Long): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%d:%02d".format(minutes, seconds)
}

@Composable
fun AyahCard(
    ayah: Ayah,
    isBookmarked: Boolean = false,
    onBookmarkToggle: () -> Unit = {},
    onExplainClick: () -> Unit = {},
    isExplainLoading: Boolean = false,
    explanationText: String? = null,
    onDismissExplanation: () -> Unit = {},
    
    // Recitation parameters
    recitationState: SpeechState = SpeechState.Idle,
    isActiveRecitation: Boolean = false,
    alignedWords: List<AlignedWord>? = null,
    recitationScore: Float? = null,
    recitationFeedback: String? = null,
    isFeedbackLoading: Boolean = false,
    onReciteClick: () -> Unit = {},
    onStopRecitation: () -> Unit = {},
    onDismissRecitation: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Ayah number badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = LightGreen.copy(alpha = 0.5f)
                ) {
                    Text(
                        text = "Ayah ${ayah.ayahNumber}",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        color = PrimaryGreen,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }

                IconButton(
                    onClick = onBookmarkToggle,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = if (isBookmarked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = if (isBookmarked) "Remove bookmark" else "Bookmark",
                        tint = if (isBookmarked) ErrorRed else GrayText.copy(alpha = 0.5f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Arabic text
            Text(
                text = ayah.arabicText,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontFamily = ArabicFontFamily,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 48.sp
                ),
                color = DarkText,
                textAlign = TextAlign.Right,
                modifier = Modifier.fillMaxWidth()
            )

            if (ayah.transliteration.isNotBlank()) {
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = DividerLight.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = ayah.transliteration,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontStyle = FontStyle.Italic,
                        lineHeight = 24.sp
                    ),
                    color = DarkText.copy(alpha = 0.7f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            if (ayah.transliteration.isBlank()) {
                HorizontalDivider(color = DividerLight.copy(alpha = 0.3f))
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Translation
            Text(
                text = ayah.translationEn,
                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                color = GrayText
            )

            // Tafsir
            if (ayah.tafsirShort.isNotBlank()) {
                Spacer(modifier = Modifier.height(16.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = LightGreenSoft
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "Context / Tafsir",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = PrimaryGreen
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = ayah.tafsirShort,
                            style = MaterialTheme.typography.bodySmall.copy(lineHeight = 20.sp),
                            color = PrimaryGreenDark
                        )
                    }
                }
            }

            // Recitation Practice Panel
            if (isActiveRecitation) {
                Spacer(modifier = Modifier.height(16.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = BeigeBackground,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Recitation Practice",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = PrimaryGreenDark
                            )
                            IconButton(onClick = onStopRecitation, modifier = Modifier.size(24.dp)) {
                                Icon(Icons.Filled.Close, contentDescription = "Close", tint = GrayText)
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        when {
                            recitationState is SpeechState.Listening -> {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(
                                        Icons.Filled.Mic,
                                        contentDescription = "Listening",
                                        tint = ErrorRed,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        "Listening... Speak now",
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                                        color = ErrorRed
                                    )
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = onStopRecitation,
                                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Stop & Evaluate")
                                }
                            }
                            
                            recitationState is SpeechState.Processing || isFeedbackLoading -> {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center,
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(20.dp),
                                        color = PrimaryGreen,
                                        strokeWidth = 2.dp
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text("Evaluating recitation...", style = MaterialTheme.typography.bodyMedium, color = GrayText)
                                }
                            }
                            
                            recitationScore != null -> {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    val badgeColor = if (recitationScore >= 85f) PrimaryGreen else OrangeAccent
                                    val badgeBg = if (recitationScore >= 85f) LightGreenSoft else OrangeLight
                                    
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = badgeBg,
                                        modifier = Modifier.align(Alignment.Start)
                                    ) {
                                        Text(
                                            "Accuracy: %.0f%%".format(recitationScore),
                                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                            color = badgeColor,
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                        )
                                    }
                                    
                                    Spacer(modifier = Modifier.height(12.dp))
                                    
                                    if (alignedWords != null) {
                                        Text(
                                            text = "Aligned Word Results (Incorrect/Skipped highlighted):",
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            color = GrayText
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        
                                        val highlightedText = buildAnnotatedString {
                                            alignedWords.forEachIndexed { index, aligned ->
                                                val color = when (aligned.status) {
                                                    WordStatus.CORRECT -> PrimaryGreen
                                                    WordStatus.INCORRECT -> ErrorRed
                                                    WordStatus.SKIPPED -> GrayText.copy(alpha = 0.5f)
                                                }
                                                withStyle(style = SpanStyle(color = color, fontWeight = FontWeight.Bold, fontSize = 20.sp, fontFamily = ArabicFontFamily)) {
                                                    append(aligned.word)
                                                }
                                                if (index < alignedWords.lastIndex) {
                                                    append(" ")
                                                }
                                            }
                                        }
                                        
                                        Text(
                                            text = highlightedText,
                                            style = MaterialTheme.typography.bodyLarge,
                                            textAlign = TextAlign.Right,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(Color.White, RoundedCornerShape(8.dp))
                                                .padding(12.dp)
                                        )
                                    }
                                    
                                    if (!recitationFeedback.isNullOrBlank()) {
                                        Spacer(modifier = Modifier.height(12.dp))
                                        Text(
                                            "AI Pronunciation Tip:",
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            color = GrayText
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = Color.White,
                                            border = ButtonDefaults.outlinedButtonBorder(enabled = true),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(
                                                recitationFeedback,
                                                style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                                                color = DarkText,
                                                modifier = Modifier.padding(10.dp)
                                            )
                                        }
                                    }
                                    
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Button(
                                            onClick = onReciteClick,
                                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text("Recite Again")
                                        }
                                        OutlinedButton(
                                            onClick = onDismissRecitation,
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text("Close")
                                        }
                                    }
                                }
                            }
                            
                            recitationState is SpeechState.Error -> {
                                Text(
                                    (recitationState as SpeechState.Error).message,
                                    color = ErrorRed,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Button(
                                        onClick = onReciteClick,
                                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Retry")
                                    }
                                    OutlinedButton(
                                        onClick = onDismissRecitation,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Cancel")
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action row (Explain This + Recite Ayah)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Explain This button
                if (explanationText == null && !isExplainLoading && !isActiveRecitation) {
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

                // Recite Button
                if (!isActiveRecitation) {
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { onReciteClick() },
                        shape = RoundedCornerShape(20.dp),
                        color = LightGreenSoft
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Filled.Mic,
                                contentDescription = null,
                                tint = PrimaryGreen,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                "Recite Ayah",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = PrimaryGreen
                            )
                        }
                    }
                }
            }

            if (isExplainLoading) {
                Spacer(modifier = Modifier.height(8.dp))
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
                Spacer(modifier = Modifier.height(8.dp))
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
