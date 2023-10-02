package com.katoklizm.playlist_maker_full.ui.audioplayer

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.katoklizm.playlist_maker_full.R
import com.katoklizm.playlist_maker_full.databinding.AudioPlayerBinding
import com.katoklizm.playlist_maker_full.data.ConstTrack.SAVE_TRACK
import com.katoklizm.playlist_maker_full.domain.model.Track
import com.katoklizm.playlist_maker_full.presentation.AudioPlayerController
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {
    var binding: AudioPlayerBinding? = null

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DELAY = 400L
    }

    private var track: Track? = null

    private var remainingTimeMillis = "00:00"

    private var secondsLeftTextView: TextView? = null

    private var mainThreadHandler: Handler? = null

    private var timerRunnable: Runnable? = null

    private var playerState = STATE_DEFAULT

    lateinit var audioPlayerController: AudioPlayerController

    private var mediaPlayer = MediaPlayer()

    private val handler = Handler(Looper.getMainLooper())

    private var timerIsRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        secondsLeftTextView = findViewById(R.id.audio_player_time)

        mainThreadHandler = Handler(Looper.getMainLooper())

        timerRunnable = createUpdateTimerTask()
        mainThreadHandler?.post(timerRunnable!!)

        binding?.audioPlayerBack?.setOnClickListener {
            finish()
        }

        audioPlayerController = AudioPlayerController(this)

        track = intent.getParcelableExtra<Track>(SAVE_TRACK)

        audioPlayerController.bind(track)

        preparePlayer(track)

        binding?.audioPlayerPlaySong?.setOnClickListener {
            playbackControl()
        }
    }

    override fun onPause() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            binding?.audioPlayerPlaySong?.setImageResource(R.drawable.audio_player_play_song)
            timerIsRunning = false
        }
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        if (playerState == STATE_PLAYING) {
            startPlayer()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        timerRunnable?.let {
            mainThreadHandler?.removeCallbacks(it)
            timerRunnable = null
        }
    }

    override fun onBackPressed() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        super.onBackPressed()
    }

    private fun startTimer() {
        mainThreadHandler?.post(
            createUpdateTimerTask()
        )
    }

    private fun createUpdateTimerTask(): Runnable {
        return object : Runnable {
            override fun run() {

                if (!timerIsRunning) return

                if (playerState == STATE_PLAYING) {
                    try {
                        if (mediaPlayer.isPlaying) {
                            val currentPosition = mediaPlayer.currentPosition
                            remainingTimeMillis = SimpleDateFormat(
                                "mm:ss",
                                Locale.getDefault()
                            ).format(currentPosition)
                            binding?.audioPlayerTime?.text = remainingTimeMillis
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

    private fun preparePlayer(track: Track?) {
        try {
            track?.previewUrl?.let {
                mediaPlayer.setDataSource(it)
                mediaPlayer.prepareAsync()
                mediaPlayer.setOnPreparedListener {
                    playerState = STATE_PREPARED
                }
                mediaPlayer.setOnCompletionListener {
                    binding?.audioPlayerPlaySong?.setImageResource(R.drawable.audio_player_play_song)
                    playerState = STATE_PREPARED
                    remainingTimeMillis = "00:00"
                    binding?.audioPlayerTime?.text = remainingTimeMillis
                }
            }
        } catch (e: Exception) {
            // Обработка ошибки, например, вывод лога или уведомление пользователя
            e.printStackTrace()
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        binding?.audioPlayerPlaySong?.setImageResource(R.drawable.audio_player_pause_song)

        playerState = STATE_PLAYING

        startTimer()

        timerIsRunning = true
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        binding?.audioPlayerPlaySong?.setImageResource(R.drawable.audio_player_play_song)
        playerState = STATE_PAUSED

        //Останавливаем таймер и сбрасываем флаг
        timerIsRunning = false
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }
}