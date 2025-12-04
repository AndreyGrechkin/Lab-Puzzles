package com.defey.labpuzzles.models

sealed class LineContinueResult {
    data class Success(
        val newPath: List<FlowFreePosition>,  // Обновленный путь
        val reachedEndpoint: Boolean = false  // Достигли второй точки?
    ) : LineContinueResult()

    data class Error(
        val error: FlowFreeError,
        val position: FlowFreePosition
    ) : LineContinueResult()
}