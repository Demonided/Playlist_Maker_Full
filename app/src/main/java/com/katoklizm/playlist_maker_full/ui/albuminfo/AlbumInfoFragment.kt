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
import org.koin.androidx.viewmodel.ext.android.viewModel

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
        adapter = AlbumInfoAdapter()
        recyclerView.adapter = adapter

        viewModel.getAlbumPlaylist()
        viewModel.fillData()

        adapter.itemClickListener = { _, track ->
            findNavController().navigate(
                R.id.action_albumInfoFragment_to_audioPlayerFragment,
                AudioPlayerFragment.createArgs(track)
            )
        }

        viewModel.stateAlbum.observe(viewLifecycleOwner) { albumPlaylist ->
            val gson = Gson()
            val trackListType = object : TypeToken<List<Track>>() {}.type
            val tracks: List<Track> = gson.fromJson(albumPlaylist?.track, trackListType) ?: emptyList()
            with(binding) {
                albumInfoTitle.text = albumPlaylist?.name
                albumInfoMenuTitle.text = albumPlaylist?.name
                albumInfoDescription.text = albumPlaylist?.description
                albumInfoTime.text = calculateTotalTime(tracks)
                albumInfoQuantity.text = albumPlaylist?.getTrackQuantityString(requireContext())
                albumInfoMenuQuantity.text = albumPlaylist?.getTrackQuantityString(requireContext())

                val radiusDp = 10 // значение радиуса в DP
                val density = resources.displayMetrics.density // получаем плотность экрана в пикселях на дюйм
                val radiusPx = (radiusDp * density + 0.5f).toInt() // конвертируем DP в пиксели с учетом плотности

                Glide.with(requireContext())
                    .load(albumPlaylist?.image)
                    .placeholder(R.drawable.vector_plug)
                    .into(albumInfoImage)

                Glide.with(requireContext())
                    .load(albumPlaylist?.image)
                    .placeholder(R.drawable.vector_plug)
                    .transform(CenterCrop(), RoundedCorners(radiusPx))
                    .into(albumInfoMenuImage)
            }

            adapter.itemLongClickListener = { _, track ->
                val reliableDialog = MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.album_info_delete_track)
                    .setNegativeButton(R.string.album_info_delete_no) { dialog, which ->
                        // Оставляем как есть
                    }.setPositiveButton(R.string.album_info_delete_yes) { dialog, which ->
                        if (albumPlaylist != null) {
                            viewModel.deleteTrack(albumPlaylist, track)
                            adapter.tracksAlbum.clear()
                            adapter.tracksAlbum.addAll(tracks)
                        }
                    }
                reliableDialog.show()
            }

            binding.albumInfoShape.setOnClickListener {
                if (tracks.isNotEmpty()) {
                    viewModel.shareAlbum(albumPlaylist!!)
                } else {
                    Toast.makeText(
                        requireContext(),
                        R.string.album_info_no_list_track,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            binding.albumInfoMenuShare.setOnClickListener {
                if (tracks.isNotEmpty()) {
                    viewModel.shareAlbum(albumPlaylist!!)
                } else {
                    Toast.makeText(
                        requireContext(),
                        R.string.album_info_no_list_track,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            binding.albumInfoMenuDeleteAlbum.setOnClickListener {
                val deleteAlbum = MaterialAlertDialogBuilder(requireContext())
                    .setTitle("${getString(R.string.album_info_delete_album)} ${albumPlaylist!!.name}?")
                    .setNegativeButton(R.string.album_info_delete_no) { dialog, which ->
                        // Оставляем как есть
                    }.setPositiveButton(R.string.album_info_delete_yes) { dialog, which ->
                        viewModel.deleteAlbum(albumId = albumPlaylist.id)
                        findNavController().popBackStack()
                    }
                deleteAlbum.show()
            }
        }

        viewModel.stateAlbumTrack.observe(viewLifecycleOwner) {
            adapter.tracksAlbum.clear()
            adapter.tracksAlbum.addAll(it?.reversed() ?: emptyList())
            adapter.notifyDataSetChanged()
        }

        binding.albumInfoDots.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.albumInfoMenuEdinInformation.setOnClickListener {
            findNavController().navigate(
                R.id.action_albumInfoFragment_to_newPlaylistFragment
            )
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun calculateTotalTime(tracks: List<Track>?): String? {
        var totalMilliseconds = 0L
        if (tracks != null) {
            for (track in tracks) {
                totalMilliseconds += track.trackTimeMillis!!.toLong()
            }
        }

        val totalSecond = totalMilliseconds / 60000
        val trackTime = context?.resources?.getQuantityString(
            R.plurals.track_time,
            totalSecond.toInt(),
            totalSecond.toInt()
        )

        return trackTime
    }

    companion object {
        const val SAVE_ALBUM = "SAVE_ALBUM"
        fun createArgs(album: AlbumPlaylist): Bundle {
//            return bundleOf(SAVE_ALBUM to album)
            return bundleOf()
        }
    }
}