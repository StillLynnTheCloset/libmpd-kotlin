package com.stilllynnthecloset.libmpd

internal fun List<Pair<String, String>>.getString(key: String): String? {
    return firstOrNull { key.equals(it.first, ignoreCase = true) }?.second
}

internal fun List<Pair<String, String>>.getInt(key: String): Int? {
    return firstOrNull { key.equals(it.first, ignoreCase = true) }?.second?.toInt()
}

internal fun List<Pair<String, String>>.getDouble(key: String): Double? {
    return firstOrNull { key.equals(it.first, ignoreCase = true) }?.second?.toDouble()
}
