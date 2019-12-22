package xylixlike.utils

import javafx.scene.input.KeyCode

/**
 * Parse String to Integer with default value if String isn't a valid Integer.
 * @param s String to parse
 * @param defaultValue int value to default to
 * @return Parsed Integer or the default
 */
fun valueOfWithDefault(s: String, defaultValue: Int): Int {
    return if (s.matches("-?\\d+".toRegex())) s.toInt() else defaultValue
}

fun toDirection(key: KeyCode?): Int {
    return when (key) {
        KeyCode.DOWN -> 0
        KeyCode.RIGHT -> 90
        KeyCode.UP -> 180
        KeyCode.LEFT -> 270
        else -> throw IllegalArgumentException()
    }
}