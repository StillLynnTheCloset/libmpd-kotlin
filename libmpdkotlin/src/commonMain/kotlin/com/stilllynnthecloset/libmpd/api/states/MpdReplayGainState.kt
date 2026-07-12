package com.stilllynnthecloset.libmpd.api.states

import com.stilllynnthecloset.libmpd.MpdObject
import com.stilllynnthecloset.libmpd.protocol.MpdProtocolVersion

public enum class MpdReplayGainState constructor(private val stateKey: String) : MpdObject {
    OFF("off"),
    TRACK("track"),
    ALBUM("album"),
    AUTO("auto"),
    ;

    override val minMpdProtocolVersion: MpdProtocolVersion = MpdProtocolVersion.Protocol16

    override fun toString(): String = stateKey

    internal companion object {
        internal fun fromStateKey(stateKey: String): MpdReplayGainState = values().first { it.stateKey == stateKey }
    }
}
