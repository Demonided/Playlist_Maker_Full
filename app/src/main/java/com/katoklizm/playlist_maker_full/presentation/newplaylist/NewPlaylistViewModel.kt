package com.katoklizm.playlist_maker_full.presentation.newplaylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.katoklizm.playlist_maker_full.domain.album.AlbumPlaylistInteractor
import com.katoklizm.playlist_maker_full.domain.album.model.AlbumPlaylist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewPlaylistViewModel(
    private val albumPlaylistInteractor: AlbumPlaylistInteractor
): ViewModel() {

    private val _stateAlbumPlaylist = MutableLiveData<AlbumPlaylist?>(null)
    val stateAlbumPlaylist: LiveData<AlbumPlaylist?> = _stateAlbumPlaylist

    fun updateStateAlbum(album: AlbumPlaylist?) {
        _stateAlbumPlaylist.postValue(album)
    }

    suspend fun addAlbumPlaylist(album: AlbumPlaylist) {
        albumPlaylistInteractor.addAlbumPlaylist(album)
    }

    fun updateAlbum(album: AlbumPlaylist) {
        viewModelScope.launch(Dispatchers.IO) {
            albumPlaylistInteractor.updateAlbumPlaylist(album)
        }
    }
}