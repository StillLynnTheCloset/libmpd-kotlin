package com.stilllynnthecloset.libmpd.api.states

import com.stilllynnthecloset.libmpd.MpdObject
import com.stilllynnthecloset.libmpd.protocol.MpdProtocolVersion


public enum class MpdPlayingState constructor(private val stateKey: String) : MpdObject {
    PLAYING("play"),
    STOPPED("stop"),
    PAUSED("pause"),
    ;

    override val minMpdProtocolVersion: MpdProtocolVersion = MpdProtocolVersion.Protocol1

    override fun toString(): String = stateKey

    internal companion object {
        internal fun fromStateKey(stateKey: String): MpdPlayingState = values().first { it.stateKey == stateKey }
    }
}
