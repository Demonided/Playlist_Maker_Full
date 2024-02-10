package com.katoklizm.playlist_maker_full.presentation.newplaylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.katoklizm.playlist_maker_full.domain.album.AlbumPlaylistInteractor
import com.katoklizm.playlist_maker_full.domain.album.model.AlbumPlaylist

class NewPlaylistViewModel(
    private val albumPlaylistInteractor: AlbumPlaylistInteractor
): ViewModel() {

    suspend fun addAlbumPlaylist(album: AlbumPlaylist) {
        albumPlaylistInteractor.addAlbumPlaylist(album)
    }
}