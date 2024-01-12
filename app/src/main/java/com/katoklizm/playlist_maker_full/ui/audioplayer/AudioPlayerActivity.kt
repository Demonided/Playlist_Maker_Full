package com.katoklizm.playlist_maker_full.ui.audioplayer

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.katoklizm.playlist_maker_full.R
import com.katoklizm.playlist_maker_full.data.ConstTrack
import com.katoklizm.playlist_maker_full.databinding.AudioPlayerBinding
import com.katoklizm.playlist_maker_full.data.ConstTrack.SAVE_TRACK
import com.katoklizm.playlist_maker_full.domain.player.PlayerState
import com.katoklizm.playlist_maker_full.domain.search.model.Track
import com.katoklizm.playlist_maker_full.presentation.audioplayer.AudioPlayerViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {
    private var binding: AudioPlayerBinding? = null

    private var track: Track? = null

    private var mainThreadHandler: Handler? = null

    private var timerRunnable: Runnable? = null

    private var playerState = PlayerState.STATE_DEFAULT

    private val audioPlayerViewModel by viewModel<AudioPlayerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        track = intent.getParcelableExtra(SAVE_TRACK)

        binding?.audioPlayerNameSong?.text = track?.trackName

        binding?.audioPlayerNameMusician?.text = track?.artistName

        binding?.audioPlayerTextViewTrackNameRead?.text = track?.trackName
        binding?.audioPlayerTextViewYearRead?.text = track?.releaseDate
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

        audioPlayerViewModel.statePlayer.observe(this) {
            renderState(it)
        }

        audioPlayerViewModel.timerState.observe(this) {
            binding?.audioPlayerTime?.text = SimpleDateFormat(
                "mm:ss", Locale.getDefault()
            ).format(it)
        }

        mainThreadHandler = Handler(Looper.getMainLooper())

        audioPlayerViewModel.preparePlayer(track) {
            // доработать в процесе отображения не активной кнопки
            preparePlayer()
        }

        binding?.audioPlayerBack?.setOnClickListener {
            finish()
        }

        binding?.audioPlayerPlaySong?.setOnClickListener {
            audioPlayerViewModel.playbackControl()
        }

        binding?.audioPlayerLikeMusicTrack?.setOnClickListener {
            if (track!!.isFavorite) {
                binding?.audioPlayerLikeMusicTrack?.setImageResource(R.drawable.audio_player_like_off)
                track?.isFavorite = false
            } else {
                binding?.audioPlayerLikeMusicTrack?.setImageResource(R.drawable.audio_player_like_on)
                track?.isFavorite = true
            }
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d("State_Player", "State ${audioPlayerViewModel.playerStateListener()}")
        if (audioPlayerViewModel.playerStateListener() == PlayerState.STATE_PLAYING) {
            audioPlayerViewModel.pausePlayer()
            binding?.audioPlayerPlaySong?.setImageResource(R.drawable.audio_player_play_song)
        }
    }

    private fun renderState(state: PlayerState) {
        when (state) {
            PlayerState.STATE_PLAYING -> {
                binding?.audioPlayerPlaySong?.setImageResource(R.drawable.audio_player_pause_song)
            }

            PlayerState.STATE_PREPARED -> {
                binding?.audioPlayerPlaySong?.setImageResource(R.drawable.audio_player_play_song)
            }

            PlayerState.STATE_PAUSED -> {
                binding?.audioPlayerPlaySong?.setImageResource(R.drawable.audio_player_play_song)
            }

            PlayerState.STATE_DEFAULT -> {
                binding?.audioPlayerPlaySong?.setImageResource(R.drawable.audio_player_play_song)
            }
        }
    }

    fun preparePlayer() {
        binding?.audioPlayerPlaySong?.setImageResource(R.drawable.audio_player_play_song)
        binding?.audioPlayerPlaySong?.isEnabled = true
    }

}