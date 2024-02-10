package com.katoklizm.playlist_maker_full.ui.medialibrary.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.katoklizm.playlist_maker_full.R
import com.katoklizm.playlist_maker_full.databinding.FragmentPlaylistsBinding
import com.katoklizm.playlist_maker_full.domain.album.model.AlbumPlaylist
import com.katoklizm.playlist_maker_full.presentation.medialibrary.playlist.PlaylistState
import com.katoklizm.playlist_maker_full.presentation.medialibrary.playlist.PlaylistViewModel
import com.katoklizm.playlist_maker_full.ui.albuminfo.AlbumInfoFragment
import com.katoklizm.playlist_maker_full.ui.newplalist.NewPlaylistFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment() {

    private val playlistViewModel: PlaylistViewModel by viewModel()

    private lateinit var binding: FragmentPlaylistsBinding

    lateinit var adapter: PlaylistAdapter

    private lateinit var recyclerView: RecyclerView
    private lateinit var linerLayout: LinearLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var buttonCreate: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = PlaylistAdapter()

        recyclerView = binding.playlistRecyclerView
        linerLayout = binding.favoriteTrackEmpty
        progressBar = binding.playlistProgressBar
        buttonCreate = binding.playlistNewPlaylist

        recyclerView.layoutManager =
            GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = adapter

        buttonCreate.setOnClickListener {
            findNavController().navigate(
                R.id.action_mediaLibraryFragment_to_newPlaylistFragment
            )
        }

        adapter.itemClickListener = { _, playlist ->
            findNavController().navigate(
                R.id.action_mediaLibraryFragment_to_albumInfoFragment,
                AlbumInfoFragment.createArgs(playlist)
            )
        }

        playlistViewModel.fillData()

        playlistViewModel.playlistState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun render(state: PlaylistState) {
        when (state) {
            is PlaylistState.Content -> showContent(state.album)
            is PlaylistState.EmptyList -> showEmpty()
            is PlaylistState.Loading -> showLoading()
        }
    }

    private fun showContent(album: List<AlbumPlaylist>) {
        linerLayout.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        progressBar.visibility = View.GONE

        adapter.albumPlaylist.clear()
        adapter.albumPlaylist.addAll(album)
        adapter.notifyDataSetChanged()
    }

    private fun showEmpty() {
        linerLayout.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
        recyclerView.visibility = View.GONE
    }

    private fun showLoading() {
        recyclerView.visibility = View.GONE
        linerLayout.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    companion object {
        private const val PLAYLIST = "playlist"

        private const val ARGS_TRACK_ID = "track_id"

        fun newInstance() = PlaylistFragment().apply {
            arguments = Bundle().apply {

            }
        }
    }
}