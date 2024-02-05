package com.katoklizm.playlist_maker_full.presentation.medialibrary.favorite_track

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.katoklizm.playlist_maker_full.domain.favorite.FavoriteTrackInteractor
import com.katoklizm.playlist_maker_full.domain.search.api.TrackInteractor
import com.katoklizm.playlist_maker_full.domain.search.model.Track
import kotlinx.coroutines.launch

class FavoriteTrackViewModel(
    private val favoriteTrackInteractor: FavoriteTrackInteractor,
) : ViewModel() {

    private val _favoriteTrackState = MutableLiveData<FavoriteTrackState>()
    fun favoriteTrackState(): LiveData<FavoriteTrackState> = _favoriteTrackState

    init {
        fillData()
    }

    fun fillData() {
        renderState(FavoriteTrackState.Loading)
        viewModelScope.launch {
            favoriteTrackInteractor.getTrackFavorite()
                .collect { track ->
                    processResult(track)
                }
        }
    }

    private fun processResult(track: List<Track>) {
        if (track.isEmpty()) {
            renderState(FavoriteTrackState.EmptyList)
        } else {
            renderState(FavoriteTrackState.Content(track))
        }
    }

    private fun renderState(state: FavoriteTrackState) {
        _favoriteTrackState.postValue(state)
    }
}