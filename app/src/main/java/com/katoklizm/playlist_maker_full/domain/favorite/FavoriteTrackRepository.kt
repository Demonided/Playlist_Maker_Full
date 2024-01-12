package com.katoklizm.playlist_maker_full.domain.favorite

import com.katoklizm.playlist_maker_full.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTrackRepository {

    suspend fun addTrackFavorite(track: Track)

    suspend fun deleteTrackFavorite(track: Track)

    fun getTrackFavorite(): Flow<List<Track>>
}