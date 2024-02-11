package com.katoklizm.playlist_maker_full.domain.album

import com.katoklizm.playlist_maker_full.domain.album.model.AlbumPlaylist
import kotlinx.coroutines.flow.Flow

interface AlbumPlaylistInteractor {
    suspend fun addAlbumPlaylist(album: AlbumPlaylist)

    suspend fun deleteAlbumPlaylist(id: Int)

    fun getAllAlbumPlaylist(): Flow<List<AlbumPlaylist>>

    fun getAlbumPlaylist(albumId: Int): Flow<AlbumPlaylist>

    suspend fun updateAlbumPlaylist(album: AlbumPlaylist)
}