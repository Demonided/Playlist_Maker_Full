package com.katoklizm.playlist_maker_full.data.album.impl

import com.katoklizm.playlist_maker_full.domain.album.model.AlbumPlaylist
import com.katoklizm.playlist_maker_full.domain.albuminfo.AlbumInfoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AlbumInfoRepositoryImpl(): AlbumInfoRepository {

    private val playListFlow: MutableStateFlow<AlbumPlaylist?> = MutableStateFlow(null)

    override fun setPlaylist(value: AlbumPlaylist?) {
        playListFlow.value = value
    }

    override fun getPlaylist(): StateFlow<AlbumPlaylist?> =  playListFlow
}