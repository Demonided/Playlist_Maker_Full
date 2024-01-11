package com.katoklizm.playlist_maker_full.presentation.audioplayer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.katoklizm.playlist_maker_full.domain.player.PlayerState
import com.katoklizm.playlist_maker_full.domain.search.model.Track
import com.katoklizm.playlist_maker_full.domain.player.PlayerInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AudioPlayerViewModel(
    private val playerInteractor: PlayerInteractor
) : ViewModel() {

    private var timerJob: Job? = null

    private val _statePlayer = MutableLiveData(PlayerState.STATE_DEFAULT)
    val statePlayer: LiveData<PlayerState> = _statePlayer

    private val _timerState = MutableLiveData(0)
    val timerState: LiveData<Int> = _timerState

    fun startPlayer() {
        playerInteractor.startPlayer()
        _statePlayer.value = PlayerState.STATE_PLAYING

        timerJob = viewModelScope.launch {
            while (_statePlayer.value ==  PlayerState.STATE_PLAYING) {
                delay(PLAYBACK_DELAY_MILLIS)
                _timerState.postValue(playerInteractor.currentPosition())
            }
        }
    }

    fun pausePlayer() {
        playerInteractor.pausePlayer()
        _statePlayer.value = PlayerState.STATE_PAUSED

        timerJob?.cancel()
    }

    fun preparePlayer(track: Track?, completion: () -> Unit) {
        playerInteractor.preparePlayer(track, completion,
            statusObserver = object : PlayerInteractor.StatusObserver {
                override fun onPrepared() {
                    _statePlayer.postValue(PlayerState.STATE_PREPARED)
                }

                override fun onCompletion() {
                    _statePlayer.postValue(PlayerState.STATE_PREPARED)
                    timerJob?.cancel()
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

            }
        }
    }

    fun playerStateListener(): PlayerState {
        return statePlayer.value ?: PlayerState.STATE_DEFAULT
    }

    fun release() {
        playerInteractor.release()
    }

    override fun onCleared() {
        super.onCleared()
        playerInteractor.release()
        timerJob?.cancel()
    }

    companion object {
        const val PLAYBACK_DELAY_MILLIS = 300L
    }
}