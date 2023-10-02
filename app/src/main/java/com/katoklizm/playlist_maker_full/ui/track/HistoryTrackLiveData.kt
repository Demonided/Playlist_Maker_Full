package com.katoklizm.playlist_maker_full.ui.track

import androidx.lifecycle.LiveData
import com.katoklizm.playlist_maker_full.domain.model.Track

class HistoryTrackLiveData : LiveData<List<Track>>() {
    private val trackHistoryList = mutableListOf<Track>()

    fun addTrack(track: Track) {
        if (trackHistoryList.size >= 10) {
            trackHistoryList.removeAt(trackHistoryList.size - 1)
        }
        trackHistoryList.add(0, track)
        value = trackHistoryList.toList()
    }
}