package com.defey.labpuzzles.managers.timer

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class ForwardTimerImpl : ForwardTimer {
    override fun start(intervalMillis: Long, onTick: ((Long) -> Unit)?)
    override fun stop()
    override fun dispose()
    override fun getCurrentTime(): Long
    override val isRunning: Boolean
    override val elapsedTime: Long
}