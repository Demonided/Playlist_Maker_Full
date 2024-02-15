package com.katoklizm.playlist_maker_full.domain.albuminfo

import com.katoklizm.playlist_maker_full.domain.album.model.AlbumPlaylist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface AlbumInfoRepository {

    fun setPlaylist(value: AlbumPlaylist?)

    fun getPlaylist(): StateFlow<AlbumPlaylist?>
}