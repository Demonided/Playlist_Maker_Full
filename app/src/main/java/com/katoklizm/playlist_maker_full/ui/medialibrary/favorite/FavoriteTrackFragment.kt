package com.katoklizm.playlist_maker_full.ui.medialibrary.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.katoklizm.playlist_maker_full.databinding.FragmentFavoriteTrackBinding
import com.katoklizm.playlist_maker_full.presentation.medialibrary.favorite_track.FavoriteTrackViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTrackFragment: Fragment() {

    private val favoriteTrackViewModel: FavoriteTrackViewModel by viewModel()

    private var _binding: FragmentFavoriteTrackBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteTrackBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val FAVORITE_TRACK = "favorite_track"

        fun newInstance() = FavoriteTrackFragment().apply {
            arguments = Bundle().apply {
//                Toast.makeText(requireContext(), "Hello", Toast.LENGTH_LONG).show()
            }
        }
    }
}