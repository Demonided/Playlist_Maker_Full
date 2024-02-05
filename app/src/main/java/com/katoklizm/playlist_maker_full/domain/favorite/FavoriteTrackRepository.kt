package com.katoklizm.playlist_maker_full.domain.favorite

import com.katoklizm.playlist_maker_full.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTrackRepository {

    suspend fun addTrack(track: Track)
    suspend fun deleteTrack(trackId: Int)
    suspend fun getListTracks(): List<Track>
    suspend fun getListTrackIds(): List<Int>
    fun getListTracksFlow(): Flow<List<Track>>
}