package com.maulik.toastx

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform