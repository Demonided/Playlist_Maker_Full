package com.katoklizm.playlist_maker_full.domain.player

import com.katoklizm.playlist_maker_full.domain.search.model.Track

interface PlayerInteractor {
    fun startPlayer()

    fun pausePlayer()

    fun preparePlayer(track: Track?, completion: () -> Unit, statusObserver: StatusObserver)

    fun startTimer()

    fun playerStateListener(): PlayerState

    fun playbackControl()

    fun transferTime(): String

    fun release()

    interface StatusObserver {
        fun onPrepared()

        fun onCompletion()
    }
}