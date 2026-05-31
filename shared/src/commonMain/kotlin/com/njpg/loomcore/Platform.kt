package com.njpg.loomcore

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform