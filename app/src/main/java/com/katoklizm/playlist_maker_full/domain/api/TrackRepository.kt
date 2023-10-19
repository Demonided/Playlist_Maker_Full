package com.katoklizm.playlist_maker_full.domain.api

import com.katoklizm.playlist_maker_full.domain.model.Track
import com.katoklizm.playlist_maker_full.util.Resource

interface TrackRepository {
    fun searchTrack(term: String): Resource<List<Track>>
}