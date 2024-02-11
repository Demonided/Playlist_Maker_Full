package com.katoklizm.playlist_maker_full.domain.albuminfo

import com.katoklizm.playlist_maker_full.domain.album.model.AlbumPlaylist
import kotlinx.coroutines.flow.Flow

interface AlbumInfoRepository {

    fun getAlbum(): Flow<AlbumPlaylist>

    fun setAlbum(album: AlbumPlaylist)
}