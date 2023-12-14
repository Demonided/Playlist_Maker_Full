package com.katoklizm.playlist_maker_full.domain.search.api

import com.katoklizm.playlist_maker_full.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

interface TrackInteractor {

//    fun searchTrack(term: String, consumer: TrackConsumer)

    fun searchTrack(term: String): Flow<Pair<List<Track>?, String?>>
    fun readSearchHistory(): ArrayList<Track>
    fun addTrackToSearchHistory(track: Track)
    fun clearSearchHistory()

//    interface TrackConsumer {
//        fun consume(foundTrack: List<Track>?, errorMessage: String?)
//    }
}