package com.katoklizm.playlist_maker_full.ui.audioplayer

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.katoklizm.playlist_maker_full.R
import com.katoklizm.playlist_maker_full.data.ConstTrack
import com.katoklizm.playlist_maker_full.databinding.FragmentAudioPlayerBinding
import com.katoklizm.playlist_maker_full.domain.album.model.AlbumPlaylist
import com.katoklizm.playlist_maker_full.domain.search.model.Track
import com.katoklizm.playlist_maker_full.presentation.audioplayer.AudioPlayerViewModel
import com.katoklizm.playlist_maker_full.presentation.medialibrary.playlist.PlayerScreenState
import com.katoklizm.playlist_maker_full.presentation.medialibrary.playlist.PlayerStateAlbum
import com.katoklizm.playlist_maker_full.presentation.medialibrary.playlist.PlayerStatus
import com.katoklizm.playlist_maker_full.ui.newplalist.NewPlaylistFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Stack

class AudioPlayerFragment : Fragment() {

    private val audioPlayerViewModel by viewModel<AudioPlayerViewModel>()

    private var _binding: FragmentAudioPlayerBinding? = null
    val binding get() = _binding!!

    private var mainThreadHandler: Handler? = null

    private var timerRunnable: Runnable? = null

    private var playerStatus = PlayerStatus.DEFAULT

    lateinit var adapter: AudioPlayerAdapter

    private lateinit var recyclerView: RecyclerView

    private val fragmentStack: Stack<Fragment> = Stack()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAudioPlayerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val selectedTrack: Track? = arguments?.getParcelable(SAVE_TRACK)
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

        val bottomSheetContainer = binding.audioPlayerBottomSheet
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        playerStatus = audioPlayerViewModel.playerStateListener()

        adapter = AudioPlayerAdapter(requireContext())

        recyclerView = binding.audioPlayerRecyclerView

        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter

        adapter.itemClickListener = { _, playlist ->
            val selectedTrack: Track? = arguments?.getParcelable(SAVE_TRACK)
            if (selectedTrack != null) {
                audioPlayerViewModel.onPlaylistClicked(playlist, selectedTrack) { trackAdded ->
                    if (trackAdded) {
                        // Если трек добавлен в альбом, закрываем BottomSheet
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    }
                }
            }
        }

        audioPlayerViewModel.messageTrack.observe(viewLifecycleOwner) { message ->
            val toast = Toast.makeText(
                requireContext(),
                message,
                Toast.LENGTH_SHORT
            )
            toast.show()
        }

        audioPlayerViewModel.addAlbumPlaylist()

        audioPlayerViewModel.albumState.observe(viewLifecycleOwner) {
            render(it)
        }

        audioPlayerViewModel.playerState.observe(viewLifecycleOwner) {
            renderState(it)
        }

        audioPlayerViewModel.timerState.observe(viewLifecycleOwner) {
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
            findNavController().popBackStack()
        }

        binding.audioPlayerPlaySong.setOnClickListener {
            audioPlayerViewModel.playbackControl()
        }

        binding.audioPlayerLikeMusicTrack.setOnClickListener {
            prepareFavoriteTrack()
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
                        binding.audioPlayerLikeMusicTrack.isEnabled = true
                        binding.audioPlayerPlaySong.isEnabled = true
                        binding.audioPlayerBack.isEnabled = true
                    }

                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                        binding.audioPlayerLikeMusicTrack.isEnabled = false
                        binding.audioPlayerPlaySong.isEnabled = false
                        binding.audioPlayerBack.isEnabled = false
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        binding.audioPlayerNewPlaylist.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

            findNavController().navigate(
                R.id.action_audioPlayerFragment_to_newPlaylistFragment
            )
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_HIDDEN) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            } else {
                findNavController().popBackStack()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (audioPlayerViewModel.playerStateListener() == PlayerStatus.PLAYING) {
            audioPlayerViewModel.pausePlayer()
            binding.audioPlayerPlaySong.setImageResource(R.drawable.audio_player_play_song)
        }
    }

    private fun render(state: PlayerStateAlbum) {
        when (state) {
            is PlayerStateAlbum.Content -> showContent(state.album)
        }
    }

    private fun showContent(album: List<AlbumPlaylist>) {
        adapter.albumListPlaylist.clear()
        adapter.albumListPlaylist.addAll(album)
        adapter.notifyDataSetChanged()
    }

    private fun renderState(state: PlayerScreenState) {
        when (state) {
            is PlayerScreenState.Loading -> {}

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

    companion object {

        private const val SAVE_TRACK = "SAVE_TRACK"

        fun createArgs(track: Track): Bundle {
            return bundleOf(SAVE_TRACK to track)
        }
    }
}