package com.stilllynnthecloset.libmpd.protocol

import com.stilllynnthecloset.libmpd.MpdObject

/**
 * https://www.musicpd.org/doc/html/protocol.html#filters
 */
public sealed class MpdFilter : MpdObject {
    abstract override fun toString(): String

    override val minMpdProtocolVersion: MpdProtocolVersion = MpdProtocolVersion.Protocol21

    public data class TagEqual constructor(val tag: MpdSongTag, val value: String) : MpdFilter() {
        override fun toString(): String = "($tag == \"$value\")"
    }

    public data class TagContains constructor(val tag: MpdSongTag, val value: String) : MpdFilter() {
        override fun toString(): String = "($tag contains \"$value\")"
    }

    public data class TagMatches constructor(val tag: MpdSongTag, val pcre: String) : MpdFilter() {
        override fun toString(): String = "($tag =~ \"$pcre\")"
    }

    public data class FilePath constructor(val path: String) : MpdFilter() {
        override fun toString(): String = "(file == \"$path\")"
    }

    public data class BaseDirectory constructor(val dir: String) : MpdFilter() {
        override fun toString(): String = "(base \"$dir\")"
    }

    public data class ModifiedSince constructor(val value: String) : MpdFilter() {
        override fun toString(): String = "(modified-since \"$value\")"
    }

    public data class ExactAudioFormat constructor(val format: MpdAudioFormat) : MpdFilter() {
        init {
            if (format.sampleRate == null || format.bits == null || format.channels == null) {
                throw IllegalArgumentException("Exact Audio format cannot contain wildcards, use LooseAudioFormat instead. $format")
            }
        }

        override fun toString(): String = "(AudioFormat == \"$format\")"
    }

    public data class LooseAudioFormat constructor(val format: MpdAudioFormat) : MpdFilter() {
        override fun toString(): String = "(AudioFormat =~ \"$format\")"
    }

    public data class Negate constructor(val filter: MpdFilter) : MpdFilter() {
        override fun toString(): String = "(!$filter)"
    }

    public data class And constructor(val filter1: MpdFilter, val filter2: MpdFilter) : MpdFilter() {
        override fun toString(): String = "($filter1 AND $filter2)"
    }
}
