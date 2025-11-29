package com.defey.labpuzzles.managers.timer

import android.os.CountDownTimer

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class CommonTimerImpl : CommonTimer {

    private var countDownTimer: CountDownTimer? = null

    actual override fun start(
        durationMillis: Long,
        intervalMillis: Long,
        onTick: ((Long) -> Unit)?,
        onFinish: (() -> Unit)?
    ) {
        stop()
        countDownTimer = object : CountDownTimer(durationMillis, intervalMillis) {
            override fun onTick(millisUntilFinished: Long) {
                onTick?.invoke(millisUntilFinished)
            }

            override fun onFinish() {
                onFinish?.invoke()
            }
        }.start()
    }

    actual override fun stop() {
        countDownTimer?.cancel()
    }

    actual override fun dispose() {
        stop()
        countDownTimer = null
    }
}