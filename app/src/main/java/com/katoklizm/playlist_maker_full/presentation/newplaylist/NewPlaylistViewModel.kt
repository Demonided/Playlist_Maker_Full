package com.katoklizm.playlist_maker_full.presentation.newplaylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.katoklizm.playlist_maker_full.domain.album.AlbumPlaylistInteractor
import com.katoklizm.playlist_maker_full.domain.album.model.AlbumPlaylist
import com.katoklizm.playlist_maker_full.domain.albuminfo.AlbumInfoInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class NewPlaylistViewModel(
    private val albumPlaylistInteractor: AlbumPlaylistInteractor,
    private val albumInfoInteractor: AlbumInfoInteractor
): ViewModel() {

    private val _stateAlbumPlaylist = MutableLiveData<AlbumPlaylist?>(null)
    val stateAlbumPlaylist: LiveData<AlbumPlaylist?> = _stateAlbumPlaylist

    init {
        fillData()
    }

    fun fillData() {
        viewModelScope.launch {
            albumInfoInteractor.getPlaylist()
                .collect {
                    if (_stateAlbumPlaylist.value == null) {
                        _stateAlbumPlaylist.postValue(it)
                    }
                }
        }
    }

    fun updateStateAlbum(album: AlbumPlaylist?) {
        _stateAlbumPlaylist.postValue(album)
    }

    fun onPlaylistClicked(album: AlbumPlaylist) {
        albumInfoInteractor.setPlaylist(album)
        _stateAlbumPlaylist.postValue(album)
    }

    suspend fun addAlbumPlaylist(album: AlbumPlaylist) {
        albumPlaylistInteractor.addAlbumPlaylist(album)
    }

    fun updateAlbum(album: AlbumPlaylist) {
        viewModelScope.launch(Dispatchers.IO) {
            albumPlaylistInteractor.updateAlbumPlaylist(album)
            onPlaylistClicked(album)
        }
    }
}