package com.katoklizm.playlist_maker_full.presentation.medialibrary.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PlaylistViewModel: ViewModel() {

    private val _playlistState = MutableLiveData<PlaylistState>()

    fun playlistState(): LiveData<PlaylistState> = _playlistState
}