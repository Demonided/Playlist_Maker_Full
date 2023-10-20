package com.katoklizm.playlist_maker_full.ui.audioplayer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.katoklizm.playlist_maker_full.data.player.PlayerState
import com.katoklizm.playlist_maker_full.domain.search.model.Track
import com.katoklizm.playlist_maker_full.domain.player.PlayerInteractor
import com.katoklizm.playlist_maker_full.util.Creator

class AudioPlayerViewModel(
    private val playerInteractor: PlayerInteractor
) : ViewModel() {
    fun startPlayer() {
        playerInteractor.startPlayer()
    }

    fun pausePlayer() {
        playerInteractor.pausePlayer()
    }

    fun preparePlayer(track: Track?, completion: () -> Unit) {
        playerInteractor.preparePlayer(track)
    }

    fun playbackControl() {
        playerInteractor.playbackControl()
    }

    fun transferTime(): String {
        return playerInteractor.transferTime()
    }

    fun playerStateListener(): PlayerState {
        return playerInteractor.playerStateListener()
    }

    fun release() {
        playerInteractor.release()
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                // 1
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return AudioPlayerViewModel(
                        Creator.providePlayerInteractor()
                    ) as T
                }
            }
    }
}