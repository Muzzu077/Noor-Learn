package com.noorlearn.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.noorlearn.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarksScreen(
    navController: NavController,
    viewModel: BookmarksViewModel = hiltViewModel()
) {
    val bookmarkedAyahs by viewModel.bookmarkedAyahs.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Bookmarks", fontWeight = FontWeight.Bold, color = PrimaryGreen) },
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
        when {
            isLoading -> {
                Box(modifier = Modifier.fillMaxSize().background(Color.White).gridBackground().padding(padding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = PrimaryGreen)
                }
            }
            bookmarkedAyahs.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize().background(Color.White).gridBackground().padding(padding), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Filled.Favorite,
                            contentDescription = null,
                            tint = GrayText.copy(alpha = 0.3f),
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "No bookmarks yet",
                            style = MaterialTheme.typography.titleMedium,
                            color = GrayText
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Tap the heart icon on any ayah\nto save it here",
                            style = MaterialTheme.typography.bodyMedium,
                            color = GrayText.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().background(Color.White).gridBackground().padding(padding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(bookmarkedAyahs, key = { it.id }) { ayah ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
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
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = LightGreen.copy(alpha = 0.5f)
                                    ) {
                                        Text(
                                            text = "Surah ${ayah.surahId} : Ayah ${ayah.ayahNumber}",
                                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                            color = PrimaryGreen,
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                                        )
                                    }
                                    IconButton(
                                        onClick = { viewModel.removeBookmark(ayah.id) },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            Icons.Filled.Favorite,
                                            contentDescription = "Remove",
                                            tint = ErrorRed,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = ayah.arabicText,
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontFamily = ArabicFontFamily,
                                        lineHeight = 40.sp
                                    ),
                                    color = DarkText,
                                    textAlign = TextAlign.Right,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                HorizontalDivider(color = DividerLight.copy(alpha = 0.3f))
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = ayah.translationEn,
                                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                                    color = GrayText
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
