package com.katoklizm.playlist_maker_full.ui.audioplayer.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.katoklizm.playlist_maker_full.domain.player.PlayerState
import com.katoklizm.playlist_maker_full.domain.search.model.Track
import com.katoklizm.playlist_maker_full.domain.player.PlayerInteractor
import com.katoklizm.playlist_maker_full.util.Creator

class AudioPlayerViewModel(
    private val playerInteractor: PlayerInteractor
) : ViewModel() {

    private val _statePlayer = MutableLiveData<PlayerState>()
    val statePlayer: LiveData<PlayerState> = _statePlayer

    fun startPlayer() {
        playerInteractor.startPlayer()
        _statePlayer.value = PlayerState.STATE_PLAYING
    }

    fun pausePlayer() {
        playerInteractor.pausePlayer()
        _statePlayer.value = PlayerState.STATE_PAUSED
    }

    fun preparePlayer(track: Track?, completion: () -> Unit) {
        playerInteractor.preparePlayer(track)
        _statePlayer.value = PlayerState.STATE_PREPARED
    }

    fun playbackControl() {
        when (playerStateListener()) {
            PlayerState.STATE_PLAYING -> {
                pausePlayer()
                Log.d("StatePlayer", "Статус 1 во вьюМоделе")
            }

            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> {
                startPlayer()
                Log.d("StatePlayer", "Статус 2 во вьюМоделе")
            }

            else -> {
                pausePlayer()
                Log.d("StatePlayer", "Статус 3 во вьюМоделе")
            }
        }
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
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return AudioPlayerViewModel(
                        Creator.providePlayerInteractor()
                    ) as T
                }
            }
    }
}