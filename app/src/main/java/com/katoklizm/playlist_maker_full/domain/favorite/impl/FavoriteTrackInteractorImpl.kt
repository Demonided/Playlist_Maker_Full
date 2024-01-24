package com.katoklizm.playlist_maker_full.domain.favorite.impl

import com.katoklizm.playlist_maker_full.domain.favorite.FavoriteTrackInteractor
import com.katoklizm.playlist_maker_full.domain.favorite.FavoriteTrackRepository
import com.katoklizm.playlist_maker_full.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

class FavoriteTrackInteractorImpl(
    private val trackRepository: FavoriteTrackRepository
): FavoriteTrackInteractor {

    override suspend fun addTrack(track: Track) {
        trackRepository.addTrack(track)
    }

    override suspend fun deleteTrack(trackId: Int) {
        trackRepository.deleteTrack(trackId)
    }

    override fun getTrackFavorite(): Flow<List<Track>> {
        return trackRepository.getListTracksFlow()
    }

    override suspend fun checkTrackIsFavorite(id: Int): Boolean {
            val tracksId = trackRepository.getListTrackIds()
            return tracksId.contains(id)
    }
}