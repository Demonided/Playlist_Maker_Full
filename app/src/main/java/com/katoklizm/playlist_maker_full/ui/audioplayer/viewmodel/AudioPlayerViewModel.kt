package com.katoklizm.playlist_maker_full.ui.audioplayer.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.katoklizm.playlist_maker_full.domain.player.PlayerState
import com.katoklizm.playlist_maker_full.domain.search.model.Track
import com.katoklizm.playlist_maker_full.domain.player.PlayerInteractor

class AudioPlayerViewModel(
    private val playerInteractor: PlayerInteractor
) : ViewModel() {

    private val _statePlayer = MutableLiveData(PlayerState.STATE_DEFAULT)
    val statePlayer: LiveData<PlayerState> = _statePlayer

    private val _timerState = MutableLiveData(0)
    val timerState: LiveData<Int> = _timerState

    private val handler = Handler(Looper.getMainLooper())
    private val runnable = object : Runnable {
        override fun run() {
            _timerState.postValue(
                playerInteractor.currentPosition()
            )
            handler.postDelayed(this, DELAY)
        }
    }

    fun startPlayer() {
        playerInteractor.startPlayer()
        _statePlayer.value = PlayerState.STATE_PLAYING

        handler.post(runnable)
    }

    fun pausePlayer() {
        playerInteractor.pausePlayer()
        _statePlayer.value = PlayerState.STATE_PAUSED
        handler.removeCallbacks(runnable)
    }

    fun preparePlayer(track: Track?, completion: () -> Unit) {
        playerInteractor.preparePlayer(track, completion,
            statusObserver = object : PlayerInteractor.StatusObserver {
                override fun onPrepared() {
                    _statePlayer.postValue(PlayerState.STATE_PREPARED)
                }

                override fun onCompletion() {
                    _statePlayer.postValue(PlayerState.STATE_PREPARED)
                    handler.removeCallbacks(runnable)
                    _timerState.postValue(0)
                }

            })
    }

    fun playbackControl() {
        when (playerStateListener()) {
            PlayerState.STATE_PLAYING -> {
                pausePlayer()
            }

            PlayerState.STATE_PREPARED -> {
                startPlayer()
            }

            PlayerState.STATE_PAUSED -> {
                startPlayer()
            }

            PlayerState.STATE_DEFAULT -> {
                pausePlayer()
            }
        }
    }

    fun playerStateListener(): PlayerState {
        return statePlayer.value ?: PlayerState.STATE_DEFAULT
    }

    fun release() {
        playerInteractor.release()
    }

    companion object {
        const val DELAY = 300L
//        fun getViewModelFactory(): ViewModelProvider.Factory =
//            object : ViewModelProvider.Factory {
//                @Suppress("UNCHECKED_CAST")
//                override fun <T : ViewModel> create(modelClass: Class<T>): T {
//                    return AudioPlayerViewModel(
//                        Creator.providePlayerInteractor()
//                    ) as T
//                }
//            }
    }
}