package com.katoklizm.playlist_maker_full.search.audioplayer

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.katoklizm.playlist_maker_full.R
import com.katoklizm.playlist_maker_full.databinding.AudioPlayerBinding
import com.katoklizm.playlist_maker_full.search.track.ConstTrack.SAVE_TRACK
import com.katoklizm.playlist_maker_full.search.track.Track

class AudioPlayerActivity : AppCompatActivity() {
    var binding: AudioPlayerBinding? = null

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DELAY = 1000L
    }

    private var secondsLeftTextView: TextView? = null

    private var mainThreadHandler: Handler? = null

    private var playerState = STATE_DEFAULT

    lateinit var audioPlayerLoadTrack: AudioPlayerLoadTrack

    private var mediaPlayer = MediaPlayer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        secondsLeftTextView = findViewById(R.id.audio_player_time)

        mainThreadHandler = Handler(Looper.getMainLooper())

        binding?.audioPlayerBack?.setOnClickListener {
            finish()
        }

        audioPlayerLoadTrack = AudioPlayerLoadTrack(this)

        val track = intent.getParcelableExtra<Track>(SAVE_TRACK)

        audioPlayerLoadTrack.bind(track)

        preparePlayer(track)

        binding?.audioPlayerPlaySong?.setOnClickListener {
            val secondsCount = 30L

            if (secondsCount <= 0) {
                secondsLeftTextView?.text = "0:00"
            } else {
                startTimer(secondsCount)

            }
            playbackControl()
        }
    }

    private fun startTimer(duration: Long) {
        // Запоминаем время начала таймера
        val startTime = System.currentTimeMillis()
        // И отправляем задачу в Handler
        // Число секунд из EditText'а переводим в миллисекунды
        mainThreadHandler?.post(
            createUpdateTimerTask(startTime, duration * DELAY)
        )
    }

    private fun createUpdateTimerTask(startTime: Long, duration: Long): Runnable {
        return object : Runnable {
            override fun run() {
                // Сколько прошло времени с момента запуска таймера
                val elapsedTime = System.currentTimeMillis() - startTime
                // Сколько осталось до конца
                val remainingTime = duration - elapsedTime
                if (remainingTime > 0) {
                    // Если всё ещё отсчитываем секунды —
                    // обновляем UI и снова планируем задачу
                    val seconds = remainingTime / DELAY
                    secondsLeftTextView?.text = String.format("%d:%02d", seconds / 60, seconds % 60)
                    mainThreadHandler?.postDelayed(this, DELAY)
                } else {
//                    binding?.audioPlayerTime?.text =
//                    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
                    Toast.makeText(applicationContext, "GG", Toast.LENGTH_LONG).show()
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
                    playerState = STATE_PREPARED
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
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        binding?.audioPlayerPlaySong?.setImageResource(R.drawable.audio_player_play_song)
        playerState = STATE_PAUSED
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