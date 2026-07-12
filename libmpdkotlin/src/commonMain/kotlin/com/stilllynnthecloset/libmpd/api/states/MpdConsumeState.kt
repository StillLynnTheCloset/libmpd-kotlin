package com.stilllynnthecloset.libmpd.api.states

import com.stilllynnthecloset.libmpd.MpdObject
import com.stilllynnthecloset.libmpd.protocol.MpdProtocolVersion

public enum class MpdConsumeState constructor(private val stateKey: String) : MpdObject {
    OFF("0"),
    ON("1"),
    ;

    override val minMpdProtocolVersion: MpdProtocolVersion = MpdProtocolVersion.Protocol15

    override fun toString(): String = stateKey

    internal companion object {
        internal fun fromStateKey(stateKey: String): MpdConsumeState = values().first { it.stateKey == stateKey }
    }
}
