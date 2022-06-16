package com.stilllynnthecloset.libmpd.api.states

import com.stilllynnthecloset.libmpd.MpdObject
import com.stilllynnthecloset.libmpd.protocol.MpdProtocolVersion

public enum class MpdPauseState constructor(private val stateKey: String) : MpdObject {
    PLAY("0"),
    PAUSE("1"),
    TOGGLE(""),
    ;

    override val minMpdProtocolVersion: MpdProtocolVersion = MpdProtocolVersion.Protocol1

    override fun toString(): String = stateKey

    internal companion object {
        internal fun fromStateKey(stateKey: String): MpdPauseState = values().first { it.stateKey == stateKey }
    }
}
