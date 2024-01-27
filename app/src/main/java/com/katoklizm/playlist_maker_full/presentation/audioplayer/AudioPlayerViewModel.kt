package com.katoklizm.playlist_maker_full.presentation.audioplayer

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.katoklizm.playlist_maker_full.domain.favorite.FavoriteTrackInteractor
import com.katoklizm.playlist_maker_full.domain.player.PlayerInteractor
import com.katoklizm.playlist_maker_full.domain.search.model.Track
import com.katoklizm.playlist_maker_full.presentation.medialibrary.playlist.PlayerScreenState
import com.katoklizm.playlist_maker_full.presentation.medialibrary.playlist.PlayerStatus
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AudioPlayerViewModel(
    private val context: Context,
    private val playerInteractor: PlayerInteractor,
    private val favoriteInteractor: FavoriteTrackInteractor
) : ViewModel() {

    private var timerJob: Job? = null

    private val _timerState = MutableLiveData(0)
    val timerState: LiveData<Int> = _timerState

    private val _playerState = MutableLiveData<PlayerScreenState>()
    val playerState: LiveData<PlayerScreenState> = _playerState

    private var favoritesTrackIds = listOf<Int>()

    init {
        subscribe()
    }

    fun initState(track: Track) {
        val initState = PlayerScreenState.Ready(
            track.copy(isFavorite = favoritesTrackIds.contains(track.trackId)),
            PlayerStatus.DEFAULT
        )
        _playerState.postValue(initState)
    }

    private fun subscribe() {
        viewModelScope.launch {
            favoriteInteractor.getTrackFavorite().collect { favoriteTracks ->
                favoritesTrackIds = favoriteTracks.map { it.trackId }
                _playerState.value?.getCurrentIfReady()?.let { currentState ->
                    val track = currentState.track.copy(
                        isFavorite = favoritesTrackIds.contains(currentState.track.trackId)
                    )
                    _playerState.postValue(currentState.copy(track = track))
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        playerInteractor.release()
        timerJob?.cancel()
    }

    fun startPlayer() {
        playerInteractor.startPlayer()
        _playerState.value?.getCurrentIfReady()?.let { currentState ->
            _playerState.postValue(currentState.copy(playerStatus = PlayerStatus.PLAYING))

            timerJob = viewModelScope.launch {
                while (true) {
                    delay(PLAYBACK_DELAY_MILLIS)
                    _timerState.postValue(playerInteractor.currentPosition())
                }
            }
        }
    }

    fun pausePlayer() {
        playerInteractor.pausePlayer()
        _playerState.value?.getCurrentIfReady()?.let { currentState ->
            timerJob?.cancel()
            _playerState.postValue(currentState.copy(playerStatus = PlayerStatus.PAUSED))
        }
    }

    fun preparePlayer(track: Track?, completion: () -> Unit) {
        playerInteractor.preparePlayer(track, completion,
            statusObserver = object : PlayerInteractor.StatusObserver {
                override fun onPrepared() {
                    _playerState.value?.getCurrentIfReady()?.let { currentState ->
                        _playerState.postValue(currentState.copy(playerStatus = PlayerStatus.PREPARED))
                    }
                }

                override fun onCompletion() {
                    _playerState.value?.getCurrentIfReady()?.let { currentState ->
                        timerJob?.cancel()
                        _timerState.postValue(0)
                        _playerState.postValue(currentState.copy(playerStatus = PlayerStatus.PREPARED))
                    }
                }
            })
    }

    fun playbackControl() {
        when (playerStateListener()) {
            PlayerStatus.PLAYING -> {
                pausePlayer()
            }

            PlayerStatus.PREPARED -> {
                startPlayer()
            }

            PlayerStatus.PAUSED -> {
                startPlayer()
            }

            PlayerStatus.DEFAULT -> {

            }
        }
    }

    fun playerStateListener(): PlayerStatus {
        return (_playerState.value as? PlayerScreenState.Ready)?.playerStatus
            ?: PlayerStatus.DEFAULT
    }

    fun release() {
        playerInteractor.release()
    }

    fun onFavoriteClicked() {
        _playerState.value?.getCurrentIfReady()?.let { currentState ->
            viewModelScope.launch {
                if (currentState.track.isFavorite) {
                    favoriteInteractor.deleteTrack(currentState.track.trackId)
                    Toast.makeText(context, "Вы удалили трек из избранного", Toast.LENGTH_LONG).show()
                } else {
                    favoriteInteractor.addTrack(currentState.track)
                    Toast.makeText(context, "Вы добавили трек в избранное", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun PlayerScreenState.getCurrentIfReady(): PlayerScreenState.Ready? =
        if (this is PlayerScreenState.Ready) this else null

    companion object {
        const val PLAYBACK_DELAY_MILLIS = 300L
    }
}