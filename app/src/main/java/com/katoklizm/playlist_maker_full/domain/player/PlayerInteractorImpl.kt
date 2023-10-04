package com.katoklizm.playlist_maker_full.domain.player

import com.katoklizm.playlist_maker_full.data.dto.TrackDto
import com.katoklizm.playlist_maker_full.data.player.PlayerState
import com.katoklizm.playlist_maker_full.data.player.TimerIsRunning
import com.katoklizm.playlist_maker_full.domain.model.Track
import com.katoklizm.playlist_maker_full.util.Creator

class PlayerInteractorImpl : PlayerInteractor {
    val repository = Creator.providePlayerRepository()

    override fun startPlayer() {
        return repository.startPlayer()
    }

    override fun pausePlayer() {
        return repository.pausePlayer()
    }

    override fun preparePlayer(track: Track?) {
        return repository.preparePlayer(track)
    }

    override fun startTimer() {
        return repository.startTimer()
    }

    override fun playbackControl() {
        return repository.playbackControl()
    }

    override fun playerStateListener(): PlayerState {
        return repository.playerStateReporter()
    }

}