package com.katoklizm.playlist_maker_full.ui.audioplayer

import androidx.lifecycle.ViewModel
import com.katoklizm.playlist_maker_full.data.search.dto.TrackDto
import com.katoklizm.playlist_maker_full.domain.search.model.Track
import com.katoklizm.playlist_maker_full.domain.player.PlayerInteractor

class AudioPlayerViewModel(
    private val playerInteractor: PlayerInteractor
) : ViewModel() {
    fun startPlayer() {
        playerInteractor.startPlayer()
    }

    fun pausePlayer() {
        playerInteractor.pausePlayer()
    }

    fun preparePlayer(track: Track?) {
        playerInteractor.preparePlayer(track)
    }

    fun startTimer() {
        playerInteractor.startTimer()
    }

    fun playbackControl() {
        playerInteractor.playbackControl()
    }
}