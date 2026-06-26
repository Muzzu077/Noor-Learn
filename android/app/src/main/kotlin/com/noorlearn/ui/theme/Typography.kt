package com.noorlearn.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.noorlearn.R

// Google Font Provider
val fontProvider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val LibreCaslonTextFont = GoogleFont("Libre Caslon Text")
val PlusJakartaSansFont = GoogleFont("Plus Jakarta Sans")
val NotoNaskhArabicFont = GoogleFont("Noto Naskh Arabic")
val NotoNastaliqUrduFont = GoogleFont("Noto Nastaliq Urdu")

val LibreCaslonTextFontFamily = FontFamily(
    Font(googleFont = LibreCaslonTextFont, fontProvider = fontProvider, weight = FontWeight.Normal),
    Font(googleFont = LibreCaslonTextFont, fontProvider = fontProvider, weight = FontWeight.Medium),
    Font(googleFont = LibreCaslonTextFont, fontProvider = fontProvider, weight = FontWeight.SemiBold),
    Font(googleFont = LibreCaslonTextFont, fontProvider = fontProvider, weight = FontWeight.Bold)
)

val PlusJakartaSansFontFamily = FontFamily(
    Font(googleFont = PlusJakartaSansFont, fontProvider = fontProvider, weight = FontWeight.Normal),
    Font(googleFont = PlusJakartaSansFont, fontProvider = fontProvider, weight = FontWeight.Medium),
    Font(googleFont = PlusJakartaSansFont, fontProvider = fontProvider, weight = FontWeight.SemiBold),
    Font(googleFont = PlusJakartaSansFont, fontProvider = fontProvider, weight = FontWeight.Bold)
)

val ArabicFontFamily = FontFamily(
    Font(googleFont = NotoNaskhArabicFont, fontProvider = fontProvider, weight = FontWeight.Normal),
    Font(googleFont = NotoNaskhArabicFont, fontProvider = fontProvider, weight = FontWeight.Bold)
)

val UrduFontFamily = FontFamily(
    Font(googleFont = NotoNastaliqUrduFont, fontProvider = fontProvider, weight = FontWeight.Normal),
    Font(googleFont = NotoNastaliqUrduFont, fontProvider = fontProvider, weight = FontWeight.Bold)
)

// Legacy alias to prevent compiler errors if any other file references InterFontFamily
val InterFontFamily = PlusJakartaSansFontFamily

// Material 3 Typography Overhaul implementing Earthy Wisdom style
val NoorLearnTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = LibreCaslonTextFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 57.sp,
        lineHeight = 64.sp
    ),
    displayMedium = TextStyle(
        fontFamily = LibreCaslonTextFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 45.sp,
        lineHeight = 52.sp
    ),
    displaySmall = TextStyle(
        fontFamily = LibreCaslonTextFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
        lineHeight = 44.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = LibreCaslonTextFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp,
        lineHeight = 40.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = LibreCaslonTextFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 36.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = LibreCaslonTextFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp
    ),
    titleLarge = TextStyle(
        fontFamily = LibreCaslonTextFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp
    ),
    titleMedium = TextStyle(
        fontFamily = PlusJakartaSansFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    titleSmall = TextStyle(
        fontFamily = PlusJakartaSansFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = PlusJakartaSansFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = PlusJakartaSansFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    bodySmall = TextStyle(
        fontFamily = PlusJakartaSansFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp
    ),
    labelLarge = TextStyle(
        fontFamily = PlusJakartaSansFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    labelMedium = TextStyle(
        fontFamily = PlusJakartaSansFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp
    ),
    labelSmall = TextStyle(
        fontFamily = PlusJakartaSansFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp
    )
)
