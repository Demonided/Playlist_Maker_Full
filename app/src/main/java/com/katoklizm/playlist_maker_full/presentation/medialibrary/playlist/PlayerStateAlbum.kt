package com.katoklizm.playlist_maker_full.presentation.medialibrary.playlist

import com.katoklizm.playlist_maker_full.domain.album.model.AlbumPlaylist

sealed interface PlayerStateAlbum {
    data class Content(
        val album: List<AlbumPlaylist>
    ) : PlayerStateAlbum
}