package com.katoklizm.playlist_maker_full.domain.player

import com.katoklizm.playlist_maker_full.domain.search.model.Track

interface PlayerRepository {
    fun startPlayer()

    fun pausePlayer()

    fun preparePlayer(
        track: Track?,
        completion: () -> Unit,
        statusObserver: PlayerInteractor.StatusObserver
    )

    fun currentPosition(): Int

    fun release()
}