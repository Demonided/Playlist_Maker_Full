package com.katoklizm.playlist_maker_full.ui.albuminfo

import android.os.Bundle
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
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.katoklizm.playlist_maker_full.R
import com.katoklizm.playlist_maker_full.data.converters.AlbumDbConverters.getTrackQuantityString
import com.katoklizm.playlist_maker_full.databinding.FragmentAlbumInfoBinding
import com.katoklizm.playlist_maker_full.domain.album.model.AlbumPlaylist
import com.katoklizm.playlist_maker_full.domain.search.model.Track
import com.katoklizm.playlist_maker_full.presentation.albuminfo.AlbumInfoViewModel
import com.katoklizm.playlist_maker_full.ui.audioplayer.AudioPlayerFragment
import com.katoklizm.playlist_maker_full.ui.newplalist.NewPlaylistFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class AlbumInfoFragment : Fragment() {

    private val viewModel: AlbumInfoViewModel by viewModel()

    private var _binding: FragmentAlbumInfoBinding? = null
    val binding get() = _binding!!

    lateinit var adapter: AlbumInfoAdapter

    private lateinit var recyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumInfoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val selectedAlbum: AlbumPlaylist? = arguments?.getParcelable(SAVE_ALBUM)
        val gson = Gson()

        val trackListType = object : TypeToken<List<Track>>() {}.type
        val track: List<Track> = gson.fromJson(selectedAlbum?.track, trackListType) ?: emptyList()

        with(binding) {
            albumInfoTitle.text = selectedAlbum?.name
            albumInfoDescription.text = selectedAlbum?.description
            albumInfoMenuTitle.text = selectedAlbum?.name
        }

        Glide.with(this)
            .load(selectedAlbum?.image)
            .placeholder(R.drawable.vector_plug)
            .transform(CenterCrop(), RoundedCorners(10))
            .into(binding.albumInfoMenuImage)

        Glide.with(this)
            .load(selectedAlbum?.image)
            .into(binding.albumInfoImage)

        val bottomSheetContainer = binding.albumInfoBottomSheetAlbum
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.albumInfoOverlay.visibility = View.GONE
                        binding.albumInfoBack.visibility = View.VISIBLE
                    }

                    else -> {
                        binding.albumInfoOverlay.visibility = View.VISIBLE
                        binding.albumInfoBack.visibility = View.GONE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        recyclerView = binding.albumInfoRecyclerView
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter = AlbumInfoAdapter(track)
        recyclerView.adapter = adapter

        adapter.itemClickListener = { _, track ->
            findNavController().navigate(
                R.id.action_albumInfoFragment_to_audioPlayerFragment,
                AudioPlayerFragment.createArgs(track)
            )
        }

        adapter.itemLongClickListener = { _, tracks ->
            val reliableDialog = MaterialAlertDialogBuilder(requireContext())
                .setTitle("Хотите удалить трек?")
                .setNegativeButton("НЕТ") { dialog, which ->
                    // Оставляем как есть
                }.setPositiveButton("ДА") { dialog, which ->
                    if (selectedAlbum != null) {
                        viewModel.deleteTrack(selectedAlbum, tracks)
                        adapter.tracksAlbum.clear()
                        adapter.tracksAlbum.addAll(track)
                    }
                }
            reliableDialog.show()
        }

        binding.albumInfoShape.setOnClickListener {
            if (track.isNotEmpty()) {
                viewModel.shareAlbum(selectedAlbum!!)
            } else {
                Toast.makeText(
                    requireContext(),
                    "В этом плейлисте нет списка треков, которым можно поделиться",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        binding.albumInfoDots.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.albumInfoMenuShare.setOnClickListener {
            if (track.isNotEmpty()) {
                viewModel.shareAlbum(selectedAlbum!!)
            } else {
                Toast.makeText(
                    requireContext(),
                    "В этом плейлисте нет списка треков, которым можно поделиться",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        binding.albumInfoMenuEdinInformation.setOnClickListener {
            findNavController().navigate(
                R.id.action_albumInfoFragment_to_newPlaylistFragment,
                NewPlaylistFragment.createArgs(selectedAlbum)
            )
        }

        binding.albumInfoMenuDeleteAlbum.setOnClickListener {
            val deleteAlbum = MaterialAlertDialogBuilder(requireContext())
                .setTitle("Хотите удалить плейлист ${selectedAlbum!!.name}?")
                .setNegativeButton("НЕТ") { dialog, which ->
                    // Оставляем как есть
                }.setPositiveButton("ДА") { dialog, which ->
                    viewModel.deleteAlbum(selectedAlbum.id)
                    findNavController().popBackStack()
                }
            deleteAlbum.show()
        }

        binding.albumInfoBack.setOnClickListener {
            findNavController().popBackStack()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_HIDDEN) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            } else {
                findNavController().popBackStack()
            }
        }
    }

    fun calculateTotalTime(tracks: List<Track>?): String {
        var totalMilliseconds = 0L
        if (tracks != null) {
            for (track in tracks) {
                totalMilliseconds += track.trackTimeMillis!!.toLong()
            }
        }

        val trackTime = SimpleDateFormat("m", Locale.getDefault())
            .format(totalMilliseconds)

        Log.d("TotalMillisecond", "Текущее время $totalMilliseconds")

        return "$trackTime минут"
    }

    companion object {
        const val SAVE_ALBUM = "SAVE_ALBUM"
        fun createArgs(album: AlbumPlaylist): Bundle {
            return bundleOf(SAVE_ALBUM to album)
        }
    }
}