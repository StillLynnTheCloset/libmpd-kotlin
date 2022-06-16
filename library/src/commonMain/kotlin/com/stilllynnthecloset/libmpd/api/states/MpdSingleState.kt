package com.stilllynnthecloset.libmpd.api.states

import com.stilllynnthecloset.libmpd.MpdObject
import com.stilllynnthecloset.libmpd.protocol.MpdProtocolVersion

public enum class MpdSingleState constructor(internal val stateKey: String) : MpdObject {
    OFF("0"),
    ON("1"),
    ONE_SHOT("oneshot") {
        override val minMpdProtocolVersion: MpdProtocolVersion = MpdProtocolVersion.Protocol20
    };

    override val minMpdProtocolVersion: MpdProtocolVersion = MpdProtocolVersion.Protocol15

    override fun toString(): String = stateKey

    internal companion object {
        internal fun fromStateKey(stateKey: String): MpdSingleState = values().first { it.stateKey == stateKey }
    }
}
