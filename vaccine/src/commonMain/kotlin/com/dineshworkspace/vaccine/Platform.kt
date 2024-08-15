package com.dineshworkspace.vaccine

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform