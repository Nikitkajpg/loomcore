package com.njpg.loomcore.core

fun formatAsDate(date: String): String {
    var out = ""
    date.forEachIndexed { index, char ->
        out += char
        if ((index == 1 || index == 3) && index != date.lastIndex) {
            out += "."
        }
    }
    return out
}