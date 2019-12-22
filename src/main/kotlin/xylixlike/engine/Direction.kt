package xylixlike.engine

import xylixlike.utils.enumContains
import kotlin.math.abs

@Suppress("MagicNumber")
enum class Direction(val degrees: Int) {
    DOWN(0),
    RIGHT(90),
    UP(180),
    LEFT(270);
    fun reverse() = fromInt(degrees + 180 % 360)

    companion object {
        fun fromInt(value: Int) = Direction.values().first { it.degrees == abs(value % 360) }
        fun validDirection(name: String): Boolean { return enumContains<Direction>(name) }
    }
}
