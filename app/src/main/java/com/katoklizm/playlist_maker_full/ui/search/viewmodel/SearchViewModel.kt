package com.katoklizm.playlist_maker_full.ui.search.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.katoklizm.playlist_maker_full.util.Creator
import com.katoklizm.playlist_maker_full.domain.search.api.TrackInteractor
import com.katoklizm.playlist_maker_full.domain.search.model.Track
import com.katoklizm.playlist_maker_full.domain.player.SearchState

class SearchViewModel(
    application: Application
) : AndroidViewModel(application) {

    companion object {
        fun getModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchViewModel(this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application)
            }
        }
    }

    private val trackInteractor = Creator.provideTrackInteractor(getApplication())

    private val stateLiveData = MutableLiveData<SearchState>()
    fun observeState(): LiveData<SearchState> = stateLiveData

    private fun renderState(state: SearchState) {
        stateLiveData.postValue(state)
    }

    fun onTextChanged(searchText: String?) {
        if (searchText.isNullOrEmpty()) {
            if (trackInteractor.readSearchHistory().isNotEmpty()) renderState(
                SearchState.ContentListSaveTrack(
                    trackInteractor.readSearchHistory()
                )
            ) else {
                renderState(SearchState.Empty(""))
            }
        }
    }


    fun onFocusChanged(hasFocus: Boolean, searchText: String) {
        if (hasFocus && searchText.isEmpty()) {
            renderState(SearchState.ContentListSaveTrack(trackInteractor.readSearchHistory()))
        }
    }

    fun clearSearchHistory() {
        trackInteractor.clearSearchHistory()
        renderState(SearchState.Empty("Пустой список"))
    }

    fun refreshSearchButton(searchText: String) {
        searchRequest(searchText)

    }

    fun onTrackPresent(track: Track) {
        trackInteractor.addTrackToSearchHistory(track)
    }

    fun searchRequest(searchText: String) {
        if (searchText.isNotEmpty()) {
            renderState(SearchState.Loading)
            trackInteractor.searchTrack(
                searchText, object : TrackInteractor.TrackConsumer {
                    override fun consume(foundTrack: List<Track>?, errorMessage: String?) {
                        if (foundTrack != null) {
                            if (foundTrack.isNotEmpty()) {
                                renderState(SearchState.ContentListSearchTrack(foundTrack))
                            } else {
                                renderState(SearchState.Empty("ЫЫ"))
                            }
                        }

                        if (errorMessage != null) {
                            renderState(SearchState.Error(errorMessage))
                        }
                    }
                }
            )
        }
    }
}