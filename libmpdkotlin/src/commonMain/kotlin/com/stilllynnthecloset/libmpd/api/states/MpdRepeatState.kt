package com.stilllynnthecloset.libmpd.api.states

import com.stilllynnthecloset.libmpd.MpdObject
import com.stilllynnthecloset.libmpd.protocol.MpdProtocolVersion

public enum class MpdRepeatState constructor(private val stateKey: String) : MpdObject {
    OFF("0"),
    ON("1"),
    ;

    override val minMpdProtocolVersion: MpdProtocolVersion = MpdProtocolVersion.Protocol1

    override fun toString(): String = stateKey

    internal companion object {
        internal fun fromStateKey(stateKey: String): MpdRepeatState = values().first { it.stateKey == stateKey }
    }
}
