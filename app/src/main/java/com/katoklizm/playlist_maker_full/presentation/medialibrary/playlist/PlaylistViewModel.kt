package com.katoklizm.playlist_maker_full.presentation.medialibrary.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.katoklizm.playlist_maker_full.domain.album.AlbumPlaylistInteractor
import com.katoklizm.playlist_maker_full.domain.album.model.AlbumPlaylist
import com.katoklizm.playlist_maker_full.domain.albuminfo.AlbumInfoInteractor
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val albumPlaylistInteractor: AlbumPlaylistInteractor,
    private val albumInfoInteractor: AlbumInfoInteractor
): ViewModel() {

    private val _playlistState = MutableLiveData<PlaylistState>()

    fun playlistState(): LiveData<PlaylistState> = _playlistState

    fun onPlaylistClicked(item: AlbumPlaylist?) {
        albumInfoInteractor.setPlaylist(item)
    }

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