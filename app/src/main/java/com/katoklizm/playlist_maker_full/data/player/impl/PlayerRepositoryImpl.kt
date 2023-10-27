package com.katoklizm.playlist_maker_full.data.player.impl

import android.media.MediaPlayer
import com.katoklizm.playlist_maker_full.domain.player.PlayerInteractor
import com.katoklizm.playlist_maker_full.domain.search.model.Track
import com.katoklizm.playlist_maker_full.domain.player.PlayerRepository
import java.util.Locale

class PlayerRepositoryImpl : PlayerRepository {
    private val mediaPlayer = MediaPlayer()
    private var remainingTimeMillis = 0

    override fun startPlayer() {
        mediaPlayer.start()
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
    }

    override fun preparePlayer(
        track: Track?, completion: () -> Unit,
        statusObserver: PlayerInteractor.StatusObserver
    ) {
        try {
            track?.previewUrl?.let {
                mediaPlayer.setDataSource(it)
                mediaPlayer.prepareAsync()
                mediaPlayer.setOnPreparedListener {
                    statusObserver.onPrepared()
                    completion()
                }
                mediaPlayer.setOnCompletionListener {
                    statusObserver.onCompletion()
                }
            }
        } catch (e: Exception) {
            // Обработка ошибки, например, вывод лога или уведомление пользователя
            e.printStackTrace()
        }
    }

    override fun currentPosition(): Int {
        remainingTimeMillis = mediaPlayer.currentPosition
        return remainingTimeMillis
    }

    override fun release() {
        mediaPlayer.release()
    }
}