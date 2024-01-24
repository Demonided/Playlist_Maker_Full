package com.katoklizm.playlist_maker_full.ui.medialibrary.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.katoklizm.playlist_maker_full.databinding.FragmentFavoriteTrackBinding
import com.katoklizm.playlist_maker_full.domain.search.model.Track
import com.katoklizm.playlist_maker_full.presentation.medialibrary.favorite_track.FavoriteTrackState
import com.katoklizm.playlist_maker_full.presentation.medialibrary.favorite_track.FavoriteTrackViewModel
import com.katoklizm.playlist_maker_full.ui.search.SearchFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTrackFragment : Fragment() {

    private var _binding: FragmentFavoriteTrackBinding? = null
    private val binding get() = _binding!!

    private val favoriteTrackViewModel: FavoriteTrackViewModel by viewModel()
    private var adapter: FavoriteTrackAdapter? = null

    private lateinit var favorite_track_progressBar: ProgressBar
    private lateinit var favorite_track_recycler: RecyclerView
    private lateinit var favorite_track_empty: LinearLayout

    private var isClickAllowed = true

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

        adapter = FavoriteTrackAdapter()

        favorite_track_progressBar = binding.favoriteTrackProgressBar
        favorite_track_recycler = binding.favoriteTrackRecycler
        favorite_track_empty = binding.favoriteTrackEmpty

        favorite_track_recycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        favorite_track_recycler.adapter = adapter

        favoriteTrackViewModel.fillData()

        favoriteTrackViewModel.favoriteTrackState().observe(viewLifecycleOwner) {
            render(it)
        }

    }

    private fun render(state: FavoriteTrackState) {
        when (state) {
            is FavoriteTrackState.Content -> showContent(state.tracks)
            is FavoriteTrackState.EmptyList -> showEmpty()
            is FavoriteTrackState.Loading -> showLoading()
        }
    }

    fun showContent(track: List<Track>) {
        favorite_track_empty.visibility = View.GONE
        favorite_track_recycler.visibility = View.VISIBLE
        favorite_track_progressBar.visibility = View.GONE

        adapter?.tracks?.clear()
        adapter?.tracks?.addAll(track)
        adapter?.notifyDataSetChanged()
    }

    fun showEmpty() {
        favorite_track_empty.visibility = View.VISIBLE
        favorite_track_recycler.visibility = View.GONE
        favorite_track_progressBar.visibility = View.GONE
    }

    fun showLoading() {
        favorite_track_empty.visibility = View.GONE
        favorite_track_recycler.visibility = View.GONE
        favorite_track_progressBar.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
        }
        return current
    }



    companion object {
        private const val FAVORITE_TRACK = "favorite_track"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L

        fun newInstance() = FavoriteTrackFragment().apply {
            arguments = Bundle().apply {
//                Toast.makeText(requireContext(), "Hello", Toast.LENGTH_LONG).show()
            }
        }
    }
}