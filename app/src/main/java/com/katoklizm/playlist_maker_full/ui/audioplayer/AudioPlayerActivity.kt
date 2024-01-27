package com.katoklizm.playlist_maker_full.ui.audioplayer

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.katoklizm.playlist_maker_full.R
import com.katoklizm.playlist_maker_full.data.ConstTrack
import com.katoklizm.playlist_maker_full.data.ConstTrack.SAVE_TRACK
import com.katoklizm.playlist_maker_full.databinding.AudioPlayerBinding
import com.katoklizm.playlist_maker_full.domain.search.model.Track
import com.katoklizm.playlist_maker_full.presentation.audioplayer.AudioPlayerViewModel
import com.katoklizm.playlist_maker_full.presentation.medialibrary.playlist.PlayerScreenState
import com.katoklizm.playlist_maker_full.presentation.medialibrary.playlist.PlayerStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {
    private var binding: AudioPlayerBinding? = null

    private var mainThreadHandler: Handler? = null

    private var timerRunnable: Runnable? = null

    private var playerStatus = PlayerStatus.DEFAULT

    private val audioPlayerViewModel by viewModel<AudioPlayerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        val selectedTrack: Track? = intent.getParcelableExtra(SAVE_TRACK)
        selectedTrack?.let { audioPlayerViewModel.initState(it) }

        binding?.audioPlayerNameSong?.text = selectedTrack?.trackName

        binding?.audioPlayerNameMusician?.text = selectedTrack?.artistName

        binding?.audioPlayerTextViewTrackNameRead?.text = selectedTrack?.trackName
        binding?.audioPlayerTextViewYearRead?.text = selectedTrack?.releaseDate
        binding?.audioPlayerTextViewGenreRead?.text = selectedTrack?.primaryGenreName
        binding?.audioPlayerTextViewCountryRead?.text = selectedTrack?.country

        binding?.audioPlayerTime?.text = getString(R.string.start_time)

        binding?.audioPlayerTextViewTimeRead?.text = selectedTrack?.trackTime

        Glide.with(this)
            .load(selectedTrack?.artworkUrl512)
            .transform(RoundedCorners(ConstTrack.ROUNDED_CORNERS_RADIUS))
            .placeholder(R.drawable.vector_plug)
            .into(binding!!.audioPlayerImageTrack)

        playerStatus = audioPlayerViewModel.playerStateListener()

        audioPlayerViewModel.playerState.observe(this) {
            renderState(it)
        }

        audioPlayerViewModel.timerState.observe(this) {
            binding?.audioPlayerTime?.text = SimpleDateFormat(
                "mm:ss", Locale.getDefault()
            ).format(it)
        }

        mainThreadHandler = Handler(Looper.getMainLooper())

        audioPlayerViewModel.preparePlayer(selectedTrack) {
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
            prepareFavoriteTrack()

        }
    }

    override fun onPause() {
        super.onPause()
        Log.d("State_Player", "State ${audioPlayerViewModel.playerStateListener()}")
        if (audioPlayerViewModel.playerStateListener() == PlayerStatus.PLAYING) {
            audioPlayerViewModel.pausePlayer()
            binding?.audioPlayerPlaySong?.setImageResource(R.drawable.audio_player_play_song)
        }
    }

    private fun renderState(state: PlayerScreenState) {
        when (state) {
            is PlayerScreenState.Loading -> {

            }

            is PlayerScreenState.Ready -> {
                processingPlayerStatus(state.playerStatus)

                val newImageRes =
                    if (state.track.isFavorite) R.drawable.audio_player_like_on else R.drawable.audio_player_like_off
                binding?.audioPlayerLikeMusicTrack?.setImageResource(newImageRes)
            }
        }
    }

    private fun processingPlayerStatus(status: PlayerStatus) {
        when (status) {
            PlayerStatus.PLAYING -> {
                binding?.audioPlayerPlaySong?.setImageResource(R.drawable.audio_player_pause_song)
            }

            PlayerStatus.PREPARED -> {
                binding?.audioPlayerPlaySong?.setImageResource(R.drawable.audio_player_play_song)
            }

            PlayerStatus.PAUSED -> {
                binding?.audioPlayerPlaySong?.setImageResource(R.drawable.audio_player_play_song)
            }

            PlayerStatus.DEFAULT -> {
                binding?.audioPlayerPlaySong?.setImageResource(R.drawable.audio_player_play_song)
            }
        }
    }

    fun prepareFavoriteTrack() {
        audioPlayerViewModel.onFavoriteClicked()
    }

    fun preparePlayer() {
        binding?.audioPlayerPlaySong?.setImageResource(R.drawable.audio_player_play_song)
        binding?.audioPlayerPlaySong?.isEnabled = true
    }
}