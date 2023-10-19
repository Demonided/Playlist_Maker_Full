package com.katoklizm.playlist_maker_full.ui.audioplayer.activity

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.katoklizm.playlist_maker_full.R
import com.katoklizm.playlist_maker_full.data.ConstTrack
import com.katoklizm.playlist_maker_full.databinding.AudioPlayerBinding
import com.katoklizm.playlist_maker_full.data.ConstTrack.SAVE_TRACK
import com.katoklizm.playlist_maker_full.data.player.PlayerState
import com.katoklizm.playlist_maker_full.domain.search.model.Track
import com.katoklizm.playlist_maker_full.ui.audioplayer.viewmodel.AudioPlayerViewModel
import com.katoklizm.playlist_maker_full.util.Creator

class AudioPlayerActivity : AppCompatActivity() {
    var binding: AudioPlayerBinding? = null

    private var track: Track? = null

    private var mainThreadHandler: Handler? = null

    private var timerRunnable: Runnable? = null

    private var playerState = PlayerState.STATE_DEFAULT

    lateinit var viewModelAudioPlayer: AudioPlayerViewModel

    lateinit var audioPlayerImageTrack: ImageView
    lateinit var audioPlayerNameSong: TextView
    lateinit var audioPlayerNameMusician: TextView
    lateinit var audioPlayerTime: TextView
    lateinit var audioPlayerTextViewTimeRead: TextView
    lateinit var audioPlayerTextViewTrackNameRead: TextView
    lateinit var audioPlayerTextViewYearRead: TextView
    lateinit var audioPlayerTextViewGenreRead: TextView
    lateinit var audioPlayerTextViewCountryRead: TextView

    private var mediaPlayer = MediaPlayer()

    private var timerIsRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        viewModelAudioPlayer = ViewModelProvider(
            this,
            AudioPlayerViewModel.getViewModelFactory()
        )[AudioPlayerViewModel::class.java]

        track = intent.getParcelableExtra(SAVE_TRACK)

        audioPlayerImageTrack = findViewById(R.id.audio_player_image_track)
        audioPlayerNameSong = findViewById(R.id.audio_player_name_song)
        audioPlayerNameMusician = findViewById(R.id.audio_player_name_musician)
        audioPlayerTime = findViewById(R.id.audio_player_time)
        audioPlayerTextViewTimeRead = findViewById(R.id.audio_player_textView_time_read)
        audioPlayerTextViewTrackNameRead = findViewById(R.id.audio_player_textView_track_name_read)
        audioPlayerTextViewYearRead = findViewById(R.id.audio_player_textView_year_read)
        audioPlayerTextViewGenreRead = findViewById(R.id.audio_player_textView_genre_read)
        audioPlayerTextViewCountryRead = findViewById(R.id.audio_player_textView_country_read)

        audioPlayerNameSong.text = track?.trackName

        audioPlayerNameMusician.text = track?.artistName

        audioPlayerTextViewTrackNameRead.text = track?.trackName
        audioPlayerTextViewYearRead.text = track?.releaseDate?.let { ConstTrack.formatDate(it) }
        audioPlayerTextViewGenreRead.text = track?.primaryGenreName
        audioPlayerTextViewCountryRead.text = track?.country

        audioPlayerTime.text = "00:00"

        audioPlayerTextViewTimeRead.text = track?.trackTime

        Glide.with(this)
            .load(track?.artworkUrl512)
            .transform(RoundedCorners(ConstTrack.ROUNDED_CORNERS_RADIUS))
            .placeholder(R.drawable.vector_plug)
            .into(audioPlayerImageTrack)

        playerState = viewModelAudioPlayer.playerStateListener()

        mainThreadHandler = Handler(Looper.getMainLooper())

        mainThreadHandler?.post(
            updateStateButton()
        )

        mainThreadHandler?.post(
            updateTimer()
        )

        viewModelAudioPlayer.preparePlayer(track){
            // доработать в процесе отображения не активной кнопки
            preparePlayer()
        }

        binding?.audioPlayerBack?.setOnClickListener {
            finish()
        }

        binding?.audioPlayerPlaySong?.setOnClickListener {
            viewModelAudioPlayer.playbackControl()

        }
    }

    override fun onPause() {
        if (playerState == PlayerState.STATE_PLAYING) {
            viewModelAudioPlayer.pausePlayer()
            binding?.audioPlayerPlaySong?.setImageResource(R.drawable.audio_player_play_song)
            timerIsRunning = false
        }
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        if (playerState == PlayerState.STATE_PLAYING) {
            viewModelAudioPlayer.startPlayer()
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

    fun playerStateDrawer() {
        playerState = viewModelAudioPlayer.playerStateListener()
        when (playerState) {
            PlayerState.STATE_DEFAULT -> {
                binding?.audioPlayerPlaySong?.setImageResource(R.drawable.audio_player_play_song)

            }

            PlayerState.STATE_PREPARED -> {
                binding?.audioPlayerPlaySong?.setImageResource(R.drawable.audio_player_play_song)
            }

            PlayerState.STATE_PAUSED -> {
                binding?.audioPlayerPlaySong?.setImageResource(R.drawable.audio_player_play_song)
            }

            PlayerState.STATE_PLAYING -> {
                binding?.audioPlayerPlaySong?.setImageResource(R.drawable.audio_player_pause_song)
            }
        }
    }

    fun preparePlayer() {
        binding?.audioPlayerPlaySong?.setImageResource(R.drawable.audio_player_play_song)
        binding?.audioPlayerPlaySong?.isEnabled = true
    }

    private fun updateStateButton(): Runnable {
        val updatedButton = Runnable {
            playerStateDrawer()
            mainThreadHandler?.postDelayed(updateStateButton(), DELAY)
        }
        return updatedButton
    }

    private fun updateTimer(): Runnable {
        val updatedTimer = Runnable {
            binding?.audioPlayerTime?.text = viewModelAudioPlayer.transferTime()
            mainThreadHandler?.postDelayed(updateTimer(), DELAY)
        }
        return updatedTimer
    }

    companion object {
        const val DELAY = 100L
    }
}