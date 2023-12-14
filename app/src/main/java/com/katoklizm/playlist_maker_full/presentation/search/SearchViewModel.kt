package com.katoklizm.playlist_maker_full.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.katoklizm.playlist_maker_full.domain.search.SearchState
import com.katoklizm.playlist_maker_full.domain.search.api.TrackInteractor
import com.katoklizm.playlist_maker_full.domain.search.model.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SearchViewModel(
    private val trackInteractor: TrackInteractor
) : ViewModel() {

    private val stateLiveData = MutableLiveData<SearchState>()

    val trackList = ArrayList<Track>()
    val trackHistoryList = ArrayList<Track>()

    private var searchJob: Job? = null

    private val _isScreenPaused = MutableLiveData<Boolean>()
    fun isScreenPaused(): LiveData<Boolean> = _isScreenPaused

    fun observeState(): LiveData<SearchState> = stateLiveData

    private fun renderState(state: SearchState) {
        stateLiveData.postValue(state)
    }

    fun activeFragment(state: Boolean) {
        _isScreenPaused.postValue(state)
    }

    fun onTextChanged(searchText: String?) {
        if (searchText.isNullOrBlank()) {
            if (trackInteractor.readSearchHistory().isNotEmpty()) renderState(
                SearchState.ContentListSaveTrack(
                    trackInteractor.readSearchHistory()
                )
            ) else {
                renderState(SearchState.EmptyScreen)
            }
        }
    }

    fun onFocusChanged(hasFocus: Boolean, searchText: String) {
        if (hasFocus && searchText.isEmpty()) {
            if (trackInteractor.readSearchHistory().isNotEmpty()) {
                renderState(SearchState.ContentListSaveTrack(trackInteractor.readSearchHistory()))
            } else {
                renderState(SearchState.EmptyScreen)
            }
        }
    }

    fun clearSearchHistory() {
        trackInteractor.clearSearchHistory()
        renderState(SearchState.EmptyScreen)
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

            viewModelScope.launch {
                trackInteractor
                    .searchTrack(searchText)
                    .collect { pair ->
                        processResult(pair.first, pair.second)
                    }
            }

//            trackInteractor.searchTrack(
//                searchText, object : TrackInteractor.TrackConsumer {
//                    override fun consume(foundTrack: List<Track>?, errorMessage: String?) {
//                        if (foundTrack != null) {
//                            if (foundTrack.isNotEmpty()) {
//                                renderState(SearchState.ContentListSearchTrack(foundTrack))
//                            } else {
//                                renderState(SearchState.Empty(""))
//                            }
//                        }
//
//                        if (errorMessage != null) {
//                            renderState(SearchState.Error(errorMessage))
//                        }
//                    }
//                }
//            )
        }
    }

    private fun processResult(foundTrack: List<Track>?, errorMessage: String?) {
        val track = mutableListOf<Track>()

        if (foundTrack != null) {
            track.addAll(foundTrack)
        }

        when {
            errorMessage != null -> {
                renderState(SearchState.Error(""))

            }
            track.isEmpty() -> {
                renderState(SearchState.Empty(""))
            }
            else -> {
                renderState(SearchState.ContentListSearchTrack(track = track))
            }
        }
    }
}