package com.stilllynnthecloset.libmpd.protocol

import com.stilllynnthecloset.libmpd.MpdObject
import com.stilllynnthecloset.libmpd.api.MpdSong
import com.stilllynnthecloset.libmpd.api.MpdStats
import com.stilllynnthecloset.libmpd.api.MpdStatus

public sealed class MpdCommandResult : MpdObject {
    override val minMpdProtocolVersion: MpdProtocolVersion = MpdProtocolVersion.Protocol1

    /**
     * The response to any command for which response parsing hasn't been implemented.
     */
    public data class UnimplementedResult constructor(
        val results: List<Pair<String, String>>,
    ) : MpdCommandResult()

    /**
     * The response to a command list.
     */
    public data class CommandListResult constructor(
        val results: List<Pair<String, String>>,
    ) : MpdCommandResult()

    /**
     * The response to commands that only return OK.
     */
    public object AckResultResult : MpdCommandResult()

    // region Status Commands

    public class ClearErrorResult : MpdCommandResult()

    public data class CurrentSongResult constructor(val mpdSong: MpdSong) : MpdCommandResult()

    public class IdleResult : MpdCommandResult()

    public class StopIdleResult : MpdCommandResult()

    public data class StatusResult constructor(val mpdStatus: MpdStatus) : MpdCommandResult()

    public data class StatsResult constructor(val mpdStats: MpdStats) : MpdCommandResult()

    // endregion Status Commands

    // region Playback Options Commands

    public class ConsumeResult : MpdCommandResult()

    public class CrossfadeResult : MpdCommandResult()

    public class MixRampDBResult : MpdCommandResult()

    public class MixRampDelayResult : MpdCommandResult()

    public class RandomResult : MpdCommandResult()

    public class RepeatResult : MpdCommandResult()

    public class SetVolumeResult : MpdCommandResult()

    public class SingleResult : MpdCommandResult()

    public class ReplayGainModeResult : MpdCommandResult()

    public class ReplayGainStatusResult : MpdCommandResult()

    @Deprecated("Use `SetVolume` instead.")
    public class VolumeResult : MpdCommandResult()

    // endregion Playback Options Commands

    // region Playback Control Commands

    public class NextResult : MpdCommandResult()

    public class PauseResult : MpdCommandResult()

    public class PlayByPositionResult : MpdCommandResult()

    public class PlayByIdResult : MpdCommandResult()

    public class PreviousResult : MpdCommandResult()

    public class SeekByPositionResult : MpdCommandResult()

    public class SeekByIdResult : MpdCommandResult()

    public class SeekCurrentResult : MpdCommandResult()

    public class StopResult : MpdCommandResult()

    // endregion Playback Control Commands

    // region Queue Manipulation Commands

    public class AddResult : MpdCommandResult()

    public class AddIdResult : MpdCommandResult()

    public class ClearResult : MpdCommandResult()

    public class DeleteResult : MpdCommandResult()

    public class DeleteIdResult : MpdCommandResult()

    public class MoveResult : MpdCommandResult()

    public class MoveRangeResult : MpdCommandResult()

    public class MoveIdResult : MpdCommandResult()

    @Deprecated("Deprecated by MPD")
    public class PlaylistResult : MpdCommandResult()

    public class PlaylistFindResult : MpdCommandResult()

    public class PlaylistIdResult : MpdCommandResult()

    public class PlaylistInfoResult : MpdCommandResult()

    public class PlaylistSearchResult : MpdCommandResult()

    public class PlaylistChangesResult : MpdCommandResult()

    public class PlaylistChangesPosIdResult : MpdCommandResult()

    public class SetPriorityResult : MpdCommandResult()

    public class SetPriorityByIdResult : MpdCommandResult()

    public class RangeIdResult : MpdCommandResult()

    public class shuffleResult : MpdCommandResult()

    public class swapResult : MpdCommandResult()

    public class swapidResult : MpdCommandResult()

    public class addtagidResult : MpdCommandResult()

    public class cleartagidResult : MpdCommandResult()

    // endregion Queue Manipulation Commands

    // region Playlist Manipulation Commands

