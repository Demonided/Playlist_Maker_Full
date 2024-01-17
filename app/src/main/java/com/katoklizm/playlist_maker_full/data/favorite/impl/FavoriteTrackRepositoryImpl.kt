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
    val dbConverters: TrackDbConverters
) : FavoriteTrackRepository {

    override suspend fun addTrackFavorite(track: Track) {

    }

    override suspend fun deleteTrackFavorite(track: Track) {

    }

    override fun getTrackFavorite(): Flow<List<Track>>  {
        return appDatabase.trackDao().getAllTrack().map { listTrack ->
            listTrack.map {
                dbConverters.map(it)
            }
        }
    }
}
