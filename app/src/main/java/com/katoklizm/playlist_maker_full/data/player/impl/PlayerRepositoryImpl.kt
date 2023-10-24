package com.katoklizm.playlist_maker_full.data.player.impl

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.katoklizm.playlist_maker_full.domain.player.PlayerState
import com.katoklizm.playlist_maker_full.domain.search.model.Track
import com.katoklizm.playlist_maker_full.domain.player.PlayerRepository
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerRepositoryImpl : PlayerRepository {
    private val mediaPlayer = MediaPlayer()
    private var playerState = PlayerState.STATE_DEFAULT
    private var timerIsRunning = false
    private var remainingTimeMillis = "00:00"
    private val handler: Handler = Handler(Looper.getMainLooper())
    private var mainThreadHandler: Handler? = null

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = PlayerState.STATE_PLAYING

        startTimer()

        timerIsRunning = true
        handler.post(
            createUpdateTimerTask()
        )
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = PlayerState.STATE_PAUSED

        //Останавливаем таймер и сбрасываем флаг
        timerIsRunning = false
    }

    override fun preparePlayer(track: Track?) {
        try {
            track?.previewUrl?.let {
                mediaPlayer.setDataSource(it)
                mediaPlayer.prepareAsync()
                mediaPlayer.setOnPreparedListener {
                    playerState = PlayerState.STATE_PREPARED
                }
                mediaPlayer.setOnCompletionListener {
                    playerState = PlayerState.STATE_PREPARED
                }
            }
        } catch (e: Exception) {
            // Обработка ошибки, например, вывод лога или уведомление пользователя
            e.printStackTrace()
        }
    }

    fun createUpdateTimerTask(): Runnable {
        return object : Runnable {
            override fun run() {
                if (!timerIsRunning) return

                try {
                    val currentPosition = mediaPlayer.currentPosition
                    remainingTimeMillis = SimpleDateFormat(
                        "mm:ss",
                        Locale.getDefault()
                    ).format(currentPosition)
                    handler.postDelayed(this, DELAY)
                } catch (e: IllegalStateException) {
                    timerIsRunning = false
                    e.printStackTrace()
                }
            }
        }
    }

    override fun startTimer() {
        mainThreadHandler?.post(
            createUpdateTimerTask()
        )
    }

    override fun playerStateReporter(): PlayerState {
        return playerState
    }

    override fun transferTime(): String {
        return remainingTimeMillis
    }

    override fun release() {
        mediaPlayer.release()
    }

    companion object {
        const val DELAY = 100L
    }
}