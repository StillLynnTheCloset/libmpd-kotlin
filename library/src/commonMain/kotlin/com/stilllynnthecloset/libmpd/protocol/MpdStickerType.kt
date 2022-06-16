package com.stilllynnthecloset.libmpd.protocol

import com.stilllynnthecloset.libmpd.MpdObject

public enum class MpdStickerType constructor(private val typeName: String) : MpdObject {
    Song("song"),
    ;

    override fun toString(): String = typeName

    override val minMpdProtocolVersion: MpdProtocolVersion = MpdProtocolVersion.Protocol1
}
