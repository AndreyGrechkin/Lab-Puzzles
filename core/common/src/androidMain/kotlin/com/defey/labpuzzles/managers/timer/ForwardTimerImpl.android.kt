package com.defey.labpuzzles.managers.timer

import java.util.Timer
import java.util.TimerTask

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class ForwardTimerImpl : ForwardTimer {

    private var timer: Timer? = null
    private var startTime: Long = 0L
    private var _elapsedTime: Long = 0L
    private var _isRunning: Boolean = false

    actual override val isRunning: Boolean get() = _isRunning
    actual override val elapsedTime: Long get() = _elapsedTime

    actual override fun start(intervalMillis: Long, onTick: ((Long) -> Unit)?) {
        stop()

        startTime = System.currentTimeMillis()
        _isRunning = true

        timer = Timer().apply {
            schedule(object : TimerTask() {
                override fun run() {
                    _elapsedTime = System.currentTimeMillis() - startTime
                    onTick?.invoke(_elapsedTime)
                }
            }, 0, intervalMillis)
        }
    }

    actual override fun stop() {
        timer?.cancel()
        timer = null
        _isRunning = false
    }

    actual override fun dispose() {
        stop()
        _elapsedTime = 0L
    }

    actual override fun getCurrentTime(): Long {
        return System.currentTimeMillis()
    }
}