package com.defey.labpuzzles.dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Контроллер для управления состоянием диалога.
 * Отслеживает видимость и содержимое диалога.
 *
 * @property isVisible Флаг видимости диалога
 * @property content Composable-контент диалога
 */
@Stable
class DialogController {
    var isVisible by mutableStateOf(false)
        private set

    var content: (@Composable () -> Unit)? by mutableStateOf(null)
        private set

    /**
     * Показать диалог с указанным содержимым
     * @param content Composable-функция с содержимым диалога
     */
    fun showDialog(content: @Composable () -> Unit) {
        this.content = content
        this.isVisible = true
    }

    /**
     * Скрыть диалог и очистить содержимое
     */
    fun hideDialog() {
        this.isVisible = false
        this.content = null
    }
}

/**
 * Создает и запоминает экземпляр DialogController.
 * Для каждой платформы будет своя реализация.
 */
@Composable
expect fun rememberDialogController(): DialogController