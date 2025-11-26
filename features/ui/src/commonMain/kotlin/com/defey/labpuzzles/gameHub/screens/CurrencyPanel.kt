package com.defey.labpuzzles.gameHub.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.defey.labpuzzles.gameHub.GameHubUiContract
import com.defey.labpuzzles.models.CurrencyType
import com.defey.labpuzzles.resources.Res
import com.defey.labpuzzles.resources.icon_energy
import com.defey.labpuzzles.resources.icon_experience
import com.defey.labpuzzles.resources.icon_plus
import com.defey.labpuzzles.resources.icon_premium_crystals
import com.defey.labpuzzles.resources.icon_science_points
import org.jetbrains.compose.resources.painterResource

@Composable
fun CurrencyPanel(
    currencies: GameHubUiContract.PlayerCurrencies,
    onAddCurrencyClick: (CurrencyType) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Карточка опыта (без кнопки +)
        CurrencyCard(
            icon = painterResource(Res.drawable.icon_experience),
            value = formatNumber(currencies.experience),
            showPlusButton = false,
            onAddClick = {},
        )

        // Карточка научных очков
        CurrencyCard(
            icon = painterResource(Res.drawable.icon_science_points),
            value = formatNumber(currencies.sciencePoints),
            showPlusButton = true,
            onAddClick = { onAddCurrencyClick(CurrencyType.SCIENCE_POINTS) },
        )

        // Карточка кристаллов
        CurrencyCard(
            icon = painterResource(Res.drawable.icon_premium_crystals),
            value = formatNumber(currencies.premiumCrystals),
            showPlusButton = true,
            onAddClick = { onAddCurrencyClick(CurrencyType.PREMIUM_CRYSTALS) },
        )

        // Карточка энергии
        CurrencyCard(
            icon = painterResource(Res.drawable.icon_energy),
            value = "${currencies.energy}/${currencies.maxEnergy}",
            showPlusButton = true,
            onAddClick = { onAddCurrencyClick(CurrencyType.ENERGY) },
        )
    }
}

@Composable
private fun CurrencyCard(
    icon: Painter,
    value: String,
    showPlusButton: Boolean,
    onAddClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .background(
                color = Color(0x802196F3),
                shape = RoundedCornerShape(20.dp)
            )
            .clickable(
                enabled = showPlusButton,
                onClick = onAddClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Image(
                painter = icon,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )

            Text(
                text = value,
                color = Color.White,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium
            )

            if (showPlusButton) {
                Image(
                    painter = painterResource(Res.drawable.icon_plus),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

private fun formatNumber(number: Int): String {
    return when {
        number >= 1000000 -> "${number / 1000000}M"
        number >= 10000 -> "${number / 1000}K" // 15000 -> 15K
        number >= 1000 -> "${number / 1000}.${(number % 1000) / 100}K" // 1500 -> 1.5K
        else -> number.toString()
    }
}