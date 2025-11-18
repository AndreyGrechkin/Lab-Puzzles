package com.defey.labpuzzles.base

data class NavOptions(
    val popUpToRoute: String? = null,
    val inclusive: Boolean = false,
    val saveState: Boolean = false,
    val restoreState: Boolean = false
)