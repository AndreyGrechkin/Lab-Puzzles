package com.defey.labpuzzles.managers.timer

interface ForwardTimer {
    fun start(
        intervalMillis: Long = 1000,
        onTick: ((Long) -> Unit)? = null
    )

    fun stop()
    fun dispose()

    fun getCurrentTime(): Long

    val isRunning: Boolean
    val elapsedTime: Long
}