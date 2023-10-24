package com.katoklizm.playlist_maker_full.ui.audioplayer.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.katoklizm.playlist_maker_full.R
import com.katoklizm.playlist_maker_full.data.ConstTrack
import com.katoklizm.playlist_maker_full.databinding.AudioPlayerBinding
import com.katoklizm.playlist_maker_full.data.ConstTrack.SAVE_TRACK
import com.katoklizm.playlist_maker_full.domain.player.PlayerState
import com.katoklizm.playlist_maker_full.domain.search.model.Track
import com.katoklizm.playlist_maker_full.ui.audioplayer.viewmodel.AudioPlayerViewModel
import java.text.SimpleDateFormat
import java.util.Locale

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

        audioPlayerViewModel.statePlayer.observe(this) {
            renderState(it)
        }

//        audioPlayerViewModel.timerState.observe(this) {
//            binding?.audioPlayerTime?.text = SimpleDateFormat(
//        "mm:ss", Locale.getDefault()
//            ).format(it)
//        }

        mainThreadHandler = Handler(Looper.getMainLooper())

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

    private fun renderState(state: PlayerState) {
        when (state) {
            PlayerState.STATE_PLAYING -> {
                binding?.audioPlayerPlaySong?.setImageResource(R.drawable.audio_player_pause_song)
                Log.d("StatePlayer", "Статус 1 в Активити")
            }

            PlayerState.STATE_PREPARED -> {
                binding?.audioPlayerPlaySong?.setImageResource(R.drawable.audio_player_play_song)
                Log.d("StatePlayer", "Статус 2 в Активити")
            }

            PlayerState.STATE_PAUSED -> {
                binding?.audioPlayerPlaySong?.setImageResource(R.drawable.audio_player_play_song)
                Log.d("StatePlayer", "Статус 3 в Активити")
            }

            else -> {
                binding?.audioPlayerPlaySong?.setImageResource(R.drawable.audio_player_play_song)
                audioPlayerViewModel.pausePlayer()
            }
        }
    }

    fun preparePlayer() {
        binding?.audioPlayerPlaySong?.setImageResource(R.drawable.audio_player_play_song)
        binding?.audioPlayerPlaySong?.isEnabled = true
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