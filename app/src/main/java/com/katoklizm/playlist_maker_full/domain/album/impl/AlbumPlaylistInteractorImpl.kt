package com.katoklizm.playlist_maker_full.domain.album.impl

import com.katoklizm.playlist_maker_full.domain.album.AlbumPlaylistInteractor
import com.katoklizm.playlist_maker_full.domain.album.AlbumPlaylistRepository
import com.katoklizm.playlist_maker_full.domain.album.model.AlbumPlaylist
import kotlinx.coroutines.flow.Flow

class AlbumPlaylistInteractorImpl(
    private val albumPlaylistRepository: AlbumPlaylistRepository
) : AlbumPlaylistInteractor {
    override suspend fun addAlbumPlaylist(album: AlbumPlaylist) {
        albumPlaylistRepository.addAlbumPlaylist(album)
    }

    override suspend fun deleteAlbumPlaylist(id: Int) {
        albumPlaylistRepository.deleteAlbumPlaylist(id)
    }

    override fun getAllAlbumPlaylist(): Flow<List<AlbumPlaylist>> {
        return albumPlaylistRepository.getAllAlbumPlaylist()
    }

    override suspend fun updateAlbumPlaylist(album: AlbumPlaylist) {
        albumPlaylistRepository.updateAlbumPlaylist(album)
    }
}