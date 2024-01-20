package com.katoklizm.playlist_maker_full.domain.search.impl

import android.util.Log
import java.util.concurrent.Executors
import com.katoklizm.playlist_maker_full.domain.search.api.TrackInteractor
import com.katoklizm.playlist_maker_full.domain.search.api.TrackRepository
import com.katoklizm.playlist_maker_full.domain.search.model.Track
import com.katoklizm.playlist_maker_full.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TrackInteractorImpl(private val repository: TrackRepository) : TrackInteractor {

    override fun searchTrack(term: String): Flow<Pair<List<Track>?, String?>> {
        return repository.searchTrack(term).map { result ->
            when(result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }
                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }
        }
    }

    override fun readSearchHistory(): ArrayList<Track> {
        return repository.readSearchHistory()
    }

    override fun addTrackToSearchHistory(track: Track) {
        repository.addTrackToSearchHistory(track)
    }

    override fun clearSearchHistory() {
        repository.clearSearchHistory()
    }
}