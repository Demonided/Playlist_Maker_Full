package com.katoklizm.playlist_maker_full.data.album.impl

import com.katoklizm.playlist_maker_full.data.converters.AlbumDbConverters.mapToAlbumPlaylist
import com.katoklizm.playlist_maker_full.data.converters.AlbumDbConverters.mapToAlbumPlaylistEntity
import com.katoklizm.playlist_maker_full.data.db.AppDatabase
import com.katoklizm.playlist_maker_full.domain.album.AlbumPlaylistRepository
import com.katoklizm.playlist_maker_full.domain.album.model.AlbumPlaylist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AlbumPlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
) : AlbumPlaylistRepository {
    override suspend fun addAlbumPlaylist(album: AlbumPlaylist) {
        val albums = album.mapToAlbumPlaylistEntity()
        appDatabase.albumPlaylistDao().insertAlbum(albums)
    }

    override suspend fun deleteAlbumPlaylist(id: Int) {
        appDatabase.albumPlaylistDao().deleteAlbumPlaylist(id)
    }

    override fun getAllAlbumPlaylist(): Flow<List<AlbumPlaylist>> = flow {
        val album = appDatabase.albumPlaylistDao().getAllAlbumPlaylistFlow()
        emit(album.mapToAlbumPlaylist())
    }

    override fun getAlbumPlaylist(albumId: Int): Flow<AlbumPlaylist> = flow {
        val album = appDatabase.albumPlaylistDao().getAlbumPlaylist(albumId)
        emit(album.mapToAlbumPlaylist())
    }

    override suspend fun updateAlbumPlaylist(album: AlbumPlaylist) {
        val albums = album.mapToAlbumPlaylistEntity()
        appDatabase.albumPlaylistDao().updateAlbumPlaylist(albums)
    }
}