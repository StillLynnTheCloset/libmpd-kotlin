package com.stilllynnthecloset.libmpd.api

import com.stilllynnthecloset.libmpd.MpdObject
import com.stilllynnthecloset.libmpd.api.states.*
import com.stilllynnthecloset.libmpd.protocol.MpdAudioFormat
import com.stilllynnthecloset.libmpd.protocol.MpdProtocolVersion

public data class MpdStatus constructor(
    val volume: Int?,
    val repeat: MpdRepeatState?,
    val random: MpdRandomState?,
    val single: MpdSingleState?,
    val consume: MpdConsumeState?,
    val playlist: Int?,
    val playlistLength: Int?,
    val state: MpdPlayingState?,
    val song: Int?,
    val songId: Int?,
    val nextSong: Int?,
    val nextSongId: Int?,
    val time: String?,
    val elapsed: Double?,
    val duration: Double?,
    val bitRate: Int?,
    val crossfade: Int?,
    val mixrampdb: Double?,
    val mixrampdelay: Int?,
    val audio: MpdAudioFormat?,
    val updatingDbJobId: Int?,
    val error: String?,
) : MpdObject {
    override val minMpdProtocolVersion: MpdProtocolVersion = MpdProtocolVersion.Protocol1

    public constructor(pairs: List<Pair<String, String>>) : this(
        volume = pairs.firstOrNull { it.first == "volume" }?.second?.toInt(),
        repeat = pairs.firstOrNull { it.first == "repeat" }?.second?.let { MpdRepeatState.fromStateKey(it) },
        random = pairs.firstOrNull { it.first == "random" }?.second?.let { MpdRandomState.fromStateKey(it) },
        single = pairs.firstOrNull { it.first == "single" }?.second?.let { MpdSingleState.fromStateKey(it) },
        consume = pairs.firstOrNull { it.first == "consume" }?.second?.let { MpdConsumeState.fromStateKey(it) },
        playlist = pairs.firstOrNull { it.first == "playlist" }?.second?.toInt(),
        playlistLength = pairs.firstOrNull { it.first == "playlistlength" }?.second?.toInt(),
        state = pairs.firstOrNull { it.first == "state" }?.second?.let { MpdPlayingState.fromStateKey(it) },
        song = pairs.firstOrNull { it.first == "song" }?.second?.toInt(),
        songId = pairs.firstOrNull { it.first == "songid" }?.second?.toInt(),
        nextSong = pairs.firstOrNull { it.first == "nextsong" }?.second?.toInt(),
        nextSongId = pairs.firstOrNull { it.first == "nextsongid" }?.second?.toInt(),
        time = pairs.firstOrNull { it.first == "time" }?.second,
        elapsed = pairs.firstOrNull { it.first == "elapsed" }?.second?.toDouble(),
        duration = pairs.firstOrNull { it.first == "duration" }?.second?.toDouble(),
        bitRate = pairs.firstOrNull { it.first == "bitrate" }?.second?.toInt(),
        crossfade = pairs.firstOrNull { it.first == "xfade" }?.second?.toInt(),
        mixrampdb = pairs.firstOrNull { it.first == "mixrampdb" }?.second?.toDouble(),
        mixrampdelay = pairs.firstOrNull { it.first == "mixrampdelay" }?.second?.toInt(),
        audio = pairs.firstOrNull { it.first == "audio" }?.second?.let { MpdAudioFormat(it) },
        updatingDbJobId = pairs.firstOrNull { it.first == "updating_db" }?.second?.toInt(),
        error = pairs.firstOrNull { it.first == "error" }?.second,
    )
}
