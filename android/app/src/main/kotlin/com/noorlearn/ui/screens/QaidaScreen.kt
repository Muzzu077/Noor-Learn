package com.noorlearn.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.noorlearn.ui.theme.*

data class ArabicLetter(val letter: String, val transliteration: String, val example: String)

val QAIDA_LETTERS = listOf(
    ArabicLetter("ا", "Alif", "Allah (الله)"),
    ArabicLetter("ب", "Baa", "Bab (Door)"),
    ArabicLetter("ت", "Taa", "Tuffah (Apple)"),
    ArabicLetter("ث", "Thaa", "Thalj (Ice)"),
    ArabicLetter("ج", "Jeem", "Jamal (Camel)"),
    ArabicLetter("ح", "Haa", "Haleeb (Milk)"),
    ArabicLetter("خ", "Khaa", "Khubz (Bread)"),
    ArabicLetter("د", "Daal", "Deek (Rooster)"),
    ArabicLetter("ذ", "Dhaal", "Dhahab (Gold)"),
    ArabicLetter("ر", "Raa", "Rummaan (Pomegranate)"),
    ArabicLetter("ز", "Zaa", "Zahra (Flower)"),
    ArabicLetter("س", "Seen", "Samak (Fish)"),
    ArabicLetter("ش", "Sheen", "Shams (Sun)"),
    ArabicLetter("ص", "Sadd", "Sadeeq (Friend)"),
    ArabicLetter("ض", "Dadd", "Dofda' (Frog)"),
    ArabicLetter("ط", "Taa", "Taa'ira (Airplane)"),
    ArabicLetter("ظ", "Zhaa", "Zharf (Envelope)"),
    ArabicLetter("ع", "Ayn", "Asal (Honey)"),
    ArabicLetter("غ", "Ghayn", "Ghaima (Cloud)"),
    ArabicLetter("ف", "Faa", "Feel (Elephant)"),
    ArabicLetter("ق", "Qaaf", "Qamar (Moon)"),
    ArabicLetter("ك", "Kaaf", "Kitab (Book)"),
    ArabicLetter("ل", "Laam", "Laimoon (Lemon)"),
    ArabicLetter("م", "Meem", "Maa' (Water)"),
    ArabicLetter("ن", "Noon", "Noor (Light)"),
    ArabicLetter("ه", "Haa", "Hadiya (Gift)"),
    ArabicLetter("و", "Waw", "Warda (Rose)"),
    ArabicLetter("ي", "Yaa", "Yad (Hand)")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QaidaScreen(navController: NavController) {
    var selectedLetter by remember { mutableStateOf<ArabicLetter?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Qaida - Arabic Basics", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = PrimaryGreen)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BeigeBackground,
                    titleContentColor = DarkText
                )
            )
        },
        containerColor = BeigeBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Learn the Arabic Alphabet",
                style = MaterialTheme.typography.titleMedium.copy(color = GrayText),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                contentPadding = PaddingValues(bottom = 80.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(QAIDA_LETTERS) { letter ->
                    Card(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clickable { selectedLetter = letter },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedLetter == letter) PrimaryGreen else CardWhite
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = letter.letter,
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (selectedLetter == letter) Color.White else DarkText
                                    )
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = letter.transliteration,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = if (selectedLetter == letter) Color.White.copy(alpha = 0.8f) else GrayText
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        // Bottom Sheet for letter details
        if (selectedLetter != null) {
            ModalBottomSheet(
                onDismissRequest = { selectedLetter = null },
                containerColor = CardWhite,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .padding(bottom = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .background(LightGreen, shape = RoundedCornerShape(24.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = selectedLetter!!.letter,
                            style = MaterialTheme.typography.displayLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = PrimaryGreen
                            ),
                            textAlign = TextAlign.Center
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Text(
                        text = selectedLetter!!.transliteration,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = DarkText
                        )
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "Example: ${selectedLetter!!.example}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = GrayText
                        )
                    )
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    Button(
                        onClick = { selectedLetter = null },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                    ) {
                        Text("Got it!", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                    }
                }
            }
        }
    }
}
