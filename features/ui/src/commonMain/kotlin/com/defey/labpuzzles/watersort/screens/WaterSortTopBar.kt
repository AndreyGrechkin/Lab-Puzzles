package com.defey.labpuzzles.watersort.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.defey.labpuzzles.gameHub.screens.CurrencyPanel
import com.defey.labpuzzles.models.CurrencyType
import com.defey.labpuzzles.models.PlayerCurrencies
import com.defey.labpuzzles.resources.Res
import com.defey.labpuzzles.resources.bg_arrow_back
import org.jetbrains.compose.resources.painterResource

@Composable
fun WaterSortTopBar(
    currencies: PlayerCurrencies,
    levelId: String,
    onBackClick: () -> Unit,
    onAddCurrencyClick: (CurrencyType) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        CurrencyPanel(currencies = currencies, onAddCurrencyClick = onAddCurrencyClick)
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(Res.drawable.bg_arrow_back),
                contentDescription = "Back",
                modifier = Modifier
                    .size(48.dp)
                    .clickable(onClick = onBackClick)
                    .padding(end = 8.dp)
            )

            Text(
                text = "Уровень $levelId",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth().padding(end = 48.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}