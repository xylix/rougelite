package rougelite.utils

/** Source: https://stackoverflow.com/a/41855007 */
inline fun <reified T : Enum<T>> enumContains(name: String): Boolean {
    return T::class.java.enumConstants.any { it.name == name}
}
