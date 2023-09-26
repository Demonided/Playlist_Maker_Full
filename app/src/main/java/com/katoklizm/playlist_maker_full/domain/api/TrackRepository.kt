package com.katoklizm.playlist_maker_full.domain.api

import com.katoklizm.playlist_maker_full.domain.model.Track

interface TrackRepository {
    fun searchTrack(term: String): List<Track>
}