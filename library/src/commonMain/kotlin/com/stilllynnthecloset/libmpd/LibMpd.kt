package com.stilllynnthecloset.libmpd

import com.stilllynnthecloset.libmpd.platform.MpdConnection

public object LibMpd {
    public fun getConnection(
        address: String = "localhost",
        port: Int = 6600,
        debug: Boolean = false,
    ): MpdConnection = MpdConnection(address, port, debug)
}
