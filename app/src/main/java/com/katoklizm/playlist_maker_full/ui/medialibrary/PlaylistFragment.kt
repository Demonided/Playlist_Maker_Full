package com.katoklizm.playlist_maker_full.ui.medialibrary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.katoklizm.playlist_maker_full.databinding.FragmentPlaylistsBinding
import com.katoklizm.playlist_maker_full.presentation.medialibrary.playlist.PlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistFragment: Fragment() {

    private val playlistViewModel: PlaylistViewModel by viewModel()

    private lateinit var binding: FragmentPlaylistsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        private const val PLAYLIST = "playlist"

        fun newInstance() = PlaylistFragment().apply {
            arguments = Bundle().apply {

            }
        }
    }
}