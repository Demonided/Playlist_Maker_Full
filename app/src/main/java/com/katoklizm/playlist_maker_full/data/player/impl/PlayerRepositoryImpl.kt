package com.katoklizm.playlist_maker_full.data.player.impl

import android.app.Activity
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.katoklizm.playlist_maker_full.data.player.PlayerState
import com.katoklizm.playlist_maker_full.domain.search.model.Track
import com.katoklizm.playlist_maker_full.domain.player.PlayerRepository
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerRepositoryImpl: PlayerRepository {
    private val mediaPlayer = MediaPlayer()
    private var playerState = PlayerState.STATE_DEFAULT
    private var timerIsRunning = false
    private var remainingTimeMillis = "00:00"
    private val handler: Handler = Handler(Looper.getMainLooper())
    private var mainThreadHandler: Handler? = null

    override fun startPlayer() {
        mediaPlayer.start()
//        binding?.audioPlayerPlaySong?.setImageResource(R.drawable.audio_player_pause_song)

        playerState = PlayerState.STATE_PLAYING

        startTimer()

        timerIsRunning = true
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
//        binding?.audioPlayerPlaySong?.setImageResource(R.drawable.audio_player_play_song)
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
//                    binding?.audioPlayerPlaySong?.setImageResource(R.drawable.audio_player_play_song)
                    playerState = PlayerState.STATE_PREPARED
                    remainingTimeMillis = "00:00"
//                    binding?.audioPlayerTime?.text = remainingTimeMillis
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

                if (playerState == PlayerState.STATE_PLAYING) {
                    try {
                        if (mediaPlayer.isPlaying) {
                            val currentPosition = mediaPlayer.currentPosition
                            remainingTimeMillis = SimpleDateFormat(
                                "mm:ss",
                                Locale.getDefault()
                            ).format(currentPosition)
//                            binding?.audioPlayerTime?.text = remainingTimeMillis
                            handler.postDelayed(this, DELAY)
                        } else {
                            timerIsRunning = false
                        }
                    } catch (e: IllegalStateException) {
                        timerIsRunning = false
                        e.printStackTrace()
                    }
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
            }

            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> {
                startPlayer()
            }

            else -> {
                pausePlayer()
            }
        }
    }

    override fun playerStateReporter(): PlayerState {
        return playerState
    }

    companion object {
        const val DELAY = 100L
    }
}