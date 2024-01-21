package com.katoklizm.playlist_maker_full.domain.favorite

import com.katoklizm.playlist_maker_full.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTrackInteractor {

    suspend fun addTrack(track: Track)

    suspend fun deleteTrack(trackId: String)

    fun getTrackFavorite(): Flow<List<Track>>

    suspend fun checkTrackIsFavorite(id: String): Boolean
}