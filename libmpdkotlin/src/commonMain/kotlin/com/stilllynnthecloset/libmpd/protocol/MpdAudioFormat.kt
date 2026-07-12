package com.stilllynnthecloset.libmpd.protocol

import com.stilllynnthecloset.libmpd.MpdObject

/**
 * https://www.musicpd.org/doc/html/user.html#global-audio-format
 *
 * TODO: Restrict values of [bits]
 */
public data class MpdAudioFormat constructor(
    val sampleRate: Int?,
    val bits: String?,
    val channels: Int?,
) : MpdObject {
    internal constructor(string: String) : this(string.split(":"))

    internal constructor(strings: List<String>) : this(strings[0].toIntOrNull(), strings[1], strings[2].toIntOrNull())

    override val minMpdProtocolVersion: MpdProtocolVersion = MpdProtocolVersion.Protocol1

    override fun toString(): String = "${sampleRate ?: "*"}:${bits ?: "*"}:${channels ?: "*"}"
}
