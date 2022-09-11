package com.stilllynnthecloset.libmpd

import com.stilllynnthecloset.libmpd.platform.Log
import com.stilllynnthecloset.libmpd.protocol.MpdCommand
import com.stilllynnthecloset.libmpd.protocol.MpdCommandList
import com.stilllynnthecloset.libmpd.protocol.MpdCommandResult
import com.stilllynnthecloset.libmpd.protocol.MpdException
import com.stilllynnthecloset.libmpd.protocol.MpdProtocolVersion
import io.ktor.network.selector.SelectorManager
import io.ktor.network.sockets.Socket
import io.ktor.network.sockets.aSocket
import io.ktor.network.sockets.openReadChannel
import io.ktor.network.sockets.openWriteChannel
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.ByteWriteChannel
import io.ktor.utils.io.core.use
import io.ktor.utils.io.readUTF8Line
import io.ktor.utils.io.writeStringUtf8
import kotlinx.coroutines.Dispatchers

public class MpdConnection internal constructor(
    private val address: String,
    private val port: Int,
    private val debug: Boolean,
) {
    private companion object {
        private val versionRegex = Regex("OK MPD (.*)")
    }

    private val selectorManager = SelectorManager(Dispatchers.Default) // Can't Use IO since it only exists on JVM.

    @Throws(MpdException::class)
    public suspend fun <T : MpdCommandResult> runCommand(command: MpdCommand<T>): T {
        if (debug) {
            Log.debug("Opening connection to $address:$port")
        }
        val (sink, source, socket) = openSocket(address, port)
        socket.use {
            val version = readVersion(source)
            if (version < command.minMpdProtocolVersion) {
                throw MpdException("Command not supported")
            }
            writeCommand(command, sink)
            return command.parseResult(readResults(source))
        }
    }

    @Throws(MpdException::class)
    public suspend fun runCommandList(commands: List<MpdCommand<*>>): List<MpdCommandResult> {
        if (debug) {
            Log.debug("Opening connection to $address:$port")
        }
        val (sink, source, socket) = openSocket(address, port)
        socket.use {
            readVersion(source)

            return commands.map { command ->
                writeCommand(command, sink)
                command.parseResult(readResults(source))
            }
        }
    }

    @Throws(MpdException::class)
    public suspend fun runCommandList(vararg commands: MpdCommand<*>): List<MpdCommandResult> {
        if (debug) {
            Log.debug("Opening connection to $address:$port")
        }
        val (sink, source, socket) = openSocket(address, port)
        socket.use {
            readVersion(source)

            return commands.map { command ->
                writeCommand(command, sink)
                command.parseResult(readResults(source))
            }
        }
    }

    @Throws(MpdException::class)
    public suspend fun runCommandList(commandList: MpdCommandList): MpdCommandResult.CommandListResult {
        if (debug) {
            Log.debug("Opening connection to $address:$port")
        }
        val (sink, source, socket) = openSocket(address, port)
        socket.use {
            readVersion(source)

            val listStart = if (commandList.listOk) {
                "command_list_ok_begin"
            } else {
                "command_list_begin"
            }
            sink.writeStringUtf8("$listStart\r\n")
            commandList.commands.forEach { command ->
                writeCommand(command, sink)
            }
            sink.writeStringUtf8("command_list_end\r\n")
            return MpdCommandResult.CommandListResult(readResults(source))
        }
    }

    private suspend fun openSocket(address: String, port: Int): Triple<ByteWriteChannel, ByteReadChannel, Socket> {
        val socket = aSocket(selectorManager).tcp().connect(address, port)
        val sendChannel = socket.openWriteChannel(autoFlush = true)
        val receiveChannel = socket.openReadChannel()
        return Triple(sendChannel, receiveChannel, socket)
    }

    private suspend fun readVersion(source: ByteReadChannel): MpdProtocolVersion {
        val line = source.readUTF8Line().orEmpty()

        if (debug) {
            Log.debug("Got connection OK + Version = $line")
        }

        val matches = versionRegex.matchEntire(line)
        val match = matches?.groupValues?.get(1).orEmpty()

        if (debug) {
            Log.debug("Got protocol string = $match")
        }

        return MpdProtocolVersion.fromVersionString(match).also {
            if (debug) {
                Log.debug("Got protocolVersion = $it")
            }
        }
    }

    private suspend fun writeCommand(command: MpdCommand<*>, sink: ByteWriteChannel) {
        val toRun = command.commandString()
        if (debug) {
            Log.debug("Writing `$toRun`")
        }
        sink.writeStringUtf8("$toRun\r\n")
    }

    private suspend fun readResults(source: ByteReadChannel): List<Pair<String, String>> {
        val results: MutableList<Pair<String, String>> = mutableListOf()
        var line: String
        var key: String
        var value: String
        do {
            line = source.readUTF8Line().orEmpty()

            if (debug) {
                Log.debug("Read line `$line`")
            }
            if (line.startsWith("ACK")) {
                throw MpdException(line)
            }

            key = line.substringBefore(':', "VERSION")
            value = line.substringAfter(':').trimStart()

            if (!(line == "OK" || line.startsWith("ACK"))) {
                val result = key to value
                results.add(result)
            }
            if (key == "binary") {
                // Read the next $value bytes into a buffer
                source.readPacket(value.toInt())
            }
        } while (!(line == "OK" || line.startsWith("ACK")))

        return results
    }
}