    public class ListPlaylistResult : MpdCommandResult()

    public class ListPlaylistInfoResult : MpdCommandResult()

    public class ListPlaylistsResult : MpdCommandResult()

    public class LoadResult : MpdCommandResult()

    public class PlaylistAddResult : MpdCommandResult()

    public class PlaylistClearResult : MpdCommandResult()

    public class PlaylistDeleteResult : MpdCommandResult()

    public class PlaylistMoveResult : MpdCommandResult()

    public class RenamePlaylistResult : MpdCommandResult()

    public class RemovePlaylistResult : MpdCommandResult()

    public class SavePlaylistResult : MpdCommandResult()

    // endregion Playlist Manipulation Commands

    // region Database Manipulation Commands

    public class AlbumArtResult : MpdCommandResult()

    public class CountResult : MpdCommandResult()

    public class GetFingerPrintResult : MpdCommandResult()

    public class FindResult : MpdCommandResult()

    public class FindAddResult : MpdCommandResult()

    public class ListTagsResult : MpdCommandResult()

    public class ListAllResult : MpdCommandResult()

    public class ListAllInfoResult : MpdCommandResult()

    public class ListFilesResult : MpdCommandResult()

    public class LSInfoResult : MpdCommandResult()

    public class ReadCommentsResult : MpdCommandResult()

    public class SearchResult : MpdCommandResult()

    public class SearchAddToQueueResult : MpdCommandResult()

    public class SearchAddToPlaylistResult : MpdCommandResult()

    public class UpdateResult : MpdCommandResult()

    public class RescanResult : MpdCommandResult()

    // endregion Database Manipulation Commands

    // region Mounts Commands

    public class MountResult : MpdCommandResult()

    public class UnmountResult : MpdCommandResult()

    public class ListMountsResult : MpdCommandResult()

    public class ListNeighborsResult : MpdCommandResult()

    // endregion Mounts Commands

    // region Stickers Commands

    public class StickerGetResult : MpdCommandResult()

    public class StickerSetResult : MpdCommandResult()

    public class StickerDeleteResult : MpdCommandResult()

    public class StickerListResult : MpdCommandResult()

    public class StickerFindResult : MpdCommandResult()

    public class stickerfindResult : MpdCommandResult()

    // endregion Stickers Commands

    // region Connection Manipulation Commands

    public class CloseResult : MpdCommandResult()

    public class KillResult : MpdCommandResult()

    public class PasswordResult : MpdCommandResult()

    public class PingResult : MpdCommandResult()

    public class TagTypesResult : MpdCommandResult()

    public class TagTypesDisableResult : MpdCommandResult()

    public class TagTypesEnableResult : MpdCommandResult()

    public class TagTypesClearResult : MpdCommandResult()

    public class TagTypesAllResult : MpdCommandResult()

    // endregion Connection Manipulation Commands

    // region Partition Commands

    public class SwitchPartitionResult : MpdCommandResult()

    public class ListPartitionsResult : MpdCommandResult()

    public class NewPartitionResult : MpdCommandResult()

    // endregion Partition Commands

    // region Audio Output Device Commands

    public class DisableOutputResult : MpdCommandResult()

    public class EnableOutputResult : MpdCommandResult()

    public class ToggleOutputResult : MpdCommandResult()

    public class OutputsResult : MpdCommandResult()

    public class OutputSetResult : MpdCommandResult()

    // endregion Audio Output Device Commands

    // region Reflective Commands

    public class ConfigResult : MpdCommandResult()

    public class CommandsResult : MpdCommandResult()

    public class DecodersResult : MpdCommandResult()

    public class NotCommandsResult : MpdCommandResult()

    public class UrlHandlersResult : MpdCommandResult()

    // endregion Reflective Commands

    // region Client-to-Client Commands

    public class SubscribeResult : MpdCommandResult()

    public class UnsubscribeResult : MpdCommandResult()

    public class ChannelsResult : MpdCommandResult()

    public class ReadMessagesResult : MpdCommandResult()

    public class SendMessageResult : MpdCommandResult()

    // endregion Client-to-Client Commands
}
