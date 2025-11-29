package com.defey.labpuzzles.managers.timer

interface CommonTimer {
    fun start(
        durationMillis: Long,
        intervalMillis: Long = 1000,
        onTick: ((Long) -> Unit)? = null,
        onFinish: (() -> Unit)? = null
    )
    fun stop()
    fun dispose()
}