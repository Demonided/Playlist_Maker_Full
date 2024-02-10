package com.katoklizm.playlist_maker_full.presentation.medialibrary.playlist

import com.katoklizm.playlist_maker_full.domain.album.model.AlbumPlaylist

interface PlaylistState {
    object Loading : PlaylistState

    object EmptyList : PlaylistState

    data class Content(
        val album: List<AlbumPlaylist>
    ) : PlaylistState
}