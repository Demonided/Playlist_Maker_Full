package com.katoklizm.playlist_maker_full.domain.search.impl

import android.util.Log
import java.util.concurrent.Executors
import com.katoklizm.playlist_maker_full.domain.search.api.TrackInteractor
import com.katoklizm.playlist_maker_full.domain.search.api.TrackRepository
import com.katoklizm.playlist_maker_full.domain.search.model.Track
import com.katoklizm.playlist_maker_full.util.Resource

class TrackInteractorImpl(private val repository: TrackRepository) : TrackInteractor {

    private val executor = Executors.newCachedThreadPool()
    override fun searchTrack(term: String, consumer: TrackInteractor.TrackConsumer) {
        executor.execute {
            when(val resource = repository.searchTrack(term)) {
                is Resource.Success -> { consumer.consume(resource.data, null)}
                is Resource.Error ->{ consumer.consume(null, resource.message)}
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