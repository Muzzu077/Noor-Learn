package com.noorlearn.ui.screens

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.noorlearn.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val userName by viewModel.userName.collectAsState()
    val surahsRead by viewModel.surahsRead.collectAsState()
    val bookmarksCount by viewModel.bookmarksCount.collectAsState()
    val studyMinutes by viewModel.studyMinutes.collectAsState()
    val streakCount by viewModel.streakCount.collectAsState()
    val isSignedOut by viewModel.isSignedOut.collectAsState()

    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(isSignedOut) {
        if (isSignedOut) {
            navController.navigate("auth") {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BeigeBackground).gridBackground()
            .verticalScroll(rememberScrollState())
    ) {
        // Header with avatar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(PrimaryGreen, PrimaryGreenDeep)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 24.dp)
            ) {
                // Avatar circle
                Box(
                    modifier = Modifier
                        .size(88.dp)
                        .shadow(8.dp, CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(GoldAccent, OrangeAccent)
                            ),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = userName.take(1).uppercase(),
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 36.sp
                        )
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    userName,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White.copy(alpha = 0.15f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.LocalFireDepartment,
                            contentDescription = null,
                            tint = OrangeAccent,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            if (streakCount > 0) "$streakCount day streak" else "Start your streak!",
                            style = MaterialTheme.typography.labelLarge.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }
            }
        }

        // Stats row — overlapping the header
        Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-24).dp)
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    value = surahsRead.toString(),
                    label = "Surahs Read",
                    icon = Icons.AutoMirrored.Filled.MenuBook,
                    accentColor = PrimaryGreen,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    value = bookmarksCount.toString(),
                    label = "Bookmarks",
                    icon = Icons.Filled.BookmarkBorder,
                    accentColor = OrangeAccent,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    value = if (studyMinutes >= 60) "${studyMinutes / 60}h" else "${studyMinutes}m",
                    label = "Study Time",
                    icon = Icons.Filled.AccessTime,
                    accentColor = InfoBlue,
                    modifier = Modifier.weight(1f)
                )
            }

        // Menu items
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CardWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column {
                        SettingsItem(
                            icon = Icons.Outlined.Settings,
                            title = "Preferences",
                            subtitle = "Learning level, goals",
                            onClick = { navController.navigate("onboarding") }
                        )
                        HorizontalDivider(color = DividerLight, modifier = Modifier.padding(horizontal = 20.dp))
                        SettingsItem(
                            icon = Icons.Filled.BookmarkBorder,
                            title = "My Bookmarks",
                            subtitle = "$bookmarksCount saved ayahs",
                            onClick = { navController.navigate("bookmarks") }
                        )
                        HorizontalDivider(color = DividerLight, modifier = Modifier.padding(horizontal = 20.dp))
                        SettingsItem(
                            icon = Icons.Filled.EmojiEvents,
                            title = "Achievements",
                            subtitle = "Track your milestones",
                            onClick = { navController.navigate("surah_list") }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Sign out & Account Deletion
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CardWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column {
                        SettingsItem(
                            icon = Icons.AutoMirrored.Outlined.ExitToApp,
                            title = "Sign Out",
                            titleColor = ErrorRed,
                            iconColor = ErrorRed,
                            onClick = { viewModel.signOut() }
                        )
                        HorizontalDivider(color = DividerLight, modifier = Modifier.padding(horizontal = 20.dp))
                        SettingsItem(
                            icon = Icons.Outlined.Settings,
                            title = "Delete Account",
                            subtitle = "Permanently remove your data",
                            titleColor = ErrorRed,
                            iconColor = ErrorRed,
                            onClick = { showDeleteDialog = true }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Delete Account") },
                text = { Text("Are you sure you want to delete your account? This action is permanent. All your recitation logs, bookmarks, and streaks will be deleted forever.") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDeleteDialog = false
                            viewModel.deleteAccount()
                        },
                        colors = ButtonDefaults.textButtonColors(contentColor = ErrorRed)
                    ) {
                        Text("Delete")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun StatCard(
    value: String,
    label: String,
    icon: ImageVector,
    accentColor: Color = PrimaryGreen,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(accentColor.copy(alpha = 0.1f), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                value,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                label,
                style = MaterialTheme.typography.labelSmall.copy(color = GrayText),
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    titleColor: Color = DarkText,
    iconColor: Color = PrimaryGreen,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(iconColor.copy(alpha = 0.08f), shape = RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = title, tint = iconColor, modifier = Modifier.size(22.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = titleColor,
                    fontWeight = FontWeight.Medium
                )
            )
            if (subtitle != null) {
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(color = LightGrayText)
                )
            }
        }
        Icon(
            Icons.Filled.ChevronRight,
            contentDescription = null,
            tint = DividerLight,
            modifier = Modifier.size(20.dp)
        )
    }
}
