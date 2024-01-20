package com.katoklizm.playlist_maker_full.presentation.medialibrary.favorite_track

import com.katoklizm.playlist_maker_full.domain.search.model.Track

sealed interface FavoriteTrackState {

    object Loading : FavoriteTrackState

    object EmptyList : FavoriteTrackState

    data class Content(
        val tracks: List<Track>
    ) : FavoriteTrackState

    data class Error(val code: Int) : FavoriteTrackState

}