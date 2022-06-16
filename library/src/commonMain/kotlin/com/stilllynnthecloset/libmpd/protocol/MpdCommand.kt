package com.stilllynnthecloset.libmpd.protocol

import com.stilllynnthecloset.libmpd.MpdObject
import com.stilllynnthecloset.libmpd.api.*
import com.stilllynnthecloset.libmpd.api.states.*

/**
 * https://www.musicpd.org/doc/html/protocol.html
 */
public sealed class MpdCommand : MpdObject {
    internal companion object {
        fun escapeString(input: String): String {
            return "\"${input.replace("\"", "\\\"")}\""
        }
    }

    public abstract val commandKey: String

    public open val argumentsList: List<String> = emptyList()

    override val minMpdProtocolVersion: MpdProtocolVersion = MpdProtocolVersion.Protocol1

    internal fun commandString(): String =
        argumentsList.joinToString(prefix = "$commandKey ", separator = " ")

    /**
     * Allows a client to issue a command that is not yet implemented by this library.
     */
    public data class Unimplemented(
        override val commandKey: String,
        override val argumentsList: List<String> = emptyList()
    ) : MpdCommand()

    // region Status Commands

    /**
     * Clears the current error message in status (this is also accomplished by any command that starts playback).
     */
    public  class ClearError : MpdCommand() {
        override val commandKey: String = "clearerror"
    }

    /**
     * Displays the song info of the current song (same song that is identified in status).
     */
    public class CurrentSong : MpdCommand() {
        override val commandKey: String = "currentsong"
    }

    /**
     * Waits until there is a noteworthy change in one or more of MPD’s subsystems. As soon as there is one, it lists
     * all changed systems in a line in the format `changed: SUBSYSTEM`
     *
     * Change events accumulate, even while the connection is not in “idle” mode; no events gets lost while the client
     * is doing something else with the connection. If an event had already occurred since the last call, the new idle
     * command will return immediately.
     * While a client is waiting for idle results, the server disables timeouts, allowing a client to wait for events
     * as long as mpd runs. The idle command can be canceled by sending [StopIdle] (no other commands are allowed).
     * MPD will then leave idle mode and print results immediately; might be empty at this time.
     *
     * If the optional [subsystems] argument is used, MPD will only send notifications when something changed in one of
     * the specified [subsystems].
     */
    public data class Idle constructor(val subsystems: List<MpdSubsystem>? = null) : MpdCommand() {
        override val commandKey: String = "idle"

        override val argumentsList: List<String> = listOfNotNull(subsystems?.joinToString { it.toString() })
    }

    /**
     * Send while waiting for an [Idle] to exit the waiting.
     */
    public class StopIdle : MpdCommand() {
        override val commandKey: String = "noidle"
    }

    /**
     * Reports the current status of the player and the volume level.
     * MPD may omit lines which have no (known) value. Older MPD versions used to have a “magic” value for “unknown”, e.g. “volume: -1”.
     *
     * volume: 0-100 (deprecated: -1 if the volume cannot be determined)
     * repeat: 0 or 1
     * random: 0 or 1
     * single [2]: 0, 1, or oneshot [6]
     * consume [2]: 0 or 1
     * playlist: 31-bit unsigned integer, the playlist version number
     * playlistlength: integer, the length of the playlist
     * state: play, stop, or pause
     * song: playlist song number of the current song stopped on or playing
     * songid: playlist songid of the current song stopped on or playing
     * nextsong [2]: playlist song number of the next song to be played
     * nextsongid [2]: playlist songid of the next song to be played
     * time: total time elapsed (of current playing/paused song) in seconds (deprecated, use elapsed instead)
     * elapsed [3]: Total time elapsed within the current song in seconds, but with higher resolution.
     * duration [5]: Duration of the current song in seconds.
     * bitrate: instantaneous bitrate in kbps
     * xfade: crossfade in seconds
     * mixrampdb: mixramp threshold in dB
     * mixrampdelay: mixrampdelay in seconds
     * audio: The format emitted by the decoder plugin during playback, format: samplerate:bits:channels. See Global Audio Format for a detailed explanation.
     * updating_db: job id
     * error: if there is an error, returns message here
     */
    public class Status : MpdCommand() {
        override val commandKey: String = "status"
    }

    /**
     * Displays statistics.
     *
     * artists: number of artists
     * albums: number of albums
     * songs: number of songs
     * uptime: daemon uptime in seconds
     * db_playtime: sum of all song times in the database in seconds
     * db_update: last db update in UNIX time
     * playtime: time length of music played
     */
    public class Stats : MpdCommand() {
        override val commandKey: String = "stats"
    }

    // endregion Status Commands

    // region Playback Options Commands

    /**
     * Sets consume state to [state]. When consume is activated, each song played is removed from playlist.
     */
    public data class Consume constructor(val state: MpdConsumeState) : MpdCommand() {
        override val commandKey: String = "consume"

        override val argumentsList: List<String> = listOf(state.toString())

        override val minMpdProtocolVersion: MpdProtocolVersion = MpdProtocolVersion.Protocol15
    }

    /**
     * Sets crossfading between songs.
     */
    public data class Crossfade constructor(val seconds: Long) : MpdCommand() {
        override val commandKey: String = "crossfade"

        override val argumentsList: List<String> = listOf(seconds.toString())
    }

    /**
     * Sets the threshold at which songs will be overlapped. Like crossfading but doesn’t fade the track volume, just overlaps. The songs need to have MixRamp tags added by an external tool. 0dB is the normalized maximum volume so use negative values, I prefer -17dB. In the absence of mixramp tags crossfading will be used. See http://sourceforge.net/projects/mixramp
     */
    public data class MixRampDB constructor(val deciBels: Int) : MpdCommand() {
        override val commandKey: String = "mixrampdb"

        override val argumentsList: List<String> = listOf(deciBels.toString())
    }

    /**
     * Additional time subtracted from the overlap calculated by mixrampdb. A value of “nan” disables MixRamp overlapping and falls back to crossfading.
     */
    public data class MixRampDelay constructor(val seconds: Long) : MpdCommand() {
        override val commandKey: String = "mixrampdelay"

        override val argumentsList: List<String> = listOf(seconds.toString())
    }

    /**
     * Sets random state to [state].
     */
    public data class Random constructor(val state: MpdRandomState) : MpdCommand() {
        override val commandKey: String = "random"

        override val argumentsList: List<String> = listOf(state.toString())
    }

    /**
     * Sets repeat state to [state].
     */
    public data class Repeat constructor(val state: MpdRepeatState) : MpdCommand() {
        override val commandKey: String = "repeat"

        override val argumentsList: List<String> = listOf(state.toString())
    }

    /**
     * Sets volume to [volume], the range of volume is 0-100.
     */
    public data class SetVolume constructor(val volume: Int) : MpdCommand() {
        init {
            check(volume in 0..100) { "Volume must be between 0 and 100"}
        }

        override val commandKey: String = "setvol"

        override val argumentsList: List<String> = listOf(volume.toString())
    }

    /**
     * Sets single state to [state]. When single is activated, playback is stopped after current song, or song is
     * repeated if the ‘repeat’ mode is enabled.
     */
    public data class Single constructor(val state: MpdSingleState) : MpdCommand() {
        override val commandKey: String = "single"

        override val argumentsList: List<String> = listOf(state.stateKey)

        override val minMpdProtocolVersion: MpdProtocolVersion = MpdProtocolVersion.Protocol15
    }

    /**
     * Sets the replay gain mode. One of off, track, album, auto . Changing the mode during playback may take several seconds, because the new settings does not affect the buffered data. This command triggers the options idle event.
     */
    public data class ReplayGainMode constructor(val mode: MpdReplayGainState) : MpdCommand() {
        override val commandKey: String = "replay_gain_mode"

        override val argumentsList: List<String> = listOf(mode.toString())
    }

    /**
     * Prints replay gain options. Currently, only the variable replay_gain_mode is returned.
     */
    public class ReplayGainStatus : MpdCommand() {
        override val commandKey: String = "replay_gain_status"
    }

    /**
     * Changes volume by amount [volume]. Deprecated, use setvol instead.
     */
    @Deprecated("Use `SetVolume` instead.")
    public data class Volume constructor(val volume: Int) : MpdCommand() {
        override val commandKey: String = "volume"

        override val argumentsList: List<String> = listOf(volume.toString())
    }

    // endregion Playback Options Commands

    // region Playback Control Commands

    /**
     * Plays next song in the playlist.
     */
    public class Next : MpdCommand() {
        override val commandKey: String = "next"
    }

    /**
     * Toggles pause/resumes playing.
     * The use of pause command without the [pause] argument is deprecated.
     */
    public data class Pause constructor(val pause: MpdPauseState) : MpdCommand() {
        @Deprecated("The use of pause command without the PAUSE argument is deprecated.")
        public constructor() : this(MpdPauseState.TOGGLE)

        override val commandKey: String = "pause"

        override val argumentsList: List<String> = listOf(pause.toString())
    }

    /**
     * Begins playing the playlist at song number [songPosition].
     */
    public data class PlayByPosition constructor(val songPosition: Int? = null) : MpdCommand() {
        override val commandKey: String = "play"

        override val argumentsList: List<String> = listOfNotNull(songPosition?.toString())
    }

    /**
     * Begins playing the playlist at song [songId].
     */
    public data class PlayById constructor(val songId: Int? = null) : MpdCommand() {
        override val commandKey: String = "playid"

        override val argumentsList: List<String> = listOfNotNull(songId?.toString())
    }

    /**
     * Plays previous song in the playlist.
     */
    public class Previous : MpdCommand() {
        override val commandKey: String = "previous"
    }

    /**
     * Seeks to the position [time] (in seconds; fractions allowed) of entry [songPosition] in the playlist.
     */
    public data class SeekByPosition constructor(val songPosition: Int, val time: Double) : MpdCommand() {
        override val commandKey: String = "seek"

        override val argumentsList: List<String> = listOf(songPosition.toString(), time.toString())
    }

    /**
     * Seeks to the position [time] (in seconds; fractions allowed) of song [songId].
     */
    public data class SeekById constructor(val songId: Int, val time: Double) : MpdCommand() {
        override val commandKey: String = "seekid"

        override val argumentsList: List<String> = listOf(songId.toString(), time.toString())
    }

    /**
     * Seeks to the position [time] (in seconds; fractions allowed) within the current song. If prefixed by + or -,
     * then the time is relative to the current playing position.
     *
     * TODO: Better handle relative positions.
     */
    public data class SeekCurrent constructor(val time: Double, val relative: Boolean = false) : MpdCommand() {
        override val commandKey: String = "seekcur"

        override val argumentsList: List<String> = listOf(time.toString())
    }

    /**
     * Stops playing.
     */
    public class Stop : MpdCommand() {
        override val commandKey: String = "stop"
    }

    // endregion Playback Control Commands

    // region Queue Manipulation Commands

    /**
     * Adds the file URI to the playlist (directories add recursively). URI can also be a single file.
     */
    public data class Add constructor(val path: String) : MpdCommand() {
        override val commandKey: String = "add"

        override val argumentsList: List<String> = listOf(path)
    }

    /**
     * Adds a song to the playlist (non-recursive) and returns the song id. URI is always a single file or URL.
     */
    public data class AddId constructor(val path: String, val position: Int? = null) : MpdCommand() {
        override val commandKey: String = "addid"

        override val argumentsList: List<String> = listOfNotNull(path, position?.toString())
    }

    /**
     * Clears the queue.
     */
    public class Clear : MpdCommand() {
        override val commandKey: String = "clear"
    }

    /**
     * Deletes songs at [range] from the playlist.
     */
    public data class Delete constructor(val range: MpdRange) : MpdCommand() {
        public constructor(position: Int) : this(MpdRange(position))

        override val commandKey: String = "delete"

        override val argumentsList: List<String> = listOf(range.toString())
    }

    /**
     * Deletes the song [songId] from the playlist
     */
    public data class DeleteId constructor(val songId: Int) : MpdCommand() {
        override val commandKey: String = "deleteid"

        override val argumentsList: List<String> = listOf(songId.toString())
    }

    /**
     * Moves the song at [from] to [to] in the playlist.
     */
    public data class Move constructor(val from: Int, val to: Int) : MpdCommand() {
        override val commandKey: String = "move"

        override val argumentsList: List<String> = listOf(from.toString(), to.toString())
    }

    /**
     * Moves the range of songs at [from] to [to] in the playlist.
     */
    public data class MoveRange constructor(val from: MpdRange, val to: Int) : MpdCommand() {
        override val commandKey: String = "move"

        override val argumentsList: List<String> = listOf(from.toString(), to.toString())

        override val minMpdProtocolVersion: MpdProtocolVersion =
            MpdProtocolVersion.Protocol15
    }

    /**
     * Moves the song with FROM (songid) to TO (playlist index) in the playlist. If TO is negative, it is relative to the current song in the playlist (if there is one).
     */
    public data class MoveId constructor(val from: Int, val to: Int) : MpdCommand() {
        override val commandKey: String = "moveid"

        override val argumentsList: List<String> = listOf(from.toString(), to.toString())
    }

    /**
     * Displays the queue.
     * Do not use this, instead use playlistinfo.
     */
    @Deprecated("Deprecated by MPD")
    public class Playlist : MpdCommand() {
        override val commandKey: String = "playlist"
    }

    /**
     * Finds songs in the queue with strict matching.
     */
    public data class PlaylistFind constructor(val tag: String, val needle: String) : MpdCommand() {
        override val commandKey: String = "playlistfind"

        override val argumentsList: List<String> = listOf(tag, needle)
    }

    /**
     * Displays a list of songs in the playlist. [songId] is optional and specifies a single song to display info for.
     */
    public data class PlaylistId constructor(val songId: Int? = null) : MpdCommand() {
        override val commandKey: String = "playlistid"

        override val argumentsList: List<String> = listOfNotNull(songId?.toString())
    }

    /**
     * Displays a list of all songs in the playlist, or if the optional argument is given, displays information only
     * for the [range] of songs.
     */
    public data class PlaylistInfo constructor(val range: MpdRange? = null) : MpdCommand() {
        public constructor(position: Int) : this(MpdRange(position))

        override val commandKey: String = "playlistinfo"

        override val argumentsList: List<String> = listOfNotNull(range?.toString())
    }

    /**
     * Searches case-insensitively for partial matches in the queue.
     */
    // TODO: playlistsearch {TAG} {NEEDLE}
    public class PlaylistSearch constructor() : MpdCommand() {
        override val commandKey: String = "playlistsearch"

        override val argumentsList: List<String> = listOf()
    }

    /**
     * Displays changed songs currently in the playlist since VERSION. Start and end positions may be given to limit the output to changes in the given range.
     * To detect songs that were deleted at the end of the playlist, use playlistlength returned by status command.
     */
    public data class PlaylistChanges constructor(val version: Int, val range: MpdRange?) : MpdCommand() {
        override val commandKey: String = "plchanges"

        override val argumentsList: List<String> = listOfNotNull(version.toString(), range?.toString())
    }

    /**
     * Displays changed songs currently in the playlist since VERSION. This function only returns the position and the id of the changed song, not the complete metadata. This is more bandwidth efficient.
     * To detect songs that were deleted at the end of the playlist, use playlistlength returned by status command.
     */
    public data class PlaylistChangesPosId constructor(val version: Int, val range: MpdRange?) : MpdCommand() {
        override val commandKey: String = "plchangesposid"

        override val argumentsList: List<String> = listOfNotNull(version.toString(), range?.toString())
    }

    /**
     * Set the priority of the specified songs. A higher priority means that it will be played first when “random” mode is enabled.
     * A priority is an integer between 0 and 255. The default priority of new songs is 0.
     */
    public data class SetPriority constructor(val priority: Int, val positions: List<MpdRange>) : MpdCommand() {
        init {
            check(priority in 0..255) { "Priority must be between 0 and 255" }
        }

        public constructor(priority: Int, vararg positions: MpdRange) : this(priority, positions.toList())

        public constructor(priority: Int, vararg positions: Int) : this(priority, positions.map {
            MpdRange(
                it
            )
        })

        override val commandKey: String = "prio"

        override val argumentsList: List<String> = listOf(priority.toString(), positions.joinToString())
    }

    /**
     * Same as [SetPriority], but address the songs with their id.
     */
    public data class SetPriorityById constructor(val priority: Int, val ids: List<Int>) : MpdCommand() {
        init {
            check(priority in 0..255) { "Priority must be between 0 and 255" }
        }

        public constructor(priority: Int, vararg ids: Int) : this(priority, ids.toList())

        override val commandKey: String = "SetPriorityById"

        override val argumentsList: List<String> = listOf(priority.toString(), ids.joinToString())
    }

    /**
     * Since MPD 0.19 Specifies the portion of the song that shall be played. START and END are offsets in seconds (fractional seconds allowed); both are optional. Omitting both (i.e. sending just “:”) means “remove the range, play everything”. A song that is currently playing cannot be manipulated this way.
     */
    // TODO: rangeid {ID} {START:END}
    public class RangeId constructor() : MpdCommand() {
        override val commandKey: String = "rangeid"

        override val argumentsList: List<String> = listOf()
    }

    /**
     * Shuffles the queue. START:END is optional and specifies a range of songs.
     */
    // TODO: shuffle [START:END]
    public class shuffle constructor() : MpdCommand() {
        override val commandKey: String = "shuffle"

        override val argumentsList: List<String> = listOf()
    }

    /**
     * Swaps the positions of SONG1 and SONG2.
     */
    // TODO: swap {SONG1} {SONG2}
    public class swap constructor() : MpdCommand() {
        override val commandKey: String = "swap"

        override val argumentsList: List<String> = listOf()
    }

    /**
     * Swaps the positions of SONG1 and SONG2 (both song ids).
     */
    // TODO: swapid {SONG1} {SONG2}
    public class swapid constructor() : MpdCommand() {
        override val commandKey: String = "swapid"

        override val argumentsList: List<String> = listOf()
    }

    /**
     * Adds a tag to the specified song. Editing song tags is only possible for remote songs. This change is volatile: it may be overwritten by tags received from the server, and the data is gone when the song gets removed from the queue.
     */
    // TODO: addtagid {SONGID} {TAG} {VALUE}
    public class addtagid constructor() : MpdCommand() {
        override val commandKey: String = "addtagid"

        override val argumentsList: List<String> = listOf()
    }

    /**
     * Removes tags from the specified song. If TAG is not specified, then all tag values will be removed. Editing song tags is only possible for remote songs.
     */
    // TODO: cleartagid {SONGID} [TAG]
    public class cleartagid constructor() : MpdCommand() {
        override val commandKey: String = "cleartagid"

        override val argumentsList: List<String> = listOf()
    }

    // endregion Queue Manipulation Commands

    // region Playlist Manipulation Commands

    /**
     * Lists the songs in the playlist. Playlist plugins are supported.
     */
    public data class ListPlaylist constructor(val name: String) : MpdCommand() {
        override val commandKey: String = "listplaylist"

        override val argumentsList: List<String> = listOf(name)
    }

    /**
     * Lists the songs with metadata in the playlist. Playlist plugins are supported.
     */
    public data class ListPlaylistInfo constructor(val name: String) : MpdCommand() {
        override val commandKey: String = "listplaylistinfo"

        override val argumentsList: List<String> = listOf(name)
    }

    /**
     * Prints a list of the playlist directory. After each playlist name the server sends its last modification time as attribute “Last-Modified” in ISO 8601 format. To avoid problems due to clock differences between clients and the server, clients should not compare this value with their local clock.
     */
    public class ListPlaylists : MpdCommand() {
        override val commandKey: String = "listplaylists"
    }

    /**
     * Loads the playlist into the current queue. Playlist plugins are supported. A range may be specified to load only a part of the playlist.
     */
    public data class Load constructor(val name: String, val range: MpdRange?) : MpdCommand() {
        override val commandKey: String = "load"

        override val argumentsList: List<String> = mutableListOf(name).apply {
            if (range != null) {
                add(range.toString())
            }
        }
    }

    /**
     * Adds URI to the playlist NAME.m3u. NAME.m3u will be created if it does not exist.
     */
    public data class PlaylistAdd constructor(val name: String, val uri: String) : MpdCommand() {
        override val commandKey: String = "playlistadd"

        override val argumentsList: List<String> = listOf(name, uri)
    }

    /**
     * Clears the playlist NAME.m3u.
     */
    public data class PlaylistClear constructor(val name: String) : MpdCommand() {
        override val commandKey: String = "playlistclear"

        override val argumentsList: List<String> = listOf(name)
    }

    /**
     * Deletes SONGPOS from the playlist NAME.m3u.
     */
    public data class PlaylistDelete constructor(val name: String, val songPos: Int) : MpdCommand() {
        override val commandKey: String = "playlistdelete"

        override val argumentsList: List<String> = listOf(name, songPos.toString())
    }

    /**
     * Moves the song at position FROM in the playlist NAME.m3u to the position TO.
     */
    public data class PlaylistMove constructor(val name: String, val from: Int, val to: Int) : MpdCommand() {
        override val commandKey: String = "playlistmove"

        override val argumentsList: List<String> = listOf(name, from.toString(), to.toString())
    }

    /**
     * Renames the playlist NAME.m3u to NEW_NAME.m3u.
     */
    public data class RenamePlaylist constructor(val name: String, val newName: String) : MpdCommand() {
        override val commandKey: String = "rename"

        override val argumentsList: List<String> = listOf(name, newName)
    }

    /**
     * Removes the playlist NAME.m3u from the playlist directory.
     */
    public data class RemovePlaylist constructor(val name: String) : MpdCommand() {
        override val commandKey: String = "rm"

        override val argumentsList: List<String> = listOf(name)
    }

    /**
     * Saves the queue to NAME.m3u in the playlist directory.
     */
    public data class SavePlaylist constructor(val name: String) : MpdCommand() {
        override val commandKey: String = "save"

        override val argumentsList: List<String> = listOf(name)
    }

    // endregion Playlist Manipulation Commands

    // region Database Manipulation Commands

    /**
     * Locate album art for the given song and return a chunk of an album art image file at [offset].
     * This is currently implemented by searching the directory the file resides in for a file called
     * cover.png, cover.jpg, cover.tiff or cover.bmp.
     * Returns the file size and actual number of bytes read at the requested offset, followed by the chunk requested
     * as raw bytes (see Binary Responses), then a newline and the completion code.
     */
    public data class AlbumArt constructor(val uri: String, val offset: Int) : MpdCommand() {
        override val commandKey: String = "albumart"

        override val argumentsList: List<String> = listOf(uri, offset.toString())
    }

    /**
     * Count the number of songs and their total playtime in the database matching [filter].
     * The [group] keyword may be used to group the results by a tag.
     * A [group] with an empty value contains counts of matching song which don’t this group tag.
     * It exists only if at least one such song is found.
     */
    public data class Count constructor(val filter: MpdFilter, val group: MpdSongTag? = null) : MpdCommand() {
        override val commandKey: String = "count"

        override val argumentsList: List<String> = listOfNotNull(filter.toString(), group?.let { "group $it" })
    }

    /**
     * Calculate the song’s audio fingerprint.
     * This command is only available if MPD was built with libchromaprint (-Dchromaprint=enabled).
     */
    public data class GetFingerPrint constructor(val uri: String) : MpdCommand() {
        override val commandKey: String = "getfingerprint"

        override val argumentsList: List<String> = listOf(uri)
    }

    /**
     * Search the database for songs matching [filter].
     * [sort] sorts the result by the specified tag. The sort is descending if the tag is prefixed with a minus (‘-‘).
     * Without sort, the order is undefined. Only the first tag value will be used, if multiple of the same type exist.
     * To sort by “Artist”, “Album” or “AlbumArtist”, you should specify “ArtistSort”, “AlbumSort” or “AlbumArtistSort”
     * instead. These will automatically fall back to the former if “*Sort” doesn’t exist. “AlbumArtist” falls back to
     * just “Artist”. The type “Last-Modified” can sort by file modification time.
     * [window] can be used to query only a portion of the real response.
     */
    public data class Find constructor(val filter: MpdFilter, val sort: MpdSort? = null, val window: MpdRange? = null) : MpdCommand() {
        override val commandKey: String = "find"

        override val argumentsList: List<String> =
            listOfNotNull(filter.toString(), sort?.let { "sort $it" }, window?.let { "window $it" })
    }

    /**
     * Search the database for songs matching [filter] and add them to the queue. Parameters have the same meaning as for find.
     */
    public data class FindAdd constructor(val filter: MpdFilter, val sort: MpdSort? = null, val window: MpdRange? = null) : MpdCommand() {
        override val commandKey: String = "findadd"

        override val argumentsList: List<String> =
            listOfNotNull(filter.toString(), sort?.let { "sort $it" }, window?.let { "window $it" })
    }

    /**
     * Lists unique tags values of the specified type. [type] can be any tag supported by MPD.
     * Additional arguments may specify a [filter]. [group] may be used (repeatedly) to group the results by one or more tags.
     * The following example lists all album names, grouped by their respective (album) artist:
     * list album group albumartist
     * list file was implemented in an early MPD version, but does not appear to make a lot of sense. It still
     * works (to avoid breaking compatibility), but is deprecated.
     */
    public data class ListTags constructor(val type: MpdSongTag, val filter: MpdFilter, val group: MpdSongTag? = null) : MpdCommand() {
        override val commandKey: String = "list"

        override val argumentsList: List<String> =
            listOfNotNull(type.toString(), filter.toString(), group?.let { "group $group" })
    }

    /**
     * Lists all songs and directories in [uri].
     * Do not use this command. Do not manage a client-side copy of MPD’s database. That is fragile and adds huge
     * overhead. It will break with large databases. Instead, query MPD whenever you need something.
     */
    public data class ListAll constructor(val uri: String? = null) : MpdCommand() {
        override val commandKey: String = "listall"

        override val argumentsList: List<String> = listOfNotNull(uri)
    }

    /**
     * Same as [ListAll], except it also returns metadata info in the same format as [LSInfo]
     * Do not use this command. Do not manage a client-side copy of MPD’s database. That is fragile and adds huge
     * overhead. It will break with large databases. Instead, query MPD whenever you need something.
     */
    public data class ListAllInfo constructor(val uri: String? = null) : MpdCommand() {
        override val commandKey: String = "listallinfo"

        override val argumentsList: List<String> = listOfNotNull(uri)
    }

    /**
     * Lists the contents of the directory [uri], including files are not recognized by MPD. URI can be a path
     * relative to the music directory or an URI understood by one of the storage plugins. The response contains at
     * least one line for each directory entry with the prefix “file: ” or “directory: “, and may be followed by
     * file attributes such as “Last-Modified” and “size”.
     * For example, “smb://SERVER” returns a list of all shares on the given SMB/CIFS server; “nfs://servername/path”
     * obtains a directory listing from the NFS server.
     */
    public data class ListFiles constructor(val uri: String? = null) : MpdCommand() {
        override val commandKey: String = "listfiles"

        override val argumentsList: List<String> = listOfNotNull(uri)
    }

    /**
     * Lists the contents of the directory [uri]. The response contains records starting with file, directory or playlist, each followed by metadata (tags or other metadata).
     * When listing the root directory, this currently returns the list of stored playlists. This behavior is deprecated; use “listplaylists” instead.
     * This command may be used to list metadata of remote files (e.g. URI beginning with “http://” or “smb://”).
     * Clients that are connected via local socket may use this command to read the tags of an arbitrary local file (URI is an absolute path).
     */
    public data class LSInfo constructor(val uri: String? = null) : MpdCommand() {
        override val commandKey: String = "lsinfo"

        override val argumentsList: List<String> = listOfNotNull(uri)
    }

    /**
     * Read “comments” (i.e. key-value pairs) from the file specified by [uri]. This “URI” can be a path relative to the music directory or an absolute path.
     * This command may be used to list metadata of remote files (e.g. URI beginning with “http://” or “smb://”).
     * The response consists of lines in the form “KEY: VALUE”. Comments with suspicious characters (e.g. newlines) are ignored silently.
     * The meaning of these depends on the codec, and not all decoder plugins support it. For example, on Ogg files, this lists the Vorbis comments.
     */
    public data class ReadComments constructor(val uri: String? = null) : MpdCommand() {
        override val commandKey: String = "readcomments"

        override val argumentsList: List<String> = listOfNotNull(uri)
    }

    /**
     * Search the database for songs matching [filter]. Parameters have the same meaning as for find, except that search is not case sensitive.
     */
    public data class Search constructor(val filter: MpdFilter, val sort: MpdSort? = null, val window: MpdRange? = null) : MpdCommand() {
        override val commandKey: String = "search"

        override val argumentsList: List<String> =
            listOfNotNull(escapeString(filter.toString()), sort?.let { "sort $it" }, window?.let { "window $it" })
    }

    /**
     * Search the database for songs matching [filter] and add them to the queue.
     * Parameters have the same meaning as for search.
     */
    public data class SearchAddToQueue constructor(
        val filter: MpdFilter,
        val sort: MpdSort? = null,
        val window: MpdRange? = null
    ) : MpdCommand() {
        override val commandKey: String = "searchadd"

        override val argumentsList: List<String> =
            listOfNotNull(filter.toString(), sort?.let { "sort $it" }, window?.let { "window $it" })
    }

    /**
     * Search the database for songs matching [filter]  and add them to the playlist named [name].
     * If a playlist by that name doesn't exist it is created.
     * Parameters have the same meaning as for search.
     */
    public data class SearchAddToPlaylist constructor(
        val name: String,
        val filter: MpdFilter,
        val sort: MpdSort? = null,
        val window: MpdRange? = null
    ) : MpdCommand() {
        override val commandKey: String = "searchaddpl"

        override val argumentsList: List<String> =
            listOfNotNull(name, filter.toString(), sort?.let { "sort $it" }, window?.let { "window $it" })
    }

    /**
     * Updates the music database: find new files, remove deleted files, update modified files.
     * [uri] is a particular directory or song/file to update. If you do not specify it, everything is updated.
     * Prints updating_db: JOBID where JOBID is a positive number identifying the update job. You can read the current job id in the status response.
     */
    public data class Update constructor(val uri: String? = null) : MpdCommand() {
        override val commandKey: String = "update"

        override val argumentsList: List<String> = listOfNotNull(uri)
    }

    /**
     * Same as update, but also rescans unmodified files.
     */
    public data class Rescan constructor(val uri: String? = null) : MpdCommand() {
        override val commandKey: String = "rescan"

        override val argumentsList: List<String> = listOfNotNull(uri)
    }

    // endregion Database Manipulation Commands

    // region Mounts Commands

    /**
     * Mount the specified remote storage URI at the given path.
     */
    public data class Mount constructor(val path: String, val uri: String) : MpdCommand() {
        override val commandKey: String = "mount"

        override val argumentsList: List<String> = listOf(path, uri)
    }

    /**
     * Unmounts the specified path.
     */
    public class Unmount : MpdCommand() {
        override val commandKey: String = "unmount"
    }

    /**
     * Queries a list of all mounts. By default, this contains just the configured music_directory.
     */
    public class ListMounts : MpdCommand() {
        override val commandKey: String = "listmounts"
    }

    /**
     * Queries a list of “neighbors” (e.g. accessible file servers on the local net). Items on that list may be used with the mount command.
     */
    public class ListNeighbors : MpdCommand() {
        override val commandKey: String = "listneighbors"
    }

    // endregion Mounts Commands

    // region Stickers Commands

    /**
     * Reads a sticker value for the specified object.
     */
    public data class StickerGet constructor(val type: MpdStickerType, val uri: String, val name: String) : MpdCommand() {
        override val commandKey: String = "sticker get"

        override val argumentsList: List<String> = listOf(type.toString(), uri, name)
    }

    /**
     * Adds a sticker value to the specified object. If a sticker item with that name already exists, it is replaced.
     */
    public data class StickerSet constructor(val type: MpdStickerType, val uri: String, val name: String, val value: String) : MpdCommand() {
        override val commandKey: String = "sticker set"

        override val argumentsList: List<String> = listOf(type.toString(), uri, name, value)
    }

    /**
     * Deletes a sticker value from the specified object. If you do not specify a sticker name, all sticker values are deleted.
     */
    public data class StickerDelete constructor(val type: MpdStickerType, val uri: String, val name: String) : MpdCommand() {
        override val commandKey: String = "sticker delete"

        override val argumentsList: List<String> = listOf(type.toString(), uri, name)
    }

    /**
     * Lists the stickers for the specified object.
     */
    public data class StickerList constructor(val type: MpdStickerType, val uri: String) : MpdCommand() {
        override val commandKey: String = "sticker list"

        override val argumentsList: List<String> = listOf(type.toString(), uri)
    }

    /**
     * Searches the sticker database for stickers with the specified name, below the specified directory (URI). For each matching song, it prints the URI and that one sticker’s value.
     * Searches for stickers with the given value.
     * Other supported operators are: “<”, “>”
     */
    public data class StickerFind constructor(val type: MpdStickerType, val uri: String, val name: String) : MpdCommand() {
        override val commandKey: String = "sticker find"

        override val argumentsList: List<String> = listOf(type.toString(), uri, name)
    }

    /**
     *
     */
    // TODO: sticker find {TYPE} {URI} {NAME} =|<|> {VALUE}
    public class stickerfind constructor() : MpdCommand() {
        override val commandKey: String = "sticker find"

        override val argumentsList: List<String> = listOf()
    }

    // endregion Stickers Commands

    // region Connection Manipulation Commands

    /**
     * Closes the connection to MPD. MPD will try to send the remaining output buffer before it actually closes the connection, but that cannot be guaranteed. This command will not generate a response.
     * Clients should not use this command; instead, they should just close the socket.
     */
    public class Close : MpdCommand() {
        override val commandKey: String = "close"
    }

    /**
     * Kills MPD.
     * Do not use this command. Send SIGTERM to MPD instead, or better: let your service manager handle MPD shutdown (e.g. systemctl stop mpd).
     */
    public class Kill : MpdCommand() {
        override val commandKey: String = "kill"
    }

    /**
     * This is used for authentication with the server. PASSWORD is simply the plaintext password.
     */
    public data class Password constructor(val password: String) : MpdCommand() {
        override val commandKey: String = "password"
    }

    /**
     * Does nothing but return “OK”.
     */
    public class Ping : MpdCommand() {
        override val commandKey: String = "ping"
    }

    /**
     * Shows a list of available tag types. It is an intersection of the metadata_to_use setting and this client’s tag mask.
     * About the tag mask: each client can decide to disable any number of tag types, which will be omitted from responses to this client. That is a good idea, because it makes responses smaller. The following tagtypes sub commands configure this list.
     */
    public class TagTypes : MpdCommand() {
        override val commandKey: String = "tagtypes"
    }

    /**
     * Remove one or more tags from the list of tag types the client is interested in. These will be omitted from responses to this client.
     */
    public data class TagTypesDisable constructor(val tags: List<MpdSongTag>) : MpdCommand() {
        override val commandKey: String = "tagtypes disable"
    }

    /**
     * Re-enable one or more tags from the list of tag types for this client. These will no longer be hidden from responses to this client.
     */
    public data class TagTypesEnable constructor(val tags: List<MpdSongTag>) : MpdCommand() {
        override val commandKey: String = "tagtypes enable"
    }

    /**
     * Clear the list of tag types this client is interested in. This means that MPD will not send any tags to this client.
     */
    public class TagTypesClear : MpdCommand() {
        override val commandKey: String = "tagtypes clear"
    }

    /**
     * Announce that this client is interested in all tag types. This is the default setting for new clients.
     */
    public class TagTypesAll : MpdCommand() {
        override val commandKey: String = "tagtypes all"
    }

    // endregion Connection Manipulation Commands

    // region Partition Commands

    /**
     * Switch the client to a different partition.
     */
    public data class SwitchPartition constructor(val name: String) : MpdCommand() {
        override val commandKey: String = "partition"

        override val argumentsList: List<String> = listOf(name)
    }

    /**
     * Print a list of partitions. Each partition starts with a partition keyword and the partition’s name, followed by information about the partition.
     */
    public class ListPartitions : MpdCommand() {
        override val commandKey: String = "listpartitions"
    }

    /**
     * Create a new partition.
     */
    public data class NewPartition constructor(val name: String) : MpdCommand() {
        override val commandKey: String = "newpartition"

        override val argumentsList: List<String> = listOf(name)
    }

    // endregion Partition Commands

    // region Audio Output Device Commands

    /**
     * Turns an output off.
     */
    public data class DisableOutput constructor(val id: String) : MpdCommand() {
        override val commandKey: String = "disableoutput"

        override val argumentsList: List<String> = listOf(id)
    }

    /**
     * Turns an output on.
     */
    public data class EnableOutput constructor(val id: String) : MpdCommand() {
        override val commandKey: String = "enableoutput"

        override val argumentsList: List<String> = listOf(id)
    }

    /**
     * Turns an output on or off, depending on the current state.
     */
    public data class ToggleOutput constructor(val id: String) : MpdCommand() {
        override val commandKey: String = "toggleoutput"

        override val argumentsList: List<String> = listOf(id)
    }

    /**
     * Shows information about all outputs.
     *
     * Return information:
     * outputid: ID of the output. May change between executions
     * outputname: Name of the output. It can be any.
     * outputenabled: Status of the output. 0 if disabled, 1 if enabled.
     */
    public class Outputs : MpdCommand() {
        override val commandKey: String = "outputs"
    }

    /**
     * Set a runtime attribute. These are specific to the output plugin, and supported values are usually printed in the outputs response.
     */
    public data class OutputSet constructor(val id: String, val name: String, val value: String) : MpdCommand() {
        override val commandKey: String = "outputset"

        override val argumentsList: List<String> = listOf(id, name, value)
    }

    // endregion Audio Output Device Commands

    // region Reflective Commands

    /**
     * Dumps configuration values that may be interesting for the client. This command is only permitted to “local” clients (connected via local socket).
     *
     * The following response attributes are available:
     * music_directory: The absolute path of the music directory.
     */
    public class Config : MpdCommand() {
        override val commandKey: String = "config"
    }

    /**
     * Shows which commands the current user has access to.
     */
    public class Commands : MpdCommand() {
        override val commandKey: String = "commands"
    }

    /**
     * Print a list of decoder plugins, followed by their supported suffixes and MIME types.
     */
    public class Decoders : MpdCommand() {
        override val commandKey: String = "decoders"
    }

    /**
     * Shows which commands the current user does not have access to.
     */
    public class NotCommands : MpdCommand() {
        override val commandKey: String = "notcommands"
    }

    /**
     * Gets a list of available URL handlers.
     */
    public class UrlHandlers : MpdCommand() {
        override val commandKey: String = "urlhandlers"
    }

    // endregion Reflective Commands

    // region Client-to-Client Commands

    /**
     * Subscribe to a channel. The channel is created if it does not exist already. The name may consist of alphanumeric ASCII characters plus underscore, dash, dot and colon.
     */
    public data class Subscribe constructor(val name: String) : MpdCommand() {
        override val commandKey: String = "subscribe"

        override val argumentsList: List<String> = listOf(name)
    }

    /**
     * Unsubscribe from a channel.
     */
    public data class Unsubscribe constructor(val name: String) : MpdCommand() {
        override val commandKey: String = "unsubscribe"

        override val argumentsList: List<String> = listOf(name)
    }

    /**
     * Obtain a list of all channels. The response is a list of “channel:” lines.
     */
    public class Channels : MpdCommand() {
        override val commandKey: String = "channels"
    }

    /**
     * Reads messages for this client. The response is a list of “channel:” and “message:” lines.
     */
    public class ReadMessages : MpdCommand() {
        override val commandKey: String = "readmessages"
    }

    /**
     * Send a message to the specified channel.
     */
    public data class SendMessage constructor(val channel: String, val text: String) : MpdCommand() {
        override val commandKey: String = "sendmessage"

        override val argumentsList: List<String> = listOf(channel, text)
    }

    // endregion Client-to-Client Commands
}
