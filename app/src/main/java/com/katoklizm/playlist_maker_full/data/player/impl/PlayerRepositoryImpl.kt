package com.katoklizm.playlist_maker_full.data.player.impl

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.katoklizm.playlist_maker_full.domain.player.PlayerInteractor
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

    override fun preparePlayer(track: Track?, completion: () -> Unit,
                               statusObserver: PlayerInteractor.StatusObserver) {
        try {
            track?.previewUrl?.let {
                mediaPlayer.setDataSource(it)
                mediaPlayer.prepareAsync()
                mediaPlayer.setOnPreparedListener {
                    playerState = PlayerState.STATE_PREPARED
                    statusObserver.onPrepared()
                    completion()
                }
                mediaPlayer.setOnCompletionListener {
                    playerState = PlayerState.STATE_PREPARED
                    statusObserver.onCompletion()
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
                    if (playerState == PlayerState.STATE_PLAYING) {
                        val currentPosition = mediaPlayer.currentPosition
                        remainingTimeMillis = SimpleDateFormat(
                            "mm:ss",
                            Locale.getDefault()
                        ).format(currentPosition)
                        handler.postDelayed(this, DELAY)
                    } else {
                        remainingTimeMillis = "00:00"
                        timerIsRunning = false
                        handler.removeCallbacksAndMessages(null)
                    }
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

    override fun playbackControl() {
        when (playerState) {
            PlayerState.STATE_PLAYING -> {
                pausePlayer()
                Log.d("StatePlayer", "Статус 1 в репозитории")
            }

            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> {
                startPlayer()
                Log.d("StatePlayer", "Статус 2 в репозитории")
            }

            else -> {
                pausePlayer()
                Log.d("StatePlayer", "Статус 3 в репозитории")
            }
        }
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