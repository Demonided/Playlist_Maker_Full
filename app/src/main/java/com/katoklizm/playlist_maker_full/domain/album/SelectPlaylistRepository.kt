package com.katoklizm.playlist_maker_full.domain.album

import com.katoklizm.playlist_maker_full.domain.album.model.AlbumPlaylist
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SelectPlaylistRepository {

    private val playListFlow: MutableStateFlow<AlbumPlaylist?> = MutableStateFlow(null)

    fun setPlaylist(value: AlbumPlaylist?) {
        playListFlow.value = value
    }

    fun getPlaylist(): StateFlow<AlbumPlaylist?> =  playListFlow
}