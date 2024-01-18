package com.katoklizm.playlist_maker_full.domain.favorite.impl

import com.katoklizm.playlist_maker_full.domain.favorite.FavoriteTrackInteractor
import com.katoklizm.playlist_maker_full.domain.favorite.FavoriteTrackRepository
import com.katoklizm.playlist_maker_full.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

class FavoriteTrackInteractorImpl(val trackRepository: FavoriteTrackRepository): FavoriteTrackInteractor {

    override suspend fun updateTrackFavorite(track: Track) {
        trackRepository.updateTrackFavorite(track)
    }

    override fun getTrackFavorite(): Flow<List<Track>> {
        return trackRepository.getTrackFavorite()
    }
}