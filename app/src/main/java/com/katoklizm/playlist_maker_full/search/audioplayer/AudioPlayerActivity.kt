package com.katoklizm.playlist_maker_full.search.audioplayer

import android.media.MediaPlayer
import android.media.MediaPlayer.TrackInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.katoklizm.playlist_maker_full.R
import com.katoklizm.playlist_maker_full.databinding.AudioPlayerBinding
import com.katoklizm.playlist_maker_full.search.track.ConstTrack.SAVE_TRACK
import com.katoklizm.playlist_maker_full.search.track.Track
import com.katoklizm.playlist_maker_full.search.track.iTunesSearchApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import javax.security.auth.callback.Callback

class AudioPlayerActivity : AppCompatActivity() {
    var binding: AudioPlayerBinding? = null

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DELAY = 1000L
        private const val TRACK_EXCERPT = 30L
    }

    private var track: Track? = null

    private var remainingTimeMillis: Long = TRACK_EXCERPT

    private var secondsLeftTextView: TextView? = null

    private var mainThreadHandler: Handler? = null

    private var playerState = STATE_DEFAULT

    lateinit var audioPlayerLoadTrack: AudioPlayerLoadTrack

    private var mediaPlayer = MediaPlayer()

    private var timerIsRunning = false

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

        track = intent.getParcelableExtra<Track>(SAVE_TRACK)

        audioPlayerLoadTrack.bind(track)

        preparePlayer(track)

        binding?.audioPlayerPlaySong?.setOnClickListener {

            playbackControl(track)
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
            startPlayer(track)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    override fun onBackPressed() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        super.onBackPressed()
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

                if (!timerIsRunning) return

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
                    remainingTimeMillis = seconds
                } else {
                    showMessage("The end")
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

    private fun startPlayer(track: Track?) {
        mediaPlayer.start()
        binding?.audioPlayerPlaySong?.setImageResource(R.drawable.audio_player_pause_song)

        if (playerState == STATE_PREPARED) {
            remainingTimeMillis =
                if (track?.trackTimeMillis!!.toLong() / 1000 > 30) 30L else track?.trackTimeMillis!!.toLong() / 1000
        }

        playerState = STATE_PLAYING

        if (remainingTimeMillis <= 0) {
            Toast.makeText(applicationContext, "Not", Toast.LENGTH_LONG).show()

        } else {
            startTimer(remainingTimeMillis)
        }

        timerIsRunning = true
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        binding?.audioPlayerPlaySong?.setImageResource(R.drawable.audio_player_play_song)
        playerState = STATE_PAUSED

        //Останавливаем таймер и сбрасываем флаг
        timerIsRunning = false
    }

    private fun playbackControl(track: Track?) {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer(track)
            }
        }
    }

    private fun showMessage(text: String) {
        val rootView = findViewById<View>(android.R.id.content)?.rootView
        if (rootView != null) {
            binding?.audioPlayerPlaySong?.setImageResource(R.drawable.audio_player_play_song)
            remainingTimeMillis = TRACK_EXCERPT
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
        }
    }
}