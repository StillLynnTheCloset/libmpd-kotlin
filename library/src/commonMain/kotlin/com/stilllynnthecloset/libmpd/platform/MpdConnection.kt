package com.stilllynnthecloset.libmpd.platform

import com.stilllynnthecloset.libmpd.protocol.MpdCommand
import com.stilllynnthecloset.libmpd.protocol.MpdCommandList
import com.stilllynnthecloset.libmpd.protocol.MpdException

public expect class MpdConnection internal constructor(address: String, port: Int, debug: Boolean) {
    @Throws(MpdException::class)
    public fun runCommand(command: MpdCommand): List<Pair<String, String>>

    @Throws(MpdException::class)
    public fun runCommandList(commandList: MpdCommandList): List<Pair<String, String>>
}
