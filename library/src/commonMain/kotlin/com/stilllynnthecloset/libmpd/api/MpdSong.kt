package com.stilllynnthecloset.libmpd.api

import com.stilllynnthecloset.libmpd.platform.Log
import com.stilllynnthecloset.libmpd.MpdObject
import com.stilllynnthecloset.libmpd.getDouble
import com.stilllynnthecloset.libmpd.getInt
import com.stilllynnthecloset.libmpd.getString
import com.stilllynnthecloset.libmpd.protocol.MpdProtocolVersion
import com.stilllynnthecloset.libmpd.protocol.MpdSongTag

public data class MpdSong constructor(
    val file: String?,
    val artist: String?,
    val artistSort: String?,
    val album: String?,
    val albumSort: String?,
    val albumArtist: String?,
    val albumArtistSort: String?,
    val title: String?,
    val track: Int?,
    val name: String?,
    val genre: String?,
    val date: String?,
    val composer: String?,
    val performer: String?,
    val work: String?,
    val grouping: String?,
    val comment: String?,
    val disc: Int?,
    val label: String?,
    val duration: Double?,
    val time: Int?,
    val range: String?,
    val format: String?,
    val lastModified: String?,
    val musicBrainzArtistId: String?,
    val musicBrainzAlbumId: String?,
    val musicBrainzAlbumArtistId: String?,
    val musicBrainzTrackId: String?,
    val musicBrainzReleaseTrackId: String?,
    val musicBrainzWorkId: String?,
) : MpdObject {
    internal companion object {
        fun splitByKey(key: String, pairs: List<Pair<String, String>>): List<List<Pair<String, String>>> {
            val output = mutableListOf<List<Pair<String, String>>>()
            var currentSubList = mutableListOf<Pair<String, String>>()
            pairs.forEach { currentPair ->
                if (currentPair.first == key) {
                    output.add(currentSubList)
                    currentSubList = mutableListOf()
                }
                currentSubList.add(currentPair)
            }
            output.add(currentSubList)
            return output.filter { it.isNotEmpty() }
        }
        fun parsePairsToSongs(pairs: List<Pair<String, String>>): List<MpdSong> {
            val splitPairs = splitByKey("file", pairs)
            Log.error(splitPairs.toString())
            return splitPairs.map { MpdSong(it) }
        }
    }

    override val minMpdProtocolVersion: MpdProtocolVersion = MpdProtocolVersion.Protocol1

    public constructor(pairs: List<Pair<String, String>>) : this(
            file = pairs.getString("file"),
            artist = pairs.getString(MpdSongTag.ARTIST.toString()),
            artistSort = pairs.getString(MpdSongTag.ARTIST_SORT.toString()),
            album = pairs.getString(MpdSongTag.ALBUM.toString()),
            albumSort = pairs.getString(MpdSongTag.ALBUM_SORT.toString()),
            albumArtist = pairs.getString(MpdSongTag.ALBUM_ARTIST.toString()),
            albumArtistSort = pairs.getString(MpdSongTag.ALBUM_ARTIST_SORT.toString()),
            title = pairs.getString(MpdSongTag.TITLE.toString()),
            track = pairs.getInt(MpdSongTag.TRACK.toString()),
            name = pairs.getString(MpdSongTag.NAME.toString()),
            genre = pairs.getString(MpdSongTag.GENRE.toString()),
            date = pairs.getString(MpdSongTag.DATE.toString()),
            composer = pairs.getString(MpdSongTag.COMPOSER.toString()),
            performer = pairs.getString(MpdSongTag.PERFORMER.toString()),
            work = pairs.getString(MpdSongTag.WORK.toString()),
            grouping = pairs.getString(MpdSongTag.GROUPING.toString()),
            comment = pairs.getString(MpdSongTag.COMMENT.toString()),
            disc = pairs.getInt(MpdSongTag.DISC.toString()),
            label = pairs.getString(MpdSongTag.LABEL.toString()),
            duration = pairs.getDouble("duration"),
            time = pairs.getInt("time"),
            range = pairs.getString("Range"),
            format = pairs.getString("Format"),
            lastModified = pairs.getString("Last-Modified"),
            musicBrainzArtistId = pairs.getString(MpdSongTag.MUSICBRAINZ_ARTISTID.toString()),
            musicBrainzAlbumId = pairs.getString(MpdSongTag.MUSICBRAINZ_ALBUMID.toString()),
            musicBrainzAlbumArtistId = pairs.getString(MpdSongTag.MUSICBRAINZ_ALBUMARTISTID.toString()),
            musicBrainzTrackId = pairs.getString(MpdSongTag.MUSICBRAINZ_TRACKID.toString()),
            musicBrainzReleaseTrackId = pairs.getString(MpdSongTag.MUSICBRAINZ_RELEASETRACKID.toString()),
            musicBrainzWorkId = pairs.getString(MpdSongTag.MUSICBRAINZ_WORKID.toString()),
    )
}
