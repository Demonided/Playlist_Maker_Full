package com.katoklizm.playlist_maker_full.domain.api

import com.katoklizm.playlist_maker_full.domain.model.Track

interface TrackInteractor {
    fun searchTrack(term: String, consumer: TrackConsumer)

    interface TrackConsumer{
        fun consume(foundTrack: List<Track>)
    }
}