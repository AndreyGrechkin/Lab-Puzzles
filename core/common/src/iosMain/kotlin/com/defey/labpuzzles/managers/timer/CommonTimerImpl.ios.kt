package com.defey.labpuzzles.managers.timer

import kotlinx.cinterop.ExperimentalForeignApi
import platform.darwin.DISPATCH_SOURCE_TYPE_TIMER
import platform.darwin.DISPATCH_TIME_NOW
import platform.darwin.dispatch_get_main_queue
import platform.darwin.dispatch_resume
import platform.darwin.dispatch_source_cancel
import platform.darwin.dispatch_source_create
import platform.darwin.dispatch_source_set_event_handler
import platform.darwin.dispatch_source_set_timer
import platform.darwin.dispatch_source_t
import platform.darwin.dispatch_time

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class CommonTimerImpl : CommonTimer {

    private var timer: dispatch_source_t? = null
    private var remainingTime: Long = 0
    private var intervalMillis: Long = 0
    private var onTick: ((Long) -> Unit)? = null
    private var onFinish: (() -> Unit)? = null

    @OptIn(ExperimentalForeignApi::class)
    actual override fun start(
        durationMillis: Long,
        intervalMillis: Long,
        onTick: ((Long) -> Unit)?,
        onFinish: (() -> Unit)?
    ) {
        stop()
        remainingTime = durationMillis
        this.intervalMillis = intervalMillis
        this.onTick = onTick
        this.onFinish = onFinish

        val queue = dispatch_get_main_queue()
        val timer = dispatch_source_create(DISPATCH_SOURCE_TYPE_TIMER, 0u, 0u, queue)!!
        this.timer = timer

        dispatch_source_set_timer(
            timer,
            dispatch_time(DISPATCH_TIME_NOW, 0),
            intervalMillis.toULong() * 1_000_000u,
            100000000u
        )

        dispatch_source_set_event_handler(timer) {
            remainingTime -= intervalMillis
            if (remainingTime <= 0L) {
                onFinish?.invoke()
                stop()
            } else {
                onTick?.invoke(remainingTime)
            }
        }
        dispatch_resume(timer)
    }

    actual override fun stop() {
        timer?.let {
            dispatch_source_cancel(it)
            timer = null
        }
    }

    actual override fun dispose() {
        stop()
    }
}