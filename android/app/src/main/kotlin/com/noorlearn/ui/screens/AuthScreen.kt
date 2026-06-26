package com.noorlearn.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.noorlearn.ui.theme.*
import kotlinx.coroutines.launch
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.CustomCredential
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var isLogin by remember { mutableStateOf(true) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }

    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val user by viewModel.user.collectAsState()

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }

    LaunchedEffect(user) {
        if (user != null) {
            navController.navigate("dashboard") {
                popUpTo("auth") { inclusive = true }
            }
        }
    }

    Scaffold(
        containerColor = BeigeBackground
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(BeigeBackground)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(36.dp))
                // Top Header Section
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(tween(600)) + slideInVertically(initialOffsetY = { -40 }, animationSpec = tween(600))
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Premium Minimal Logo
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .shadow(8.dp, RoundedCornerShape(24.dp), ambientColor = PrimaryGreen.copy(alpha = 0.2f), spotColor = PrimaryGreen.copy(alpha = 0.2f))
                                .background(CardWhite, shape = RoundedCornerShape(24.dp))
                                .border(1.dp, PrimaryGreen.copy(alpha = 0.08f), RoundedCornerShape(24.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "✨",
                                fontSize = 42.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            "NoorLearn",
                            style = MaterialTheme.typography.displaySmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = DarkText,
                                fontFamily = FontFamily.Serif
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            "Your personal path to Islamic wisdom",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = GrayText,
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                // Input & Button Section (Form)
                AnimatedVisibility(
                    visible = visible,
                    enter = slideInVertically(initialOffsetY = { 200 }, animationSpec = tween(700, delayMillis = 200)) + fadeIn(tween(700, delayMillis = 200))
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Error handling panel
                        if (error != null) {
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = ErrorRed.copy(alpha = 0.08f),
                                border = borderStroke(0.5.dp, ErrorRed.copy(alpha = 0.15f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = error!!,
                                    color = ErrorRed,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(14.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        // Form Fields
                        if (!isLogin) {
                            OutlinedTextField(
                                value = name,
                                onValueChange = { name = it },
                                label = { Text("Full Name") },
                                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = PrimaryGreen) },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = PrimaryGreen,
                                    unfocusedBorderColor = DividerLight,
                                    focusedLabelColor = PrimaryGreen,
                                    unfocusedContainerColor = CardWhite,
                                    focusedContainerColor = CardWhite
                                ),
                                shape = RoundedCornerShape(16.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email Address") },
                            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = PrimaryGreen) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryGreen,
                                unfocusedBorderColor = DividerLight,
                                focusedLabelColor = PrimaryGreen,
                                unfocusedContainerColor = CardWhite,
                                focusedContainerColor = CardWhite
                            ),
                            shape = RoundedCornerShape(16.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Password") },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = PrimaryGreen) },
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryGreen,
                                unfocusedBorderColor = DividerLight,
                                focusedLabelColor = PrimaryGreen,
                                unfocusedContainerColor = CardWhite,
                                focusedContainerColor = CardWhite
                            ),
                            shape = RoundedCornerShape(16.dp)
                        )

                        if (isLogin) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Forgot Password?",
                                color = PrimaryGreen,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier
                                    .align(Alignment.End)
                                    .clickable {
                                        if (email.isBlank()) {
                                            Toast.makeText(context, "Please enter your email address first.", Toast.LENGTH_SHORT).show()
                                        } else {
                                            viewModel.sendPasswordResetEmail(email) {
                                                Toast.makeText(context, "Password reset email sent successfully!", Toast.LENGTH_LONG).show()
                                            }
                                        }
                                    }
                            )
                        }

                        Spacer(modifier = Modifier.height(28.dp))

                        // Submit Button
                        Button(
                            onClick = {
                                if (isLogin) {
                                    viewModel.signIn(email, password) { }
                                } else {
                                    viewModel.signUp(email, password, name) {
                                        navController.navigate("onboarding") {
                                            popUpTo("auth") { inclusive = true }
                                        }
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .shadow(4.dp, RoundedCornerShape(16.dp), clip = false),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                            enabled = !isLoading
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    modifier = Modifier.size(24.dp),
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text(
                                    text = if (isLogin) "Sign In" else "Create Account",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Google Sign-In Button
                        OutlinedButton(
                            onClick = {
                                triggerGoogleSignIn(context, coroutineScope, viewModel)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.outlinedButtonColors(containerColor = CardWhite, contentColor = DarkText),
                            border = borderStroke(1.dp, DividerLight)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                // Draw a simple Google G logo using custom text with colors
                                Text(
                                    text = "G ",
                                    color = InfoBlue,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Sign in with Google",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Medium, color = DarkText)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Switch mode button
                        TextButton(
                            onClick = { isLogin = !isLogin }
                        ) {
                            Text(
                                text = if (isLogin) "Don't have an account? Sign up" else "Already have an account? Sign in",
                                color = PrimaryGreen,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(36.dp))
            }
        }
    }
}

// Utility to create border stroke
private fun borderStroke(width: androidx.compose.ui.unit.Dp, color: Color) = androidx.compose.foundation.BorderStroke(width, color)

// Triggers Credential Manager sign in
private fun triggerGoogleSignIn(
    context: android.content.Context,
    coroutineScope: kotlinx.coroutines.CoroutineScope,
    viewModel: AuthViewModel
) {
    val credentialManager = CredentialManager.create(context)
    // Server Client ID from strings or config, using a simulated fallback for emulators
    val serverClientId = "69017110219-if1ddo7n6phr1a2pe0hh3f98tm12tg45.apps.googleusercontent.com"
    
    val googleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setServerClientId(serverClientId)
        .setAutoSelectEnabled(true)
        .build()

    val request = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    coroutineScope.launch {
        try {
            val result = credentialManager.getCredential(context, request)
            val credential = result.credential
            
            if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val idToken = googleIdTokenCredential.idToken
                viewModel.signInWithGoogle(idToken) {
                    Toast.makeText(context, "Welcome to NoorLearn!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Google Sign-In failed: Unsupported credential type", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            android.util.Log.w("AuthScreen", "Google Credential Manager failed, testing bypass if in DEBUG mode", e)
            if (com.noorlearn.BuildConfig.DEBUG) {
                // Bypass for emulator testing without Google Services configured
                viewModel.signInWithGoogle("mock-google-id-token-555") {
                    Toast.makeText(context, "Debug: Google Sign-in simulated successfully!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Google Sign-In failed: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
