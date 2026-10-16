package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// 1. Rendesk Pastel Gradient Theme Color Palette
val RendeskPrimaryBg = Color(0xFFFAF8F8)          // Soft off-white
val RendeskSecondaryBg = Color(0xFFE7E8EF)        // Light cool gray
val RendeskPastelPink = Color(0xFFF5C9D2)         // Primary accent: Pastel pink
val RendeskSoftCoral = Color(0xFFFF777F)          // Secondary accent: Soft coral
val RendeskBlushPink = Color(0xFFF4BFCB)          // Gradient color: Blush pink
val RendeskLavender = Color(0xFFC9B9F2)           // Lavender accent
val RendeskLightPurpleTint = Color(0xFFEEE7FA)     // Light purple tint
val RendeskPrimaryText = Color(0xFF151515)        // Primary text: Near-black
val RendeskSecondaryText = Color(0xFF77747A)      // Secondary text: Muted gray
val RendeskCardWhite = Color(0xFFFFFFFF)          // Card background: White
val RendeskBorderSoftGray = Color(0xFFE8E5E8)     // Borders and dividers: Soft gray
val RendeskButtonBlack = Color(0xFF000000)        // Primary button: Black
val RendeskButtonBlackPressed = Color(0xFF1E1E1E)

// Semantic Financial Accents (Aligned with pastel coral & lavender)
val FinanceProfitAccent = Color(0xFFFF777F)        // Soft coral / rose profit accent
val FinanceProfitBg = Color(0xFFFFF0F2)
val FinanceLossAccent = Color(0xFFE11D48)          // Deeper rose for loss
val FinanceLossBg = Color(0xFFFFECEF)
val FinanceBreakEvenAccent = Color(0xFF77747A)
val FinanceBreakEvenBg = Color(0xFFF3F2F5)

// Cash & UPI Pill Colors
val VendigoCashAmber = Color(0xFFFF777F)          // Coral accent for Cash
val VendigoCashContainer = Color(0xFFFFF0F2)
val VendigoCashOnContainer = Color(0xFF151515)

val VendigoBluePrimary = Color(0xFF8B72D4)        // Lavender / Royal purple for UPI
val VendigoBlueContainer = Color(0xFFEEE7FA)
val VendigoBlueOnContainer = Color(0xFF151515)

// Backward compatible aliases
val RendeskBlack = RendeskButtonBlack
val FinanceProfitGreen = RendeskSoftCoral
val FinanceLossRed = Color(0xFFE11D48)

val VendigoGreenPrimary = RendeskButtonBlack
val VendigoGreenLight = RendeskSoftCoral
val VendigoGreenContainer = RendeskLightPurpleTint
val VendigoGreenOnContainer = RendeskPrimaryText

val VendigoBgLight = RendeskPrimaryBg
val VendigoSurfaceLight = RendeskCardWhite
val VendigoSurfaceVariant = RendeskSecondaryBg
val VendigoOnSurface = RendeskPrimaryText
val VendigoOnSurfaceVariant = RendeskSecondaryText
val VendigoOutline = RendeskBorderSoftGray

// 2. Signature Gradient Style
// linear-gradient(135deg, #F8DDE2 0%, #F5C9D2 35%, #E8D8F5 70%, #FF777F 100%)
val SignaturePastelGradient = Brush.linearGradient(
    colors = listOf(
        Color(0xFFF8DDE2),
        Color(0xFFF5C9D2),
        Color(0xFFE8D8F5),
        Color(0xFFFF777F)
    )
)

// Subtle ambient background gradient
val AmbientBackgroundGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0xFFFAF8F8),
        Color(0xFFF8DDE2).copy(alpha = 0.35f),
        Color(0xFFEEE7FA).copy(alpha = 0.45f),
        Color(0xFFFAF8F8)
    )
)

// Subtle hero highlight card gradient
val HeroCardGradient = Brush.horizontalGradient(
    colors = listOf(
        Color(0xFFFBF4F6),
        Color(0xFFF8EEFA),
        Color(0xFFFFF4F5)
    )
)
