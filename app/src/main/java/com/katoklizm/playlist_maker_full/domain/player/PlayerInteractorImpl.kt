package com.katoklizm.playlist_maker_full.domain.player

import com.katoklizm.playlist_maker_full.domain.search.model.Track

class PlayerInteractorImpl(private val repository: PlayerRepository) : PlayerInteractor {

    override fun startPlayer() {
        return repository.startPlayer()
    }

    override fun pausePlayer() {
        return repository.pausePlayer()
    }

    override fun preparePlayer(
        track: Track?,
        completion: () -> Unit,
        statusObserver: PlayerInteractor.StatusObserver
    ) {
        return repository.preparePlayer(track, completion, statusObserver)
    }

    override fun release() {
        repository.release()
    }

    override fun currentPosition(): Int {
        return repository.currentPosition()
    }

}