package com.katoklizm.playlist_maker_full.domain.search.api

import com.katoklizm.playlist_maker_full.domain.search.model.Track
import com.katoklizm.playlist_maker_full.util.Resource

    interface TrackRepository {
        fun searchTrack(term: String): Resource<List<Track>>

        fun readSearchHistory(): ArrayList<Track>

        fun addTrackToSearchHistory(track: Track)

        fun clearSearchHistory()
    }