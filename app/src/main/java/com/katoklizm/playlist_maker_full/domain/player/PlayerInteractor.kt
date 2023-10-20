package com.katoklizm.playlist_maker_full.domain.player

import com.katoklizm.playlist_maker_full.data.search.dto.TrackDto
import com.katoklizm.playlist_maker_full.data.player.PlayerState
import com.katoklizm.playlist_maker_full.domain.search.model.Track

interface PlayerInteractor {
    fun startPlayer()

    fun pausePlayer()

    fun preparePlayer(track: Track?)

    fun startTimer()

    fun playbackControl()

    fun playerStateListener(): PlayerState

    fun transferTime(): String

    fun release()
}