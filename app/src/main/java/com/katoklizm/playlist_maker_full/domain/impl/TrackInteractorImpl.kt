package com.katoklizm.playlist_maker_full.domain.impl

import java.util.concurrent.Executors
import com.katoklizm.playlist_maker_full.domain.api.TrackInteractor
import com.katoklizm.playlist_maker_full.domain.api.TrackRepository

class TrackInteractorImpl(private val repository: TrackRepository) : TrackInteractor {

    private val executor = Executors.newCachedThreadPool()
    override fun searchTrack(term: String, consumer: TrackInteractor.TrackConsumer) {
        executor.execute {
            consumer.consume(repository.searchTrack(term = term))
        }
    }
}