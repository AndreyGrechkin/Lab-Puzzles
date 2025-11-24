package com.defey.labpuzzles.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.defey.labpuzzles.resources.Res
import com.defey.labpuzzles.resources.bg_dialog
import org.jetbrains.compose.resources.painterResource

/**
 * Кастомный диалог для использования во всех модулях приложения.
 *
 * @param controller Контроллер управления состоянием диалога
 * @param modifier Модификатор для контейнера содержимого диалога
 * @param backgroundColor Цфет фона содержимого диалога
 * @param overlayColor Цвет оверлея (фона за диалогом)
 * @param dismissOnBackPress Закрывать ли диалог по кнопке назад
 * @param dismissOnClickOutside Закрывать ли диалог при клике вне его
 * @param properties Свойства диалога (зависят от платформы)
 */
@Composable
fun CustomDialog(
    controller: DialogController,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Magenta,
    overlayColor: Color = Color.Black.copy(alpha = 0.5f),
    dismissOnBackPress: Boolean = true,
    dismissOnClickOutside: Boolean = true,
    properties: DialogProperties = DialogProperties(),
    contentAlignment: Alignment = Alignment.Center
) {
    if (controller.isVisible) {
        PlatformDialog(
            onDismissRequest = {
                if (dismissOnClickOutside) controller.hideDialog()
            },
            properties = DialogProperties(
                dismissOnBackPress = dismissOnBackPress,
                dismissOnClickOutside = properties.dismissOnClickOutside,
                usePlatformDefaultWidth = false
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        enabled = dismissOnClickOutside,
                        onClick = { controller.hideDialog() }
                    )
                    .background(overlayColor),
                contentAlignment = contentAlignment
            ) {
                Box(
                    modifier = modifier
                        .sizeIn(maxWidth = 400.dp, maxHeight = 600.dp)
                        .clickable(enabled = false, onClick = {})
                ) {
                    Image(
                        painter = painterResource(Res.drawable.bg_dialog),
                        contentDescription = null,
                        modifier = Modifier.matchParentSize(),
                        contentScale = ContentScale.FillBounds
                    )
                    controller.content?.invoke()
                }
            }
        }
    }
}

@Composable
expect fun PlatformDialog(
    onDismissRequest: () -> Unit,
    properties: DialogProperties,
    content: @Composable () -> Unit
)