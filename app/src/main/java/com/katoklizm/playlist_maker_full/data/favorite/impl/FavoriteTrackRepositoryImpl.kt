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
        val traks = trackDbConverters.map(track)
        appDatabase.trackDao().insertTrack(traks)
    }

    override suspend fun deleteTrack(track: Track) {
        val tracks = trackDbConverters.map(track)
        appDatabase.trackDao().deleteTrack(tracks)
    }

    override fun getListTracks(): Flow<List<Track>> {
        return appDatabase.trackDao().getAllTrack().map {listTrack ->
            listTrack.map {
                trackDbConverters.map(it)
            }
        }
    }

    override suspend fun getTracksIds(): List<Int> {
        return appDatabase.trackDao().getTrackIds().map { trackEntity ->
            trackDbConverters.map(trackEntity).trackId
        }
    }

    override fun getTracksFlow(): Flow<List<Track>> {
        return appDatabase.trackDao().getAllTrack().map { listTrack ->
            listTrack
                .map {
                    trackDbConverters.map(it)
                }
        }
    }
}
