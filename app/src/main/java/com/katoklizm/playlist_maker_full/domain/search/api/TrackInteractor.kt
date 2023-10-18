package com.katoklizm.playlist_maker_full.domain.search.api

import com.katoklizm.playlist_maker_full.domain.search.model.Track

interface TrackInteractor {

    fun searchTrack(term: String, consumer: TrackConsumer)

    interface TrackConsumer{
        fun consume(foundTrack: List<Track>?, errorMessage: String?)
    }
    fun readSearchHistory(): ArrayList<Track>
    fun addTrackToSearchHistory(track: Track)
    fun clearSearchHistory()
}