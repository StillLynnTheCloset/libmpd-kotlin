package com.stilllynnthecloset.libmpd.protocol

public enum class MpdProtocolVersion constructor(
    private val versionString: String
) {
    Protocol1("0.01"),
    Protocol14("0.14"),
    Protocol15("0.15"),
    Protocol16("0.16"),
    Protocol20("0.20"),
    Protocol21("0.21"),
    Protocol22("0.22"),
    Protocol23("0.23"),
    Protocol24("0.24"),
    ;

    internal companion object {
        internal fun fromVersionString(versionString: String): MpdProtocolVersion =
            values().firstOrNull { versionString.startsWith(it.versionString) } ?: throw IllegalArgumentException("Unknown version: $versionString")
    }
}
