package com.stilllynnthecloset.libmpd.api

import com.stilllynnthecloset.libmpd.MpdObject
import com.stilllynnthecloset.libmpd.protocol.MpdProtocolVersion

public data class MpdStats constructor(
    val artists: Int?,
    val albums: Int?,
    val songs: Int?,
    val daemonUptime: Long?,
    val daemonPlayTime: Long?,
    val dbPlayTime: Long?,
    val dbUpdate: Long?,
) : MpdObject {
    public constructor(pairs: List<Pair<String, String>>) : this(
        artists = pairs.firstOrNull { it.first == "artists" }?.second?.toInt(),
        albums = pairs.firstOrNull { it.first == "albums" }?.second?.toInt(),
        songs = pairs.firstOrNull { it.first == "songs" }?.second?.toInt(),
        daemonUptime = pairs.firstOrNull { it.first == "uptime" }?.second?.toLong(),
        daemonPlayTime = pairs.firstOrNull { it.first == "playtime" }?.second?.toLong(),
        dbPlayTime = pairs.firstOrNull { it.first == "db_playtime" }?.second?.toLong(),
        dbUpdate = pairs.firstOrNull { it.first == "db_update" }?.second?.toLong(),
    )

    override val minMpdProtocolVersion: MpdProtocolVersion = MpdProtocolVersion.Protocol1
}
