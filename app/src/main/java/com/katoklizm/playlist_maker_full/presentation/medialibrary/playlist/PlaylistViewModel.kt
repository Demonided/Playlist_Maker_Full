package com.katoklizm.playlist_maker_full.presentation.medialibrary.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.katoklizm.playlist_maker_full.domain.album.AlbumPlaylistInteractor
import com.katoklizm.playlist_maker_full.domain.album.model.AlbumPlaylist
import com.katoklizm.playlist_maker_full.domain.search.model.Track
import com.katoklizm.playlist_maker_full.presentation.medialibrary.favorite_track.FavoriteTrackState
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val albumPlaylistInteractor: AlbumPlaylistInteractor
): ViewModel() {

    private val _playlistState = MutableLiveData<PlaylistState>()

    fun playlistState(): LiveData<PlaylistState> = _playlistState

    init {
        fillData()
    }

    fun fillData() {
        renderState(PlaylistState.Loading)
        viewModelScope.launch {
            albumPlaylistInteractor.getAllAlbumPlaylist()
                .collect { album ->
                    processResult(album)
                }
        }
    }
    private fun processResult(album: List<AlbumPlaylist>) {
        if (album.isEmpty()) {
            renderState(PlaylistState.EmptyList)
        } else {
            renderState(PlaylistState.Content(album))
        }
    }
    private fun renderState(state: PlaylistState) {
        _playlistState.postValue(state)
    }
}