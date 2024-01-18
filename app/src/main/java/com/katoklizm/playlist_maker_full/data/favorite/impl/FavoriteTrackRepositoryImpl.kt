package com.katoklizm.playlist_maker_full.data.favorite.impl

import com.katoklizm.playlist_maker_full.data.converters.TrackDbConverters
import com.katoklizm.playlist_maker_full.data.db.AppDatabase
import com.katoklizm.playlist_maker_full.domain.favorite.FavoriteTrackRepository
import com.katoklizm.playlist_maker_full.domain.search.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class FavoriteTrackRepositoryImpl(
    val appDatabase: AppDatabase,
    val trackDbConverters: TrackDbConverters
) : FavoriteTrackRepository {

    override suspend fun updateTrackFavorite(track: Track) {
        val trackFavorite = trackDbConverters.map(track)
            .copy(isFavorite = !track.isFavorite)

        if (track.isFavorite) {
            appDatabase.trackDao().deleteTrack(trackDbConverters.map(track))
            track.isFavorite = false
        } else {
            appDatabase.trackDao().insertTrack(track = trackFavorite)
            track.isFavorite = true
        }
    }

    override fun getTrackFavorite(): Flow<List<Track>>  {
        return appDatabase.trackDao().getAllTrack().map { listTrack ->
            listTrack.map {
                trackDbConverters.map(it)
            }
        }
    }
}
