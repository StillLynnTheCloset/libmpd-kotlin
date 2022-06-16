package com.stilllynnthecloset.libmpd.protocol

/**
 * https://www.musicpd.org/doc/html/protocol.html#failure-responses
 */
public data class MpdException constructor(
    val errorCode: MpdErrorCode?,
    val commandListNum: Int?,
    val currentCommand: String?,
    val messageText: String?,
) : RuntimeException(messageText) {
    internal companion object {
        val matcher = Regex("ACK \\[([0-9]*)@([0-9]*)] \\{(.*?)} (.*)")

        private fun Int.toErrorCode(): MpdErrorCode =
            MpdErrorCode.findErrorCode(this)

    }

    internal constructor(message: String) : this(matcher.find(message)?.groupValues)

    private constructor(matches: List<String>?) : this(
        errorCode = matches?.getOrNull(1)?.toIntOrNull()?.toErrorCode(),
        commandListNum = matches?.getOrNull(2)?.toIntOrNull(),
        currentCommand = matches?.getOrNull(3),
        messageText = matches?.getOrNull(4)
    )
}
