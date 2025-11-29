package com.defey.labpuzzles.managers.timer

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class CommonTimerImpl:CommonTimer {
    override fun start(
        durationMillis: Long,
        intervalMillis: Long,
        onTick: ((Long) -> Unit)?,
        onFinish: (() -> Unit)?
    )

    override fun stop()
    override fun dispose()
}