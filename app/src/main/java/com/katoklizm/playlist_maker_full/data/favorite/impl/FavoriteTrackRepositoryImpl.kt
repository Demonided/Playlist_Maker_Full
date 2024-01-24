package com.katoklizm.playlist_maker_full.data.favorite.impl

import com.katoklizm.playlist_maker_full.data.converters.TrackDbConverters
import com.katoklizm.playlist_maker_full.data.db.AppDatabase
import com.katoklizm.playlist_maker_full.domain.favorite.FavoriteTrackRepository
import com.katoklizm.playlist_maker_full.domain.search.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoriteTrackRepositoryImpl(
    val appDatabase: AppDatabase,
    val trackDbConverters: TrackDbConverters
) : FavoriteTrackRepository {
    override suspend fun addTrack(track: Track) {
        val tracks = trackDbConverters.map(track)
        appDatabase.trackDao().insertTrack(track = tracks)
    }

    override suspend fun deleteTrack(track: Track) {
        appDatabase.trackDao().deleteTrack(track.trackId)
    }

    override fun gelAllTracksIsFavorite(): Flow<List<Track>> {
        return appDatabase.trackDao().getAllTrack().map { listTrack ->
            listTrack.map {
                trackDbConverters.map(it)
            }
        }
    }


}
