package com.katoklizm.playlist_maker_full.ui.audioplayer

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.katoklizm.playlist_maker_full.R
import com.katoklizm.playlist_maker_full.data.ConstTrack
import com.katoklizm.playlist_maker_full.data.ConstTrack.SAVE_TRACK
import com.katoklizm.playlist_maker_full.databinding.AudioPlayerBinding
import com.katoklizm.playlist_maker_full.domain.album.model.AlbumPlaylist
import com.katoklizm.playlist_maker_full.domain.album.model.TrackAlbumPlaylist
import com.katoklizm.playlist_maker_full.domain.search.model.Track
import com.katoklizm.playlist_maker_full.presentation.audioplayer.AudioPlayerViewModel
import com.katoklizm.playlist_maker_full.presentation.medialibrary.playlist.PlayerScreenState
import com.katoklizm.playlist_maker_full.presentation.medialibrary.playlist.PlayerStateAlbum
import com.katoklizm.playlist_maker_full.presentation.medialibrary.playlist.PlayerStatus
import com.katoklizm.playlist_maker_full.presentation.medialibrary.playlist.PlaylistState
import com.katoklizm.playlist_maker_full.ui.medialibrary.playlist.PlaylistAdapter
import com.katoklizm.playlist_maker_full.ui.newplalist.NewPlaylistFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Stack

class AudioPlayerActivity : AppCompatActivity() {

    private val audioPlayerViewModel by viewModel<AudioPlayerViewModel>()

    private var _binding: AudioPlayerBinding? = null
    val binding get() = _binding!!

    private var mainThreadHandler: Handler? = null

    private var timerRunnable: Runnable? = null

    private var playerStatus = PlayerStatus.DEFAULT

    lateinit var adapter: AudioPlayerAdapter

    private lateinit var recyclerView: RecyclerView

    private val fragmentStack: Stack<Fragment> = Stack()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = AudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val selectedTrack: Track? = intent.getParcelableExtra(SAVE_TRACK)
        selectedTrack?.let { audioPlayerViewModel.initState(it) }

        binding.audioPlayerNameSong.text = selectedTrack?.trackName

        binding.audioPlayerNameMusician.text = selectedTrack?.artistName

        binding.audioPlayerTextViewTrackNameRead.text = selectedTrack?.trackName
        binding.audioPlayerTextViewYearRead.text = selectedTrack?.releaseDate
        binding.audioPlayerTextViewGenreRead.text = selectedTrack?.primaryGenreName
        binding.audioPlayerTextViewCountryRead.text = selectedTrack?.country

        binding.audioPlayerTime.text = getString(R.string.start_time)

        binding.audioPlayerTextViewTimeRead.text = selectedTrack?.trackTime

        Glide.with(this)
            .load(selectedTrack?.artworkUrl512)
            .transform(RoundedCorners(ConstTrack.ROUNDED_CORNERS_RADIUS))
            .placeholder(R.drawable.vector_plug)
            .into(binding.audioPlayerImageTrack)

        playerStatus = audioPlayerViewModel.playerStateListener()

        adapter = AudioPlayerAdapter()

        recyclerView = binding.audioPlayerRecyclerView

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter

        adapter.itemClickListener = { _, playlist ->
            if (selectedTrack != null) {
                audioPlayerViewModel.onPlaylistClicked(playlist, track = selectedTrack)
            }
            adapter.notifyDataSetChanged()
        }

        audioPlayerViewModel.messageTrack.observe(this) { message ->
            val toast = Toast.makeText(
                this,
                message,
                Toast.LENGTH_LONG
            )
            toast.show()
        }

        audioPlayerViewModel.albumState.observe(this) {
            render(it)
        }

        audioPlayerViewModel.playerState.observe(this) {
            renderState(it)
        }

        audioPlayerViewModel.timerState.observe(this) {
            binding.audioPlayerTime.text = SimpleDateFormat(
                "mm:ss", Locale.getDefault()
            ).format(it)
        }

        mainThreadHandler = Handler(Looper.getMainLooper())

        audioPlayerViewModel.preparePlayer(selectedTrack) {
            // доработать в процесе отображения не активной кнопки
            preparePlayer()
        }

        binding.audioPlayerBack.setOnClickListener {
            finish()
        }

        binding.audioPlayerPlaySong.setOnClickListener {
            audioPlayerViewModel.playbackControl()
        }

        binding.audioPlayerLikeMusicTrack.setOnClickListener {
            prepareFavoriteTrack()
        }

        val bottomSheetContainer = binding.audioPlayerBottomSheet
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.audioPlayerAddMusicTrack.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }

                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        binding.audioPlayerNewPlaylist.setOnClickListener {
            binding.fragmentContainer.visibility = View.VISIBLE
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

            openNewPlaylistFragment()
        }
    }

    override fun onPause() {
        super.onPause()
        if (audioPlayerViewModel.playerStateListener() == PlayerStatus.PLAYING) {
            audioPlayerViewModel.pausePlayer()
            binding.audioPlayerPlaySong.setImageResource(R.drawable.audio_player_play_song)
        }
    }

    private fun openNewPlaylistFragment() {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        val newPlaylistFragment = NewPlaylistFragment()
        fragmentStack.push(newPlaylistFragment)
        transaction.replace(R.id.fragment_container, newPlaylistFragment)
        transaction.commit()
    }

    private fun render(state: PlayerStateAlbum) {
        when (state) {
            is PlayerStateAlbum.Content -> showContent(state.album)
        }
    }

    private fun showContent(album: List<AlbumPlaylist>) {
        adapter?.albumListPlaylist?.clear()
        adapter?.albumListPlaylist?.addAll(album)
        adapter?.notifyDataSetChanged()
    }

    private fun renderState(state: PlayerScreenState) {
        when (state) {
            is PlayerScreenState.Loading -> {

            }

            is PlayerScreenState.Ready -> {
                processingPlayerStatus(state.playerStatus)

                val newImageRes =
                    if (state.track.isFavorite) R.drawable.audio_player_like_on else R.drawable.audio_player_like_off
                binding.audioPlayerLikeMusicTrack.setImageResource(newImageRes)
            }
        }
    }

    private fun processingPlayerStatus(status: PlayerStatus) {
        when (status) {
            PlayerStatus.PLAYING -> {
                binding.audioPlayerPlaySong.setImageResource(R.drawable.audio_player_pause_song)
            }

            PlayerStatus.PREPARED -> {
                binding.audioPlayerPlaySong.setImageResource(R.drawable.audio_player_play_song)
            }

            PlayerStatus.PAUSED -> {
                binding.audioPlayerPlaySong.setImageResource(R.drawable.audio_player_play_song)
            }

            PlayerStatus.DEFAULT -> {
                binding.audioPlayerPlaySong.setImageResource(R.drawable.audio_player_play_song)
            }
        }
    }

    fun prepareFavoriteTrack() {
        audioPlayerViewModel.onFavoriteClicked()
    }

    fun preparePlayer() {
        binding.audioPlayerPlaySong.setImageResource(R.drawable.audio_player_play_song)
        binding.audioPlayerPlaySong.isEnabled = true
    }
}