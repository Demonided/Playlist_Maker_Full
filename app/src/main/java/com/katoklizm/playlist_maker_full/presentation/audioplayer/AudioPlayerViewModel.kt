package com.katoklizm.playlist_maker_full.presentation.audioplayer

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.katoklizm.playlist_maker_full.domain.album.AlbumPlaylistInteractor
import com.katoklizm.playlist_maker_full.domain.album.TrackAlbumPlaylistInteractor
import com.katoklizm.playlist_maker_full.domain.album.model.AlbumPlaylist
import com.katoklizm.playlist_maker_full.domain.album.model.TrackAlbumPlaylist
import com.katoklizm.playlist_maker_full.domain.favorite.FavoriteTrackInteractor
import com.katoklizm.playlist_maker_full.domain.player.PlayerInteractor
import com.katoklizm.playlist_maker_full.domain.search.model.Track
import com.katoklizm.playlist_maker_full.presentation.medialibrary.playlist.PlayerScreenState
import com.katoklizm.playlist_maker_full.presentation.medialibrary.playlist.PlayerStateAlbum
import com.katoklizm.playlist_maker_full.presentation.medialibrary.playlist.PlayerStatus
import com.katoklizm.playlist_maker_full.presentation.medialibrary.playlist.PlaylistState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.reflect.Type

class AudioPlayerViewModel(
    private val context: Context,
    private val playerInteractor: PlayerInteractor,
    private val favoriteInteractor: FavoriteTrackInteractor,
    private val albumPlaylistInteractor: AlbumPlaylistInteractor,
    private val trackAlbumPlaylistInteractor: TrackAlbumPlaylistInteractor
) : ViewModel() {

    private var timerJob: Job? = null

    private val _timerState = MutableLiveData(0)
    val timerState: LiveData<Int> = _timerState

    private val _playerState = MutableLiveData<PlayerScreenState>()
    val playerState: LiveData<PlayerScreenState> = _playerState

    private val _albumState = MutableLiveData<PlayerStateAlbum>()
    val albumState: LiveData<PlayerStateAlbum> = _albumState

    private val _albumTrackPlaylist = MutableLiveData<List<TrackAlbumPlaylist>>()
    val albumTrackPlaylist: LiveData<List<TrackAlbumPlaylist>> = _albumTrackPlaylist

    private val _messageTrack = MutableLiveData<String>()
    val messageTrack: LiveData<String> = _messageTrack

    private var favoritesTrackIds = listOf<Int>()

    init {
        subscribe()
        addAlbumPlaylist()
        getAllTrackAlbumPlaylist()
    }

    fun getAllTrackAlbumPlaylist() {
        viewModelScope.launch {
            trackAlbumPlaylistInteractor.getAllTrackAlbum().collect() { trackAlbumPlaylist ->
                _albumTrackPlaylist.postValue(trackAlbumPlaylist)
            }
        }
    }

    fun addAlbumPlaylist() {
        viewModelScope.launch {
            albumPlaylistInteractor.getAllAlbumPlaylist()
                .collect { album ->
                    processResult(album)
                }
        }
    }

    private fun processResult(album: List<AlbumPlaylist>) {
        renderState(PlayerStateAlbum.Content(album))
    }

    private fun renderState(state: PlayerStateAlbum) {
        _albumState.postValue(state)
    }

    fun onPlaylistClicked(playlist: AlbumPlaylist, track: Track, calback: (Boolean) -> Unit){
        val listType: Type = object : TypeToken<ArrayList<Track?>?>() {}.type
        var tracks: ArrayList<Track>? = Gson().fromJson(playlist.track, listType)
        if (tracks?.contains(track) == true) {
            _messageTrack.postValue("Трек уже добавлен в плейлист ${playlist.name}")
            calback(false)
        }
        else {
            if (tracks != null) {
                tracks.add(track)
            } else {
                tracks = ArrayList<Track>().apply { add(track) }
            }
            val newString = Gson().toJson(tracks)
            val newPlaylist = AlbumPlaylist(playlist.id, playlist.name, playlist.description, playlist.image,
                playlist.quantity.plus(1), newString)
            viewModelScope.launch(Dispatchers.IO) {
                albumPlaylistInteractor.updateAlbumPlaylist(newPlaylist)
                addAlbumPlaylist()
                _messageTrack.postValue("Добавлено в плейлист ${playlist.name}")
                calback(true)
            }
        }
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
                    _playerState.postValue(
                        PlayerScreenState.Ready(
                            currentState.track.copy(isFavorite = false),
                            currentState.playerStatus
                        )
                    )
                    Toast.makeText(context, "Вы удалили трек из избранного", Toast.LENGTH_LONG)
                        .show()
                } else {
                    favoriteInteractor.addTrack(currentState.track)
                    _playerState.postValue(
                        PlayerScreenState.Ready(
                            currentState.track.copy(isFavorite = true),
                            currentState.playerStatus
                        )
                    )
                    Toast.makeText(context, "Вы добавили трек в избранное", Toast.LENGTH_LONG)
                        .show()
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