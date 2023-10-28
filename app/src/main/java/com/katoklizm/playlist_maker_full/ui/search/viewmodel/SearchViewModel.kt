package com.katoklizm.playlist_maker_full.ui.search.viewmodel

import com.katoklizm.playlist_maker_full.domain.search.SearchState
import com.katoklizm.playlist_maker_full.domain.search.api.TrackInteractor
import com.katoklizm.playlist_maker_full.domain.search.model.Track
import com.katoklizm.playlist_maker_full.ui.common.BaseViewModel

class SearchViewModel(
    private val trackInteractor: TrackInteractor
) : BaseViewModel<SearchState>() {

    val trackList = ArrayList<Track>()
    val trackHistoryList = ArrayList<Track>()

    fun onTextChanged(searchText: String?) {
        if (searchText.isNullOrBlank()) {
            if (trackInteractor.readSearchHistory().isNotEmpty()) _state.postValue(
                SearchState.ContentListSaveTrack(
                    trackInteractor.readSearchHistory()
                )
            ) else {
                _state.postValue(SearchState.EmptyScreen)
            }
        }
    }

    fun onFocusChanged(hasFocus: Boolean, searchText: String) {
        if (hasFocus && searchText.isEmpty()) {
            if (trackInteractor.readSearchHistory().isNotEmpty()) {
                _state.postValue(SearchState.ContentListSaveTrack(trackInteractor.readSearchHistory()))
            } else {
                _state.postValue(SearchState.EmptyScreen)
            }
        }
    }

    fun clearSearchHistory() {
        trackInteractor.clearSearchHistory()
        _state.postValue(SearchState.EmptyScreen)
    }

    fun refreshSearchButton(searchText: String) {
        searchRequest(searchText)
    }

    fun onTrackPresent(track: Track) {
        trackInteractor.addTrackToSearchHistory(track)
    }

    fun searchRequest(searchText: String) {
        if (searchText.isNotEmpty()) {
            _state.postValue(SearchState.Loading)
            trackInteractor.searchTrack(
                searchText, object : TrackInteractor.TrackConsumer {
                    override fun consume(foundTrack: List<Track>?, errorMessage: String?) {
                        if (foundTrack != null) {
                            if (foundTrack.isNotEmpty()) {
                                _state.postValue(SearchState.ContentListSearchTrack(foundTrack))
                            } else {
                                _state.postValue(SearchState.Empty(""))
                            }
                        }

                        if (errorMessage != null) {
                            _state.postValue(SearchState.Error(errorMessage))
                        }
                    }
                }
            )
        }
    }
}