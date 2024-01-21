package com.katoklizm.playlist_maker_full.presentation.medialibrary.playlist

import com.katoklizm.playlist_maker_full.domain.search.model.Track

sealed class PlayerScreenState {
    object Loading : PlayerScreenState()
    data class Ready(
        val track: Track,
        val playerStatus: PlayerStatus
    ) : PlayerScreenState()
}

enum class PlayerStatus {
    DEFAULT,
    PREPARED,
    PLAYING,
    PAUSED
}
