package com.stilllynnthecloset.libmpd.api

import com.stilllynnthecloset.libmpd.MpdObject
import com.stilllynnthecloset.libmpd.protocol.MpdProtocolVersion

/**
 * database: the song database has been modified after update.
 * update: a database update has started or finished. If the database was modified during the update, the database event is also emitted.
 * stored_playlist: a stored playlist has been modified, renamed, created or deleted
 * playlist: the queue (i.e. the current playlist) has been modified
 * player: the player has been started, stopped or seeked
 * mixer: the volume has been changed
 * output: an audio output has been added, removed or modified (e.g. renamed, enabled or disabled)
 * options: options like repeat, random, crossfade, replay gain
 * partition: a partition was added, removed or changed
 * sticker: the sticker database has been modified.
 * subscription: a client has subscribed or unsubscribed to a channel
 * message: a message was received on a channel this client is subscribed to; this event is only emitted when the queue is empty
 */
public enum class MpdSubsystem constructor(private val subsystemName: String) : MpdObject {
    DATABASE("database"),
    UPDATE("update"),
    STORED_PLAYLIST("stored_playlist"),
    PLAYLIST("playlist"),
    PLAYER("player"),
    MIXER("mixer"),
    OUTPUT("output"),
    OPTIONS("options"),
    PARTITION("partition"),
    STICKER("sticker"),
    SUBSCRIPTION("subscription"),
    MESSAGE("message"),
    ;

    override val minMpdProtocolVersion: MpdProtocolVersion = MpdProtocolVersion.Protocol1

    override fun toString(): String = subsystemName

    internal companion object {
        fun fromName(name: String): MpdSubsystem = values().first { it.subsystemName == name }
    }
}
