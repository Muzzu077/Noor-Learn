package com.noorlearn

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.noorlearn.ui.navigation.AppNavigation
import com.noorlearn.ui.theme.*
import dagger.hilt.android.AndroidEntryPoint

// ═══════════════════════════════════════════════════════════════
// NoorLearn Color Scheme — Modern Islamic Minimalism
// Primary: Deep Emerald Green | Secondary: Soft Gold
// Background: Warm Ivory | Cards: Pure White
// ═══════════════════════════════════════════════════════════════
private val NoorLearnColorScheme = lightColorScheme(
    primary = PrimaryGreen,
    onPrimary = Color.White,
    primaryContainer = LightGreen,
    onPrimaryContainer = PrimaryGreenDark,
    secondary = OrangeAccent,
    secondaryContainer = OrangeLight,
    tertiary = GoldAccent,
    tertiaryContainer = GoldLight,
    background = BeigeBackground,
    surface = CardWhite,
    surfaceVariant = SurfaceElevated,
    onBackground = DarkText,
    onSurface = DarkText,
    onSurfaceVariant = GrayText,
    outline = BorderLight,
    outlineVariant = DividerLight,
    error = ErrorRed
)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Install splash screen BEFORE super.onCreate()
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        // Keep splash visible until theme finishes loading
        splashScreen.setKeepOnScreenCondition { false }

        enableEdgeToEdge()
        setContent {
            MaterialTheme(
                colorScheme = NoorLearnColorScheme,
                typography = NoorLearnTypography
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = BeigeBackground
                ) {
                    AppNavigation()
                }
            }
        }
    }
}
