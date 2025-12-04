package com.defey.labpuzzles.models

sealed class LineStartResult {
    data class Success(
        val lineColor: Int,
        val startPosition: FlowFreePosition
    ) : LineStartResult()

    data class Error(
        val error: FlowFreeError,
        val position: FlowFreePosition
    ) : LineStartResult()
}