package com.katoklizm.playlist_maker_full.presentation.medialibrary.favorite_track

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FavoriteTrackViewModel: ViewModel() {

    private val _favoriteTrackState = MutableLiveData<FavoriteTrackState>()

    fun favoriteTrackState(): LiveData<FavoriteTrackState> = _favoriteTrackState
}