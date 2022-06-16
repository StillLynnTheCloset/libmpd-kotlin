package com.stilllynnthecloset.libmpd.protocol

import com.stilllynnthecloset.libmpd.MpdObject

/**
 * https://www.musicpd.org/doc/html/protocol.html#tags
 */
public enum class MpdSongTag constructor(
    private val tagKey: String
) : MpdObject {
    ANY("any"),
    ARTIST("artist"),
    ARTIST_SORT("artistsort"),
    ALBUM("album"),
    ALBUM_SORT("albumsort"),
    ALBUM_ARTIST("albumartist"),
    ALBUM_ARTIST_SORT("albumartistsort"),
    TITLE("Title"),
    TRACK("track"),
    NAME("name"),
    GENRE("genre"),
    DATE("date"),
    COMPOSER("composer"),
    PERFORMER("performer"),
    WORK("work"),
    GROUPING("grouping"),
    COMMENT("comment"),
    DISC("disc"),
    LABEL("label"),
    MUSICBRAINZ_ARTISTID("musicbrainz_artistid"),
    MUSICBRAINZ_ALBUMID("musicbrainz_albumid"),
    MUSICBRAINZ_ALBUMARTISTID("musicbrainz_albumartistid"),
    MUSICBRAINZ_TRACKID("musicbrainz_trackid"),
    MUSICBRAINZ_RELEASETRACKID("musicbrainz_releasetrackid"),
    MUSICBRAINZ_WORKID("musicbrainz_workid"),
    ;

    override fun toString(): String = tagKey

    override val minMpdProtocolVersion: MpdProtocolVersion = MpdProtocolVersion.Protocol1
}
