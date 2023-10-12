package com.katoklizm.playlist_maker_full.ui.audioplayer

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.katoklizm.playlist_maker_full.R
import com.katoklizm.playlist_maker_full.databinding.AudioPlayerBinding
import com.katoklizm.playlist_maker_full.data.ConstTrack.SAVE_TRACK
import com.katoklizm.playlist_maker_full.data.player.PlayerState
import com.katoklizm.playlist_maker_full.domain.search.model.Track
import com.katoklizm.playlist_maker_full.presentation.AudioPlayerController
import com.katoklizm.playlist_maker_full.util.Creator

class AudioPlayerActivity : AppCompatActivity() {
    var binding: AudioPlayerBinding? = null

    private val repository = Creator.providePlayerRepository()

    private var track: Track? = null

    private var secondsLeftTextView: TextView? = null

    private var mainThreadHandler: Handler? = null

    private var timerRunnable: Runnable? = null

    private var playerState = PlayerState.STATE_DEFAULT

    lateinit var audioPlayerController: AudioPlayerController

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

        audioPlayerController = AudioPlayerController(this)

        track = intent.getParcelableExtra(SAVE_TRACK)

        audioPlayerController.bind(track)

        repository.preparePlayer(track)

        binding?.audioPlayerPlaySong?.setOnClickListener {
            repository.playbackControl()
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
        if (playerState == PlayerState.STATE_PLAYING) {
            repository.startPlayer()
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
}