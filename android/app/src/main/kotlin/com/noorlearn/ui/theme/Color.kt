package com.noorlearn.ui.theme

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// ═══════════════════════════════════════════════════════════════
// NoorLearn Design System — Modern Islamic Minimalism
// Theme: Deep Emerald + Warm Ivory + Soft Gold
// ═══════════════════════════════════════════════════════════════

// ─── PRIMARY PALETTE ───────────────────────────────────────────
val PrimaryGreen     = Color(0xFF0F6B5B)   // Deep Emerald Green
val PrimaryGreenDark = Color(0xFF09483D)   // Darker Emerald
val PrimaryGreenDeep = Color(0xFF062E27)   // Deepest shade
val LightGreen       = Color(0xFFE8F5F2)   // Emerald 50 — card tint
val LightGreenSoft   = Color(0xFFF0FAF7)   // Emerald 30 — subtle bg
val MintGreen        = Color(0xFFB8DDD6)   // Muted emerald for dividers

// ─── ACCENT / SECONDARY ────────────────────────────────────────
val OrangeAccent     = Color(0xFFC9A227)   // Soft Gold — primary accent
val OrangeLight      = Color(0xFFF9EFC7)   // Gold 50 — tag backgrounds
val GoldAccent       = Color(0xFFAA8919)   // Deep Gold — pressed state
val GoldLight        = Color(0xFFFDF6DC)   // Gold 30
val AccentTeal       = Color(0xFF2C8C7A)   // Muted Teal — secondary text

// ─── NEUTRAL BACKGROUNDS ───────────────────────────────────────
val BeigeBackground  = Color(0xFFF8F6F1)   // Warm Ivory — main background
val CardWhite        = Color(0xFFFFFFFF)   // Pure White — card surface
val SurfaceElevated  = Color(0xFFF2EFE9)   // Slightly darker ivory — elevated

// ─── TEXT ──────────────────────────────────────────────────────
val DarkText         = Color(0xFF1F2937)   // Dark Slate — primary text
val GrayText         = Color(0xFF6B7280)   // Mid gray — secondary text
val LightGrayText    = Color(0xFF9CA3AF)   // Light gray — disabled/hint

// ─── DIVIDERS & BORDERS ────────────────────────────────────────
val DividerLight     = Color(0xFFE5E7EB)   // Neutral 200
val BorderLight      = Color(0xFFD1D5DB)   // Neutral 300

// ─── SEMANTIC ──────────────────────────────────────────────────
val ErrorRed         = Color(0xFFDC2626)   // Red 600
val SuccessGreen     = Color(0xFF16A34A)   // Green 600
val InfoBlue         = Color(0xFF2563EB)   // Blue 600
val WarningAmber     = Color(0xFFD97706)   // Amber 600

// ─── JOURNEY TASK COLORS ───────────────────────────────────────
val TaskComplete     = Color(0xFF16A34A)   // Green — completed task
val TaskActive       = Color(0xFF0F6B5B)   // Emerald — current task
val TaskLocked       = Color(0xFFD1D5DB)   // Gray — locked task

// ═══════════════════════════════════════════════════════════════
// Subtle dot-grid background modifier for the "notebook" look
// ═══════════════════════════════════════════════════════════════
fun Modifier.gridBackground(
    gridSize: Dp = 28.dp,
    dotColor: Color = Color(0xFF0F6B5B).copy(alpha = 0.06f)
): Modifier = this.drawBehind {
    val sizePx = gridSize.toPx()
    val dotRadius = 1f

    var x = sizePx / 2
    while (x < size.width) {
        var y = sizePx / 2
        while (y < size.height) {
            drawCircle(
                color = dotColor,
                radius = dotRadius,
                center = Offset(x, y)
            )
            y += sizePx
        }
        x += sizePx
    }
}

// ─── Grid line variant (for screens that prefer lines) ─────────
fun Modifier.gridLineBackground(
    gridSize: Dp = 24.dp,
    gridColor: Color = Color(0xFF0F6B5B).copy(alpha = 0.05f)
): Modifier = this.drawBehind {
    val sizePx = gridSize.toPx()
    val width = size.width
    val height = size.height

    var x = 0f
    while (x < width) {
        drawLine(color = gridColor, start = Offset(x, 0f), end = Offset(x, height), strokeWidth = 0.8f)
        x += sizePx
    }
    var y = 0f
    while (y < height) {
        drawLine(color = gridColor, start = Offset(0f, y), end = Offset(width, y), strokeWidth = 0.8f)
        y += sizePx
    }
}
