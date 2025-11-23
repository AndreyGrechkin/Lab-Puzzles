package com.defey.labpuzzles.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.defey.labpuzzles.resources.Res
import com.defey.labpuzzles.resources.jost_bold
import com.defey.labpuzzles.resources.jost_medium
import com.defey.labpuzzles.resources.jost_semibold
import com.defey.labpuzzles.resources.montserrat_alternates_medium
import com.defey.labpuzzles.resources.montserrat_alternates_semibold
import com.defey.labpuzzles.resources.raleway_medium
import com.defey.labpuzzles.resources.raleway_regular
import org.jetbrains.compose.resources.Font

@Composable
fun AppTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) darkColorPalette() else lightColorPalette()

    val jost = FontFamily(
        Font(resource = Res.font.jost_medium, weight = FontWeight.Medium),
        Font(resource = Res.font.jost_semibold, weight = FontWeight.SemiBold),
        Font(resource = Res.font.jost_bold, weight = FontWeight.Bold)
    )

    val raleway = FontFamily(
        Font(resource = Res.font.raleway_regular, weight = FontWeight.Normal),
        Font(resource = Res.font.raleway_medium, weight = FontWeight.Medium)
    )

    val montserratAlternates = FontFamily(
        Font(resource = Res.font.montserrat_alternates_medium, weight = FontWeight.Medium),
        Font(resource = Res.font.montserrat_alternates_semibold, weight = FontWeight.SemiBold)
    )

    val appTypography = Typography(
        headlineLarge = TextStyle(
            fontFamily = jost,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            lineHeight = 40.0.sp,
            letterSpacing = 0.sp
        ),
        headlineMedium = TextStyle(
            fontFamily = jost,
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
            lineHeight = 36.sp,
            letterSpacing = 0.sp
        ),
        titleLarge = TextStyle(
            fontFamily = jost,
            fontWeight = FontWeight.Medium,
            fontSize = 20.sp
        ),
        titleMedium = TextStyle(
            fontFamily = montserratAlternates,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.15.sp
        ),
        bodyLarge = TextStyle(
            fontFamily = raleway,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = raleway,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.25.sp
        ),
        labelLarge = TextStyle(
            fontFamily = jost,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        ),
        labelMedium = TextStyle(
            fontFamily = montserratAlternates,
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp
        )
    )

    MaterialTheme(
        colorScheme = colors,
        typography = appTypography,
        shapes = AppShapes,
        content = content
    )
}