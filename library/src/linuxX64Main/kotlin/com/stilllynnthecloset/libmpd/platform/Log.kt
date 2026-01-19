package com.stilllynnthecloset.libmpd.platform

internal actual object Log {
    actual fun debug(message: Any) {
        println(message)
    }

    actual fun error(message: Any) {
        println("error: $message")
    }
}
