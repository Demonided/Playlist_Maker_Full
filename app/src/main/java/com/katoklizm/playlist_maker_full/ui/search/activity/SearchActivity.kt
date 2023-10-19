package com.katoklizm.playlist_maker_full.ui.search.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.katoklizm.playlist_maker_full.R
import com.katoklizm.playlist_maker_full.databinding.ActivitySearchBinding
import com.katoklizm.playlist_maker_full.ui.audioplayer.activity.AudioPlayerActivity
import com.katoklizm.playlist_maker_full.data.ConstTrack.SAVE_TRACK
import com.katoklizm.playlist_maker_full.domain.player.SearchState
import com.katoklizm.playlist_maker_full.domain.search.model.Track
import com.katoklizm.playlist_maker_full.ui.search.viewmodel.SearchViewModel
import com.katoklizm.playlist_maker_full.ui.search.TrackAdapter

class SearchActivity : AppCompatActivity(), TrackAdapter.OnSaveTrackManagersClickListener {
    var binding: ActivitySearchBinding? = null

    companion object {
        private const val SEARCH_TEXT = "SEARCH_TEXT"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { searchTrack() }

    private val trackAdapter = TrackAdapter(this)

    private var isClickAllowed = true

    private var enteredText: String = ""

    private val trackHistoryList = ArrayList<Track>()
    private val trackList = ArrayList<Track>()

    private lateinit var searchClearButton: ImageView
    private lateinit var searchUpdatePage: Button
    private lateinit var searchClearHistory: Button
    private lateinit var searchEditText: EditText
    private lateinit var searchProgressBar: ProgressBar
    private lateinit var searchLinerLayoutHistoryTrack: Group
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchErrorImage: LinearLayout
    private lateinit var searchNothingFound: LinearLayout

    private lateinit var viewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        initView()

        viewModel =
            ViewModelProvider(this, SearchViewModel.getModelFactory())[SearchViewModel::class.java]

        viewModel.observeState().observe(this) {
            render(it)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = trackAdapter
        trackAdapter.tracks = trackHistoryList

        binding?.settingBack?.setOnClickListener {
            finish()
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchClearButton.visibility = clearButtonVisibility(s)
                viewModel.onTextChanged(s.toString())
                searchDebounce()
            }

            override fun afterTextChanged(s: Editable?) {
                enteredText = searchEditText.text.toString()
            }
        }

        searchEditText.addTextChangedListener(textWatcher)

        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchTrack()
                true
            }
            false
        }

        searchEditText.setOnFocusChangeListener { _, hasFocus ->
            viewModel.onFocusChanged(hasFocus, searchEditText.text.toString())
        }

        searchClearButton.setOnClickListener {
            searchEditText.text.clear()
            closeKeyboard()
        }

        searchUpdatePage.setOnClickListener {
            viewModel.refreshSearchButton(enteredText!!)
        }

        searchClearHistory.setOnClickListener {
            viewModel.clearSearchHistory()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, enteredText)
    }

    private fun initView() {
        // Кнопка очистки EditText
        searchClearButton = findViewById(R.id.search_clear_button)
        // Кнопка обновить список при отсутствии интернета
        searchUpdatePage = findViewById(R.id.search_update_page)
        // Кнопка очистить историю сохраненых треков
        searchClearHistory = findViewById(R.id.search_clear_history)
        // EditText в главном меню
        searchEditText = findViewById(R.id.search_edit_text)
        // ProgressBar при поиске треков
        searchProgressBar = findViewById(R.id.search_progressBar)
        // Liner для отображения списка сохраненых треков
        searchLinerLayoutHistoryTrack = findViewById(R.id.search_liner_layout_history_track)
        // Liner при отсутсвии интернета
        searchErrorImage = findViewById(R.id.search_error_image)
        // Liner при отсутсвии контента поиска
        searchNothingFound = findViewById(R.id.search_nothing_found)
        // Recycler для отображения треков из интернета и сохраненых
        recyclerView = findViewById(R.id.search_recycler_music_track)
    }

    private fun render(state: SearchState) {
        when(state) {
            is SearchState.Loading -> showLoading()
            is SearchState.Empty -> showEmpty()
            is SearchState.Error -> showError()
            is SearchState.ContentListSearchTrack -> showContentListSearchTrack(state.track)
            is SearchState.ContentListSaveTrack -> showContentListSaveTrack(state.track)
        }
    }

    private fun closeKeyboard() {
        val imputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imputMethodManager.hideSoftInputFromWindow(searchEditText.windowToken, 0)
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

    fun showLoading() {
        searchLinerLayoutHistoryTrack.visibility = View.GONE
        searchErrorImage.visibility = View.GONE
        searchNothingFound.visibility = View.GONE
        recyclerView.visibility = View.GONE
        searchProgressBar.visibility = View.VISIBLE
    }

    fun showEmpty() {
        searchLinerLayoutHistoryTrack.visibility = View.GONE
        searchErrorImage.visibility = View.GONE
        searchNothingFound.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        searchProgressBar.visibility = View.GONE
    }

    fun showError() {
        searchLinerLayoutHistoryTrack.visibility = View.GONE
        searchErrorImage.visibility = View.VISIBLE
        searchNothingFound.visibility = View.GONE
        recyclerView.visibility = View.GONE
        searchProgressBar.visibility = View.GONE
    }

    fun showContentListSearchTrack(track: List<Track>) {
        searchLinerLayoutHistoryTrack.visibility = View.GONE
        searchErrorImage.visibility = View.GONE
        searchNothingFound.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        searchProgressBar.visibility = View.GONE

        trackList.clear()
        trackList.addAll(track)
        trackAdapter.tracks = trackList
        trackAdapter.updateTrackList(trackList)
        trackAdapter.notifyDataSetChanged()
    }

    fun showContentListSaveTrack(trackHistory: List<Track>) {
        searchLinerLayoutHistoryTrack.visibility = View.VISIBLE
        searchErrorImage.visibility = View.GONE
        searchNothingFound.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        searchProgressBar.visibility = View.GONE

        trackAdapter.tracks = trackHistory as ArrayList<Track>
        trackAdapter.updateTrackList(trackHistory)
        trackAdapter.notifyDataSetChanged()
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
        val intent = Intent(this, AudioPlayerActivity::class.java)
        intent.putExtra(SAVE_TRACK, track)
        startActivity(intent)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onButtonRecyclerViewSaveTrack(track: Track) {
        if (clickDebounce()) {
            viewModel.onTrackPresent(track)
            openAudioPlayer(track)
        }
    }
}

