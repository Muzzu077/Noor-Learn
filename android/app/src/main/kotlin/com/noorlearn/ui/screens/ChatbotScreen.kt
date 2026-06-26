package com.noorlearn.ui.screens

import android.content.Intent
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.automirrored.rounded.VolumeOff
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.noorlearn.ui.theme.*
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatbotScreen(
    navController: NavController,
    viewModel: ChatbotViewModel = hiltViewModel()
) {
    val messages by viewModel.messages.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var textState by remember { mutableStateOf(TextFieldValue("")) }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    
    // Text-To-Speech Setup
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    var ttsSpeakingMessageId by remember { mutableStateOf<String?>(null) }

    DisposableEffect(context) {
        var ttsInstance: TextToSpeech? = null
        ttsInstance = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                ttsInstance?.setOnUtteranceProgressListener(object : android.speech.tts.UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {}
                    override fun onDone(utteranceId: String?) {
                        coroutineScope.launch {
                            ttsSpeakingMessageId = null
                        }
                    }
                    @Deprecated("Deprecated in Java")
                    override fun onError(utteranceId: String?) {
                        coroutineScope.launch {
                            ttsSpeakingMessageId = null
                        }
                    }
                })
            }
        }
        tts = ttsInstance
        onDispose {
            ttsInstance?.stop()
            ttsInstance?.shutdown()
        }
    }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size)
        }
    }

    val speakToggle = { messageId: String, text: String ->
        if (ttsSpeakingMessageId == messageId) {
            tts?.stop()
            ttsSpeakingMessageId = null
        } else {
            tts?.stop()
            val cleanText = text.replace("**", "").replace("*", "")
            tts?.language = Locale.US
            tts?.speak(cleanText, TextToSpeech.QUEUE_FLUSH, null, messageId)
            ttsSpeakingMessageId = messageId
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(contentAlignment = Alignment.BottomEnd) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .background(Color.White.copy(alpha = 0.15f), shape = RoundedCornerShape(10.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Rounded.SmartToy,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            OnlinePulseIndicator()
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                "NoorLearn AI",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                "Online Islamic Companion",
                                color = Color.White.copy(alpha = 0.65f),
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    if (messages.isNotEmpty()) {
                        IconButton(onClick = { 
                            viewModel.clearChat()
                            tts?.stop()
                            ttsSpeakingMessageId = null
                        }) {
                            Icon(
                                imageVector = Icons.Rounded.Delete,
                                contentDescription = "Clear Chat",
                                tint = Color.White
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryGreen
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
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(top = 8.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (messages.isEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(2.dp, RoundedCornerShape(24.dp))
                                .background(
                                    brush = Brush.linearGradient(listOf(PrimaryGreen, PrimaryGreenDark)),
                                    shape = RoundedCornerShape(24.dp)
                                )
                                .padding(24.dp)
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(56.dp)
                                            .background(Color.White.copy(alpha = 0.2f), shape = RoundedCornerShape(16.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            Icons.Rounded.SmartToy,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(32.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Column {
                                        Text(
                                            "Assalamu Alaikum!",
                                            style = MaterialTheme.typography.titleLarge.copy(
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold
                                            )
                                        )
                                        Text(
                                            "I am Noor, your Islamic AI",
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                color = Color.White.copy(alpha = 0.8f)
                                            )
                                        )
                                    }
                                }
                                
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                Text(
                                    "Ask me any question about the Qur'an, Hadith, Arabic vocabulary, or daily Islamic practices. I am here to help you learn and grow.",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = Color.White.copy(alpha = 0.9f),
                                        lineHeight = 20.sp
                                    )
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(28.dp))
                        
                        Text(
                            "Suggested Questions",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = DarkText,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        
                        SuggestionGrid { suggestion ->
                            viewModel.sendMessage(suggestion)
                        }
                    }
                }

                items(messages) { message ->
                    ChatBubble(
                        message = message,
                        onCopy = {
                            clipboardManager.setText(AnnotatedString(message.text))
                            Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
                        },
                        onShare = {
                            val sendIntent: Intent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, message.text)
                                type = "text/plain"
                            }
                            val shareIntent = Intent.createChooser(sendIntent, null)
                            context.startActivity(shareIntent)
                        },
                        isSpeaking = ttsSpeakingMessageId == message.id,
                        onSpeakToggle = {
                            speakToggle(message.id, message.text)
                        }
                    )
                }

                if (isLoading) {
                    item {
                        Row(
                            modifier = Modifier.padding(start = 40.dp, top = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(
                                    topStart = 16.dp,
                                    topEnd = 16.dp,
                                    bottomStart = 4.dp,
                                    bottomEnd = 16.dp
                                ),
                                color = CardWhite,
                                border = BorderStroke(1.dp, BorderLight),
                                shadowElevation = 1.dp
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    TypingIndicator()
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        "Thinking...",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = GrayText
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Floating Input bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BeigeBackground).gridBackground()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .navigationBarsPadding()
            ) {
                Surface(
                    shape = RoundedCornerShape(28.dp),
                    color = CardWhite,
                    shadowElevation = 3.dp,
                    border = BorderStroke(1.dp, BorderLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 6.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = textState,
                            onValueChange = { textState = it },
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 10.dp),
                            placeholder = { Text("Type your question...", color = LightGrayText, fontSize = 15.sp) },
                            maxLines = 4,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                disabledBorderColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent
                            )
                        )
                        
                        val canSend = textState.text.isNotBlank() && !isLoading
                        IconButton(
                            onClick = {
                                if (canSend) {
                                    viewModel.sendMessage(textState.text)
                                    textState = TextFieldValue("")
                                }
                            },
                            modifier = Modifier
                                .size(44.dp)
                                .background(
                                    brush = Brush.linearGradient(
                                        if (canSend) listOf(PrimaryGreen, PrimaryGreenDark)
                                        else listOf(PrimaryGreen.copy(alpha = 0.3f), PrimaryGreenDark.copy(alpha = 0.3f))
                                    ),
                                    shape = CircleShape
                                )
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = "Send",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OnlinePulseIndicator() {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseScale"
    )
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseAlpha"
    )

    Box(
        modifier = Modifier
            .offset(x = 2.dp, y = 2.dp)
            .size(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(scaleX = scale, scaleY = scale)
                .background(Color(0xFF0F9D58).copy(alpha = alpha), shape = CircleShape)
        )
        Box(
            modifier = Modifier
                .size(6.dp)
                .background(Color(0xFF0F9D58), shape = CircleShape)
        )
    }
}

@Composable
fun TypingIndicator(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(3) { index ->
            val translationY = remember { Animatable(0f) }
            LaunchedEffect(Unit) {
                kotlinx.coroutines.delay(index * 150L)
                while (true) {
                    translationY.animateTo(
                        targetValue = -8f,
                        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
                    )
                    translationY.animateTo(
                        targetValue = 0f,
                        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
                    )
                    kotlinx.coroutines.delay(300L)
                }
            }
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .graphicsLayer(translationY = translationY.value)
                    .background(PrimaryGreen, shape = CircleShape)
            )
        }
    }
}

@Composable
fun SuggestionGrid(onSuggestionClick: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(modifier = Modifier.weight(1f)) {
                SuggestionCard(
                    category = "QUR'AN TAFSIR",
                    text = "Tell me about Surah Al-Fatiha",
                    icon = Icons.AutoMirrored.Rounded.MenuBook,
                    iconBg = GoldLight,
                    iconTint = GoldAccent,
                    onClick = { onSuggestionClick("Tell me about Surah Al-Fatiha") }
                )
            }
            Box(modifier = Modifier.weight(1f)) {
                SuggestionCard(
                    category = "FIVE PILLARS",
                    text = "What are the 5 pillars of Islam?",
                    icon = Icons.Rounded.Star,
                    iconBg = LightGreenSoft,
                    iconTint = PrimaryGreen,
                    onClick = { onSuggestionClick("What are the 5 pillars of Islam?") }
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(modifier = Modifier.weight(1f)) {
                SuggestionCard(
                    category = "PURITY & WUDU",
                    text = "How to perform Wudu properly?",
                    icon = Icons.Rounded.Opacity,
                    iconBg = Color(0xFFE8F4FD),
                    iconTint = InfoBlue,
                    onClick = { onSuggestionClick("How to perform Wudu properly?") }
                )
            }
            Box(modifier = Modifier.weight(1f)) {
                SuggestionCard(
                    category = "HADITH & SUNNAH",
                    text = "Share a hadith about patience",
                    icon = Icons.Rounded.Favorite,
                    iconBg = OrangeLight,
                    iconTint = OrangeAccent,
                    onClick = { onSuggestionClick("Share a hadith about patience") }
                )
            }
        }
    }
}

@Composable
fun SuggestionCard(
    category: String,
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconBg: Color,
    iconTint: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .shadow(1.dp, RoundedCornerShape(18.dp)),
        shape = RoundedCornerShape(18.dp),
        color = CardWhite,
        border = BorderStroke(1.dp, BorderLight)
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .background(iconBg, shape = RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Text(
                    text = category,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = iconTint
                    )
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = DarkText,
                    fontWeight = FontWeight.SemiBold
                ),
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun ChatBubble(
    message: ChatMessage,
    onCopy: () -> Unit,
    onShare: () -> Unit,
    isSpeaking: Boolean,
    onSpeakToggle: () -> Unit
) {
    val isUser = message.isUser
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {
        if (!isUser) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        Brush.linearGradient(listOf(PrimaryGreen, PrimaryGreenDark)),
                        shape = RoundedCornerShape(10.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.SmartToy,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(
            horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
        ) {
            Surface(
                color = if (isUser) PrimaryGreen else CardWhite,
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomStart = if (isUser) 16.dp else 4.dp,
                    bottomEnd = if (isUser) 4.dp else 16.dp
                ),
                border = if (!isUser) BorderStroke(1.dp, BorderLight) else null,
                shadowElevation = if (isUser) 1.dp else 1.dp,
                modifier = Modifier.widthIn(max = 280.dp)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    if (isUser) {
                        Text(
                            text = message.text,
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium,
                            lineHeight = 21.sp
                        )
                    } else {
                        Text(
                            text = formatChatMessage(message.text),
                            color = DarkText,
                            style = MaterialTheme.typography.bodyMedium,
                            lineHeight = 22.sp
                        )
                    }
                }
            }
            
            if (!isUser) {
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 6.dp)
                ) {
                    IconButton(
                        onClick = onCopy,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.ContentCopy,
                            contentDescription = "Copy",
                            tint = LightGrayText,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    IconButton(
                        onClick = onSpeakToggle,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = if (isSpeaking) Icons.AutoMirrored.Rounded.VolumeOff else Icons.AutoMirrored.Rounded.VolumeUp,
                            contentDescription = "Read Aloud",
                            tint = if (isSpeaking) OrangeAccent else LightGrayText,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    IconButton(
                        onClick = onShare,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Share,
                            contentDescription = "Share",
                            tint = LightGrayText,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }

        if (isUser) {
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        Brush.linearGradient(listOf(OrangeAccent, GoldAccent)),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Person,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = Color.White
                )
            }
        }
    }
}

fun isArabicChar(c: Char): Boolean {
    return c in '\u0600'..'\u06FF' || 
           c in '\u0750'..'\u077F' || 
           c in '\u08A0'..'\u08FF' || 
           c in '\uFB50'..'\uFDFF' || 
           c in '\uFE70'..'\uFEFF'
}

fun formatChatMessage(text: String): AnnotatedString {
    val builder = AnnotatedString.Builder()
    var i = 0
    val length = text.length
    
    while (i < length) {
        if (i < length - 2 && text[i] == '*' && text[i + 1] == '*') {
            val endIdx = text.indexOf("**", i + 2)
            if (endIdx != -1) {
                builder.pushStyle(SpanStyle(fontWeight = FontWeight.Bold, color = PrimaryGreenDark))
                val boldText = text.substring(i + 2, endIdx)
                appendWithArabicHighlighting(builder, boldText)
                builder.pop()
                i = endIdx + 2
                continue
            }
        }
        if (text[i] == '*') {
            val endIdx = text.indexOf('*', i + 1)
            if (endIdx != -1) {
                builder.pushStyle(SpanStyle(fontWeight = FontWeight.Bold, color = PrimaryGreen))
                val italicText = text.substring(i + 1, endIdx)
                appendWithArabicHighlighting(builder, italicText)
                builder.pop()
                i = endIdx + 1
                continue
            }
        }
        
        if (isArabicChar(text[i])) {
            var endIdx = i
            while (endIdx < length && (isArabicChar(text[endIdx]) || text[endIdx] == ' ' || text[endIdx] == 'ؐ' || text[endIdx] == 'ؑ' || text[endIdx] == 'ؒ' || text[endIdx] == 'ؓ')) {
                endIdx++
            }
            while (endIdx > i && text[endIdx - 1] == ' ') {
                endIdx--
            }
            val arabicText = text.substring(i, endIdx)
            builder.pushStyle(SpanStyle(
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryGreenDark
            ))
            builder.append(arabicText)
            builder.pop()
            i = endIdx
        } else {
            builder.append(text[i])
            i++
        }
    }
    return builder.toAnnotatedString()
}

private fun appendWithArabicHighlighting(builder: AnnotatedString.Builder, text: String) {
    var i = 0
    val length = text.length
    while (i < length) {
        if (isArabicChar(text[i])) {
            var endIdx = i
            while (endIdx < length && (isArabicChar(text[endIdx]) || text[endIdx] == ' ' || text[endIdx] == 'ؐ' || text[endIdx] == 'ؑ' || text[endIdx] == 'ؒ' || text[endIdx] == 'ؓ')) {
                endIdx++
            }
            while (endIdx > i && text[endIdx - 1] == ' ') {
                endIdx--
            }
            val arabicText = text.substring(i, endIdx)
            builder.pushStyle(SpanStyle(
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryGreenDark
            ))
            builder.append(arabicText)
            builder.pop()
            i = endIdx
        } else {
            builder.append(text[i])
            i++
        }
    }
}
