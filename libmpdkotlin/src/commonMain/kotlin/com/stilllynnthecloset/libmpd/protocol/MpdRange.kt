package com.stilllynnthecloset.libmpd.protocol

import com.stilllynnthecloset.libmpd.MpdObject

/**
 * Some commands (e.g. delete) allow specifying a range in the form START:END (the END item is not included in the
 * range, similar to ranges in the Python programming language). If END is omitted, then the maximum possible
 * value is assumed.
 *
 * @see {https://www.musicpd.org/doc/html/protocol.html#ranges}
 */
public data class MpdRange constructor(
    val start: Int,
    val end: Int,
) : MpdObject {

    init {
        check(start >= 0) { "Range must start at a non-negative index" }
        check(start < end) { "Start must be strictly less than end" }
    }

    public constructor(singleValue: Int) : this(singleValue, singleValue + 1)

    public constructor(range: IntRange) : this(range.first, range.last + 1)

    override fun toString(): String = "$start:$end"

    override val minMpdProtocolVersion: MpdProtocolVersion = MpdProtocolVersion.Protocol1
}
