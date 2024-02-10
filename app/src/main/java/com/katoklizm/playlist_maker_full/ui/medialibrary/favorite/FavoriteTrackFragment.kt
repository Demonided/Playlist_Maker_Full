package com.katoklizm.playlist_maker_full.ui.medialibrary.favorite

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.katoklizm.playlist_maker_full.R
import com.katoklizm.playlist_maker_full.data.ConstTrack
import com.katoklizm.playlist_maker_full.databinding.FragmentFavoriteTrackBinding
import com.katoklizm.playlist_maker_full.domain.search.model.Track
import com.katoklizm.playlist_maker_full.presentation.medialibrary.favorite_track.FavoriteTrackState
import com.katoklizm.playlist_maker_full.presentation.medialibrary.favorite_track.FavoriteTrackViewModel
import com.katoklizm.playlist_maker_full.ui.audioplayer.AudioPlayerFragment
import com.katoklizm.playlist_maker_full.ui.search.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTrackFragment : Fragment(), TrackAdapter.OnSaveTrackManagersClickListener  {

    private var _binding: FragmentFavoriteTrackBinding? = null
    private val binding get() = _binding!!

    private var isClickAllowed = true

    private val favoriteTrackViewModel: FavoriteTrackViewModel by viewModel()
    private var adapter: TrackAdapter? = null

    private val handler = Handler(Looper.getMainLooper())

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

        adapter = TrackAdapter(this)

        binding.favoriteTrackRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.favoriteTrackRecycler.adapter = adapter

        favoriteTrackViewModel.fillData()

        favoriteTrackViewModel.favoriteTrackState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    override fun onButtonRecyclerViewSaveTrack(track: Track) {
        if (clickDebounce()) {
            openAudioPlayer(track)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun render(state: FavoriteTrackState) {
        when (state) {
            is FavoriteTrackState.Content -> showContent(state.tracks)
            is FavoriteTrackState.EmptyList -> showEmpty()
            is FavoriteTrackState.Loading -> showLoading()
        }
    }

    private fun showContent(track: List<Track>) {
        binding.favoriteTrackEmpty.visibility = View.GONE
        binding.favoriteTrackRecycler.visibility = View.VISIBLE
        binding.favoriteTrackProgressBar.visibility = View.GONE

        adapter?.tracks?.clear()
        adapter?.tracks?.addAll(track)
        adapter?.notifyDataSetChanged()
    }

    private fun showEmpty() {
        binding.favoriteTrackEmpty.visibility = View.VISIBLE
        binding.favoriteTrackRecycler.visibility = View.GONE
        binding.favoriteTrackProgressBar.visibility = View.GONE
    }

    private fun showLoading() {
        binding.favoriteTrackEmpty.visibility = View.GONE
        binding.favoriteTrackRecycler.visibility = View.GONE
        binding.favoriteTrackProgressBar.visibility = View.VISIBLE
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun openAudioPlayer(track: Track) {
        findNavController().navigate(
            R.id.action_mediaLibraryFragment_to_audioPlayerFragment,
            AudioPlayerFragment.createArgs(track)
        )
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L

        fun newInstance() = FavoriteTrackFragment().apply {
            arguments = Bundle().apply {
//                Toast.makeText(requireContext(), "Hello", Toast.LENGTH_LONG).show()
            }
        }
    }
}