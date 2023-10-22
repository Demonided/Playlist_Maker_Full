package com.katoklizm.playlist_maker_full.ui.audioplayer.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
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

class AudioPlayerActivity : AppCompatActivity() {
    var binding: AudioPlayerBinding? = null

    private var track: Track? = null

    private var mainThreadHandler: Handler? = null

    private var timerRunnable: Runnable? = null

    private var playerState = PlayerState.STATE_DEFAULT

    lateinit var audioPlayerViewModel: AudioPlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        audioPlayerViewModel = ViewModelProvider(
            this,
            AudioPlayerViewModel.getViewModelFactory()
        )[AudioPlayerViewModel::class.java]

        track = intent.getParcelableExtra(SAVE_TRACK)

        binding?.audioPlayerNameSong?.text = track?.trackName

       binding?.audioPlayerNameMusician?.text = track?.artistName

        binding?.audioPlayerTextViewTrackNameRead?.text = track?.trackName
        binding?.audioPlayerTextViewYearRead?.text = track?.releaseDate?.let { ConstTrack.formatDate(it) }
        binding?.audioPlayerTextViewGenreRead?.text = track?.primaryGenreName
        binding?.audioPlayerTextViewCountryRead?.text = track?.country

        binding?.audioPlayerTime?.text = getString(R.string.start_time)

        binding?.audioPlayerTextViewTimeRead?.text = track?.trackTime

        Glide.with(this)
            .load(track?.artworkUrl512)
            .transform(RoundedCorners(ConstTrack.ROUNDED_CORNERS_RADIUS))
            .placeholder(R.drawable.vector_plug)
            .into(binding!!.audioPlayerImageTrack)

        playerState = audioPlayerViewModel.playerStateListener()

        mainThreadHandler = Handler(Looper.getMainLooper())

        mainThreadHandler?.post(
            updateStateButton()
        )

        mainThreadHandler?.post(
            updateTimer()
        )

        audioPlayerViewModel.preparePlayer(track){
            // доработать в процесе отображения не активной кнопки
            preparePlayer()
        }

        binding?.audioPlayerBack?.setOnClickListener {
            finish()
        }

        binding?.audioPlayerPlaySong?.setOnClickListener {
            audioPlayerViewModel.playbackControl()

        }
    }

    override fun onPause() {
        if (playerState == PlayerState.STATE_PLAYING) {
            audioPlayerViewModel.pausePlayer()
            binding?.audioPlayerPlaySong?.setImageResource(R.drawable.audio_player_play_song)
        }
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        if (playerState == PlayerState.STATE_PLAYING) {
            audioPlayerViewModel.startPlayer()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        audioPlayerViewModel.release()
        timerRunnable?.let {
            mainThreadHandler?.removeCallbacks(it)
            timerRunnable = null
        }
    }

    override fun onBackPressed() {
        if (playerState == PlayerState.STATE_PLAYING) {
            audioPlayerViewModel.pausePlayer()
        }
        super.onBackPressed()
    }

    fun playerStateDrawer() {
        playerState = audioPlayerViewModel.playerStateListener()
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
            binding?.audioPlayerTime?.text = audioPlayerViewModel.transferTime()
            mainThreadHandler?.postDelayed(updateTimer(), DELAY)
        }
        return updatedTimer
    }

    companion object {
        const val DELAY = 100L
    }
}