package com.defey.labpuzzles.managers.timer

import platform.Foundation.NSDate
import platform.Foundation.NSTimer
import platform.Foundation.timeIntervalSinceReferenceDate

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class ForwardTimerImpl : ForwardTimer {

    private var timer: NSTimer? = null
    private var startTime: Double = 0.0
    private var _elapsedTime: Long = 0L
    private var _isRunning: Boolean = false

    actual override val isRunning: Boolean get() = _isRunning
    actual override val elapsedTime: Long get() = _elapsedTime

    actual override fun start(intervalMillis: Long, onTick: ((Long) -> Unit)?) {
        stop()

        startTime = NSDate.timeIntervalSinceReferenceDate()
        _isRunning = true

        timer = NSTimer.scheduledTimerWithTimeInterval(
            interval = intervalMillis / 1000.0,
            repeats = true,
            block = { _ ->
                val currentTime = NSDate.timeIntervalSinceReferenceDate()
                _elapsedTime = ((currentTime - startTime) * 1000).toLong()
                onTick?.invoke(_elapsedTime)
            }
        )
    }

    actual override fun stop() {
        timer?.invalidate()
        timer = null
        _isRunning = false
    }

    actual override fun dispose() {
        stop()
        _elapsedTime = 0L
    }

    actual override fun getCurrentTime(): Long {
        return (NSDate.timeIntervalSinceReferenceDate() * 1000).toLong()
    }
}