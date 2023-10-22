package com.katoklizm.playlist_maker_full.domain.player

import com.katoklizm.playlist_maker_full.domain.search.model.Track

sealed interface SearchState {
    object Loading : SearchState

    data class ContentListSaveTrack(
        val track: List<Track>
    ) : SearchState

    data class ContentListSearchTrack(
        val track: List<Track>
    ) : SearchState

    data class Error(
        val errorMessage: String
    ) : SearchState

    data class Empty(
        val emptyMessage: String
    ) : SearchState

    object EmptyScreen: SearchState
}