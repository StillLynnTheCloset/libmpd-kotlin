package com.stilllynnthecloset.libmpd.platform

internal expect object Log {
    fun debug(message: Any)

    fun error(message: Any)
}
