package com.stilllynnthecloset.libmpd

import com.stilllynnthecloset.libmpd.protocol.MpdProtocolVersion

public interface MpdObject {
    public val minMpdProtocolVersion: MpdProtocolVersion
}