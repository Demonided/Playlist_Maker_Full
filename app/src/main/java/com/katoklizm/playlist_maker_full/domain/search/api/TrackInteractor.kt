package com.katoklizm.playlist_maker_full.domain.search.api

import com.katoklizm.playlist_maker_full.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

interface TrackInteractor {
    fun searchTrack(term: String): Flow<Pair<List<Track>?, String?>>
    fun readSearchHistory(): ArrayList<Track>
    fun addTrackToSearchHistory(track: Track)
    fun clearSearchHistory()
}