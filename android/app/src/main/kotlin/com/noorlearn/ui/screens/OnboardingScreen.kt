package com.noorlearn.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.noorlearn.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    navController: NavController,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    var currentStep by remember { mutableStateOf(0) }
    
    // Selections
    var selectedLevel by remember { mutableStateOf("") }
    var selectedGoal by remember { mutableStateOf("") }
    var selectedCommitment by remember { mutableStateOf("") }
    
    val isLoading by viewModel.isLoading.collectAsState()

    val levelOptions = listOf("Beginner", "Intermediate", "Advanced")
    val goalOptions = listOf("Learning Arabic Alphabet", "Reading Qur'an", "Understanding Hadith", "Daily Duas")
    val commitmentOptions = listOf("5 mins", "15 mins", "30 mins")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                navigationIcon = {
                    if (currentStep > 0) {
                        IconButton(onClick = { currentStep-- }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = PrimaryGreen)
                        }
                    } else {
                        Spacer(modifier = Modifier.width(48.dp))
                    }
                },
                actions = {
                    TextButton(onClick = { 
                        navController.navigate("dashboard") { 
                            popUpTo("onboarding") { inclusive = true } 
                        } 
                    }) {
                        Text("Skip", color = GrayText)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
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
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Step Progress
            LinearProgressIndicator(
                progress = { (currentStep + 1) / 3f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = PrimaryGreen,
                trackColor = PrimaryGreen.copy(alpha = 0.2f),
                gapSize = 0.dp,
                drawStopIndicator = {}
            )
            
            Spacer(modifier = Modifier.height(32.dp))

            AnimatedContent(
                targetState = currentStep,
                transitionSpec = {
                    slideInHorizontally(initialOffsetX = { it }) + fadeIn() togetherWith
                    slideOutHorizontally(targetOffsetX = { -it }) + fadeOut()
                },
                label = "onboarding_steps"
            ) { step ->
                when (step) {
                    0 -> OnboardingStepUI(
                        title = "What is your current level?",
                        description = "This helps us personalize your initial experience.",
                        options = levelOptions,
                        selectedOption = selectedLevel,
                        onSelect = { selectedLevel = it }
                    )
                    1 -> OnboardingStepUI(
                        title = "What is your primary goal?",
                        description = "We will organize your dashboard based on what you want to achieve.",
                        options = goalOptions,
                        selectedOption = selectedGoal,
                        onSelect = { selectedGoal = it }
                    )
                    2 -> OnboardingStepUI(
                        title = "What is your daily commitment?",
                        description = "Consistency is key. We'll help you build a habit.",
                        options = commitmentOptions,
                        selectedOption = selectedCommitment,
                        onSelect = { selectedCommitment = it }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (currentStep < 2) {
                        currentStep++
                    } else {
                        viewModel.completeOnboarding(
                            level = selectedLevel.ifEmpty { "Beginner" },
                            goal = selectedGoal.ifEmpty { "Learning Arabic Alphabet" },
                            commitment = selectedCommitment.ifEmpty { "5 mins" }
                        ) {
                            navController.navigate("dashboard") {
                                popUpTo("onboarding") { inclusive = true }
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(bottom = 16.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                enabled = !isLoading && (
                    (currentStep == 0 && selectedLevel.isNotEmpty()) ||
                    (currentStep == 1 && selectedGoal.isNotEmpty()) ||
                    (currentStep == 2 && selectedCommitment.isNotEmpty())
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (currentStep == 2) "Complete" else "Continue",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            if (currentStep == 2) Icons.Default.Check else Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OnboardingStepUI(
    title: String,
    description: String,
    options: List<String>,
    selectedOption: String,
    onSelect: (String) -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold, color = DarkText)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge.copy(color = GrayText)
        )
        Spacer(modifier = Modifier.height(32.dp))

        options.forEach { option ->
            val isSelected = option == selectedOption
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable { onSelect(option) },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) PrimaryGreen else CardWhite
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 8.dp else 2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = option,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color.White else DarkText
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    if (isSelected) {
                        Icon(Icons.Default.Check, contentDescription = "Selected", tint = Color.White)
                    }
                }
            }
        }
    }
}
