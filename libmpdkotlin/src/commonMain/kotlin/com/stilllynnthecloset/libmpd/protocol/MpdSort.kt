package com.stilllynnthecloset.libmpd.protocol

import com.stilllynnthecloset.libmpd.MpdObject

/**
 * sort sorts the result by the specified tag. The sort is descending if the tag is prefixed with a minus (‘-‘).
 * Without sort, the order is undefined. Only the first tag value will be used, if multiple of the same type exist.
 * To sort by “Artist”, “Album” or “AlbumArtist”, you should specify “ArtistSort”, “AlbumSort” or “AlbumArtistSort”
 * instead. These will automatically fall back to the former if “*Sort” doesn't exist. “AlbumArtist” falls back to
 * just “Artist”. The type “Last-Modified” can sort by file modification time.
 */
public data class MpdSort constructor(val tag: MpdSongTag, val direction: MpdSortDirection = MpdSortDirection.ASCENDING) : MpdObject {
    override fun toString(): String = "${direction.prefix}$tag"

    override val minMpdProtocolVersion: MpdProtocolVersion = MpdProtocolVersion.Protocol1

    public enum class MpdSortDirection constructor(internal val prefix: String) {
        ASCENDING(""),
        DESCENDING("-"),
    }
}
