package com.defey.labpuzzles.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val LightCardGameColors = lightColorScheme(
    primary = Color(0xFF6200EE),
    secondary = Color(0xFF03DAC6),
    background = Color(0xFFF5F5F5),
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black
)

val DarkCardGameColors = darkColorScheme(
    primary = Color(0xFFBB86FC),
    secondary = Color(0xFF03DAC6),
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White
)

fun lightColorPalette() = LightCardGameColors
fun darkColorPalette() = DarkCardGameColors

object LabColors {
    val cardBack = Color(0xFF2A52BE) // классический синий цвет рубашки
    val cardFront = Color.White
    val black = Color.Black
    val heart = Color(0xFFD32F2F)
    val diamond = Color(0xFFD32F2F)
    val club = Color(0xFF000000)
    val spade = Color(0xFF000000)
    val selected = Color(0xFFFF8000)
    val selectedDialog = Color.Green
    val containerButtonColor = Color(0xFFA5D6A7)
    val contentButtonColor = Color(0xFF2E7D32)
    val defaultBackground = Color(0xFF2E7D32)
    val lightBlue = Color(0xFF0052BD)
    val lightYellow = Color(0xFFDFF029)
    val lightRed = Color(0xFFFF5B5B)
    val lightBlueGreen = Color(0xFF5BEFFF)
    val lightBrown = Color(0xFF987D70)
    val dialogColor = Color(0xFFE8F5E9)
    val defaultEmptyCardSlot = Color(0x55212121)
}