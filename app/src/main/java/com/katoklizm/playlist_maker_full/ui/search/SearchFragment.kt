package com.katoklizm.playlist_maker_full.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.katoklizm.playlist_maker_full.R
import com.katoklizm.playlist_maker_full.ui.audioplayer.AudioPlayerFragment
import com.katoklizm.playlist_maker_full.data.ConstTrack.SAVE_TRACK
import com.katoklizm.playlist_maker_full.databinding.FragmentSearchBinding
import com.katoklizm.playlist_maker_full.domain.search.SearchState
import com.katoklizm.playlist_maker_full.domain.search.model.Track
import com.katoklizm.playlist_maker_full.presentation.search.SearchViewModel
import com.katoklizm.playlist_maker_full.ui.medialibrary.playlist.PlaylistFragment
import com.katoklizm.playlist_maker_full.ui.newplalist.NewPlaylistFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment(), TrackAdapter.OnSaveTrackManagersClickListener {

    private var _binding: FragmentSearchBinding? = null
    private val binding: FragmentSearchBinding
        get() = _binding!!

    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { searchTrack() }

    private val trackAdapter = TrackAdapter(this)

    private var isClickAllowed = true

    private var enteredText: String = ""

    private val viewModel by viewModel<SearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        viewModel.isScreenPaused().observe(viewLifecycleOwner) {

        }

        binding.searchRecyclerMusicTrack.layoutManager = LinearLayoutManager(requireContext())
        binding.searchRecyclerMusicTrack.adapter = trackAdapter
        trackAdapter.tracks = viewModel.trackHistoryList

        val saveTrack = viewModel.trackHistoryList

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.searchClearButton.visibility = clearButtonVisibility(s)
                viewModel.onTextChanged(s.toString())
                searchDebounce()
            }

            override fun afterTextChanged(s: Editable?) {
                enteredText = binding.searchEditText.text.toString()
            }
        }

        binding.searchEditText.addTextChangedListener(textWatcher)

        binding.searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchTrack()
                true
            }
            false
        }

        binding.searchEditText.setOnFocusChangeListener { _, hasFocus ->
            viewModel.onFocusChanged(hasFocus, binding.searchEditText.text.toString())
        }

        binding.searchClearButton.setOnClickListener {
            binding.searchEditText.text?.clear()
            closeKeyboard()
            showContentListSaveTrack(saveTrack)
        }

        binding.searchUpdatePage.setOnClickListener {
            viewModel.refreshSearchButton(enteredText)
        }

        binding.searchClearHistory.setOnClickListener {
            viewModel.clearSearchHistory()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, enteredText)
    }

    override fun onPause() {
        super.onPause()
        viewModel.refreshSearchButton("")
        viewModel.activeFragment(false)
    }

    override fun onResume() {
        super.onResume()
        viewModel.activeFragment(true)
    }

    private fun render(state: SearchState) {
        when(state) {
            is SearchState.Loading -> showLoading()
            is SearchState.Empty -> showEmpty()
            is SearchState.Error -> showError()
            is SearchState.ContentListSearchTrack -> showContentListSearchTrack(state.track)
            is SearchState.ContentListSaveTrack -> showContentListSaveTrack(state.track)
            is SearchState.EmptyScreen -> showEmptyScreen()
        }
    }

    private fun closeKeyboard() {
        val imputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imputMethodManager.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0)


    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun searchTrack() {
        if (enteredText.isNotEmpty()) {
            viewModel.searchRequest(enteredText)
        }
    }

    private fun showLoading() {
        binding.searchLinerLayoutHistoryTrack.visibility = View.GONE
        binding.searchErrorImage.visibility = View.GONE
        binding.searchNothingFound.visibility = View.GONE
        binding.searchRecyclerMusicTrack.visibility = View.GONE
        binding.searchProgressBar.visibility = View.VISIBLE
    }

    private fun showEmpty() {
        binding.searchLinerLayoutHistoryTrack.visibility = View.GONE
        binding.searchErrorImage.visibility = View.GONE
        binding.searchNothingFound.visibility = View.VISIBLE
        binding.searchRecyclerMusicTrack.visibility = View.GONE
        binding.searchProgressBar.visibility = View.GONE
    }

    private fun showError() {
        binding.searchLinerLayoutHistoryTrack.visibility = View.GONE
        binding.searchErrorImage.visibility = View.VISIBLE
        binding.searchNothingFound.visibility = View.GONE
        binding.searchRecyclerMusicTrack.visibility = View.GONE
        binding.searchProgressBar.visibility = View.GONE
    }

    private fun showContentListSearchTrack(track: List<Track>) {
        binding.searchLinerLayoutHistoryTrack.visibility = View.GONE
        binding.searchErrorImage.visibility = View.GONE
        binding.searchNothingFound.visibility = View.GONE
        binding.searchRecyclerMusicTrack.visibility = View.VISIBLE
        binding.searchProgressBar.visibility = View.GONE

        viewModel.trackList.clear()
        viewModel.trackList.addAll(track)
        trackAdapter.tracks = viewModel.trackList
        trackAdapter.updateTrackList(viewModel.trackList)
        trackAdapter.notifyDataSetChanged()
    }

    private fun showContentListSaveTrack(trackHistory: List<Track>) {
        binding.searchLinerLayoutHistoryTrack.visibility = View.VISIBLE
        binding.searchErrorImage.visibility = View.GONE
        binding.searchNothingFound.visibility = View.GONE
        binding.searchRecyclerMusicTrack.visibility = View.VISIBLE
        binding.searchProgressBar.visibility = View.GONE

        trackAdapter.tracks = trackHistory as ArrayList<Track>
        trackAdapter.updateTrackList(trackHistory)
        trackAdapter.notifyDataSetChanged()
    }

    private fun showEmptyScreen() {
        binding.searchLinerLayoutHistoryTrack.visibility = View.GONE
        binding.searchErrorImage.visibility = View.GONE
        binding.searchNothingFound.visibility = View.GONE
        binding.searchRecyclerMusicTrack.visibility = View.GONE
        binding.searchProgressBar.visibility = View.GONE
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
            R.id.action_searchFragment_to_audioPlayerFragment,
            AudioPlayerFragment.createArgs(track)
            )
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onButtonRecyclerViewSaveTrack(track: Track) {
        if (clickDebounce()) {
            viewModel.onTrackPresent(track)
            openAudioPlayer(track)
        }
    }

    companion object {
        private const val SEARCH_TEXT = "SEARCH_TEXT"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}

