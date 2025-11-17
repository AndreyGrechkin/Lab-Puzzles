package com.defey.labpuzzles

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform