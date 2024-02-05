package com.katoklizm.playlist_maker_full.data.album.impl

import com.katoklizm.playlist_maker_full.data.converters.TrackAlbumDbConverters.mapAlbumTrackPlaylistEntity
import com.katoklizm.playlist_maker_full.data.db.AppDatabase
import com.katoklizm.playlist_maker_full.domain.album.TrackAlbumPlaylistRepository
import com.katoklizm.playlist_maker_full.domain.album.model.TrackAlbumPlaylist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackAlbumPlaylistRepositoryImpl(
    private val appDatabase: AppDatabase
) : TrackAlbumPlaylistRepository {
    override suspend fun insertTrackAlbum(trackAlbum: TrackAlbumPlaylist) {
        val tracksAlbum = trackAlbum.mapAlbumTrackPlaylistEntity()
        appDatabase.trackAlbumPlaylistDao().insertTrackAlbum(tracksAlbum)
    }

    override suspend fun deleteTrackAlbum(trackId: Int) {
        appDatabase.trackAlbumPlaylistDao().deleteTrackAlbum(trackId)
    }

    override fun getAllTrackAlbum(): Flow<List<TrackAlbumPlaylist>> = flow {
        appDatabase.trackAlbumPlaylistDao().getAllTrackAlbum()
    }
}