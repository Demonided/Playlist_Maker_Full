package com.katoklizm.playlist_maker_full.domain.search.api

import com.katoklizm.playlist_maker_full.domain.search.model.Track

interface TrackInteractor {

    fun searchTrack(term: String, consumer: TrackConsumer)
    fun readSearchHistory(): ArrayList<Track>
    fun addTrackToSearchHistory(track: Track)
    fun clearSearchHistory()

    interface TrackConsumer {
        fun consume(foundTrack: List<Track>?, errorMessage: String?)
    }
}