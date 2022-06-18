package com.stilllynnthecloset.libmpd

public object LibMpd {
    public fun getConnection(
        address: String = "localhost",
        port: Int = 6600,
        debug: Boolean = false,
    ): MpdConnection = MpdConnection(address, port, debug)
}
