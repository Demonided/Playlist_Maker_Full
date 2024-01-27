package com.katoklizm.playlist_maker_full.data.favorite.impl

import com.katoklizm.playlist_maker_full.data.converters.TrackDbConverters.mapToEntity
import com.katoklizm.playlist_maker_full.data.converters.TrackDbConverters.mapToTracks
import com.katoklizm.playlist_maker_full.data.db.AppDatabase
import com.katoklizm.playlist_maker_full.domain.favorite.FavoriteTrackRepository
import com.katoklizm.playlist_maker_full.domain.search.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow

class FavoriteTrackRepositoryImpl(
    private val appDatabase: AppDatabase,
) : FavoriteTrackRepository {

    private val trackFlow = MutableStateFlow<List<Track>>(listOf())
    override suspend fun addTrack(track: Track) {
        val trackEntity = track.mapToEntity()
        appDatabase.trackDao().insertTrack(trackEntity)
        syncTrackList()
    }

    override suspend fun deleteTrack(trackId: Int) {
        appDatabase.trackDao().deleteTrack(trackId)
        syncTrackList()
    }

    override suspend fun getListTracks(): List<Track> {
        return appDatabase.trackDao().getAllTrack().mapToTracks().apply {
            trackFlow.value = this
        }
    }

    override suspend fun getListTrackIds(): List<Int> {
        return appDatabase.trackDao().getAllFavoriteTrackIds()
    }

    private suspend fun syncTrackList() {
        trackFlow.value = appDatabase.trackDao().getAllTrack().mapToTracks()
    }

    override fun getListTracksFlow(): Flow<List<Track>> = flow {
        val track = appDatabase.trackDao().getAllTracksFlow()
        emit(track.mapToTracks())
    }
}
