package com.stilllynnthecloset.libmpd.protocol

import com.stilllynnthecloset.libmpd.MpdObject

/**
 * To facilitate faster adding of files etc. you can pass a list of commands all at once using a command list.
 * The command list begins with command_list_begin or command_list_ok_begin and ends with command_list_end.
 *
 * It does not execute any commands until the list has ended. The response is a concatenation of all individual
 * responses. On success for all commands, OK is returned. If a command fails, no more commands are executed and the
 * appropriate ACK error is returned. If command_list_ok_begin is used, list_OK is returned for each successful
 * command executed in the command list.
 *
 * @see {https://www.musicpd.org/doc/html/protocol.html#command-lists}
 */
public data class MpdCommandList constructor(
    val commands: List<MpdCommand>,
    val listOk: Boolean = false,
) : MpdObject {
    override val minMpdProtocolVersion: MpdProtocolVersion = MpdProtocolVersion.Protocol1
}
