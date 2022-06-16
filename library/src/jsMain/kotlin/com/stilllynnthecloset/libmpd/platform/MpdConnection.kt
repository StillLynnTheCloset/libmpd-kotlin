package com.stilllynnthecloset.libmpd.platform

import com.stilllynnthecloset.libmpd.protocol.MpdCommand
import com.stilllynnthecloset.libmpd.protocol.MpdCommandList

public actual class MpdConnection internal actual constructor(
    private val address: String,
    private val port: Int,
    private val debug: Boolean,
) {
    public actual fun runCommand(command: MpdCommand): List<Pair<String, String>> {
        TODO("Not yet implemented")
    }

    public actual fun runCommandList(commandList: MpdCommandList): List<Pair<String, String>> {
        TODO("Not yet implemented")
    }
}
