package com.stilllynnthecloset.libmpd.demo

import com.stilllynnthecloset.libmpd.LibMpd
import com.stilllynnthecloset.libmpd.api.MpdSong
import com.stilllynnthecloset.libmpd.api.MpdStats
import com.stilllynnthecloset.libmpd.api.MpdStatus
import com.stilllynnthecloset.libmpd.protocol.MpdCommand
import com.stilllynnthecloset.libmpd.protocol.MpdCommandList
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val connection = LibMpd.getConnection(debug = true)
    val result = connection.runCommand(MpdCommand.Status())
    println(result)
    println(MpdStatus(result))
    val result2 = connection.runCommand(MpdCommand.Stats())
    println(result2)
    println(MpdStats(result2))
    val result3 = connection.runCommand(MpdCommand.CurrentSong())
    println(result3)
    println(MpdSong(result3))
    val result4 = connection.runCommand(MpdCommand.Commands())
    println(result4)
    println(MpdSong(result4))

    val result5 = connection.runCommandList(MpdCommandList(
        commands = listOf(
            MpdCommand.Status(),
            MpdCommand.Stats(),
            MpdCommand.CurrentSong(),
        ),
        listOk = true,
    ))
    println(result5.joinToString(separator = "\n"))


}
