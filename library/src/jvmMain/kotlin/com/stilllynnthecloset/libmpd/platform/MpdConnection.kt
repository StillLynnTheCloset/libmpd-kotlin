package com.stilllynnthecloset.libmpd.platform

import com.stilllynnthecloset.libmpd.protocol.MpdCommand
import com.stilllynnthecloset.libmpd.protocol.MpdCommandList
import com.stilllynnthecloset.libmpd.protocol.MpdException
import com.stilllynnthecloset.libmpd.protocol.MpdProtocolVersion
import okio.BufferedSink
import okio.BufferedSource
import okio.buffer
import okio.sink
import okio.source
import okio.use
import java.net.InetAddress
import java.net.Socket

public actual class MpdConnection internal actual constructor(
    private val address: String,
    private val port: Int,
    private val debug: Boolean,
) {
    private companion object {
        private val versionRegex = Regex("OK MPD (.*)")
    }

    @Throws(MpdException::class)
    public actual fun runCommand(command: MpdCommand): List<Pair<String, String>> {
        val (sink, source, socket) = openSocket(address, port)
        socket.use {
            val protocolVersion = readVersion(source)
            if (debug) {
                Log.error(protocolVersion)
            }
            writeCommand(command, sink)
            return readResults(source)
        }
    }

    @Throws(MpdException::class)
    public actual fun runCommandList(commandList: MpdCommandList): List<Pair<String, String>> {
        val (sink, source, socket) = openSocket(address, port)
        socket.use {
            val protocolVersion = readVersion(source)
            Log.error(protocolVersion)
            val listStart = if (commandList.listOk) {
                "command_list_ok_begin"
            } else {
                "command_list_begin"
            }
            sink.writeUtf8("$listStart\r\n")
            commandList.commands.forEach { command ->
                writeCommand(command, sink)
            }
            sink.writeUtf8("command_list_end\r\n")
            sink.flush()
            return readResults(source)
        }
    }

    private fun openSocket(address: String, port: Int): Triple<BufferedSink, BufferedSource, Socket> {
        val inetAddress = InetAddress.getByName(address)
        val socket = Socket(inetAddress, port)
        val source = socket.source().buffer()
        val sink = socket.sink().buffer()
        return Triple(sink, source, socket)
    }

    private fun readVersion(source: BufferedSource): MpdProtocolVersion {
        val line = source.readUtf8LineStrict()
        val matches = versionRegex.matchEntire(line)
        return MpdProtocolVersion.fromVersionString(
            matches?.groupValues?.get(1).orEmpty()
        )
    }

    private fun writeCommand(command: MpdCommand, sink: BufferedSink) {
        val toRun = command.commandString()
        if (debug) {
            Log.error("Writing `$toRun`")
        }
        sink.writeUtf8("$toRun\r\n").flush()
    }

    private fun readResults(source: BufferedSource): List<Pair<String, String>> {
        val results: MutableList<Pair<String, String>> = mutableListOf()
        var line: String
        var key: String
        var value: String
        do {
            line = source.readUtf8LineStrict()
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
                source.readByteArray(value.toLong())
            }
        } while (!(line == "OK" || line.startsWith("ACK")))

        return results
    }
}
