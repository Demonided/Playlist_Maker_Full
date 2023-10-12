package com.katoklizm.playlist_maker_full.presentation

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.katoklizm.playlist_maker_full.R
import com.katoklizm.playlist_maker_full.util.Creator
import com.katoklizm.playlist_maker_full.domain.search.api.TrackInteractor
import com.katoklizm.playlist_maker_full.domain.search.model.Track
import com.katoklizm.playlist_maker_full.data.search.track.HistoryTrackManager
import com.katoklizm.playlist_maker_full.ui.track.TrackAdapter

class TrackSearchController(private val activity: Activity,
                            private val adapter: TrackAdapter) {

    private val trackInteractor = Creator.provideTrackInteractor(activity)

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private lateinit var historyTrackManager: HistoryTrackManager
    private lateinit var searchClearButton: ImageView
    private lateinit var searchUpdatePage: Button
    private lateinit var searchClearHistory: Button
    private lateinit var searchEditText: EditText
    private lateinit var searchProgressBar: ProgressBar
    private lateinit var searchLinerLayoutHistoryTrack: Group
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchErrorImage: LinearLayout
    private lateinit var searchNothingFound: LinearLayout

    private val trackList = ArrayList<Track>()

    private val trackHistoryList = ArrayList<Track>()

    private val handler = Handler(Looper.getMainLooper())

    private val searchRunnable = Runnable { updatePageSearch() }

    fun onCreate() {
        searchClearButton = activity.findViewById(R.id.search_clear_button)
        searchUpdatePage = activity.findViewById(R.id.search_update_page)
        searchClearHistory = activity.findViewById(R.id.search_clear_history)
        searchEditText = activity.findViewById(R.id.search_edit_text)
        searchProgressBar = activity.findViewById(R.id.search_progressBar)
        searchLinerLayoutHistoryTrack = activity.findViewById(R.id.search_liner_layout_history_track)
        searchErrorImage = activity.findViewById(R.id.search_error_image)
        searchNothingFound = activity.findViewById(R.id.search_nothing_found)

        adapter.tracks = trackList

        historyTrackManager = HistoryTrackManager(activity)

        searchClearButton.setOnClickListener {
            searchEditText.text.clear()
            searchProgressBar.visibility = View.GONE
            hideSoftKeyboard()
            trackList.clear()
            adapter.notifyDataSetChanged()
            adapter.updateTrackList(trackHistoryList)
        }

        searchUpdatePage.setOnClickListener {
            updatePageSearch()
        }

        searchClearHistory.setOnClickListener {
            historyTrackManager.prefs.edit().clear().apply()
            searchLinerLayoutHistoryTrack.visibility = View.GONE
            adapter.tracks.clear()
            trackHistoryList.clear()
            adapter.notifyDataSetChanged()
        }

        searchMusicTrack()
        examinationFocusEditText()

        recyclerView = activity.findViewById(R.id.search_recycler_music_track)

        adapter.notifyDataSetChanged()

        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter
        adapter.tracks = trackHistoryList

        searchEditText.doOnTextChanged { text, start, before, count ->
            searchLinerLayoutHistoryTrack.visibility =
                if (searchEditText.hasFocus() && text?.isEmpty() == true && trackHistoryList.size > 0) View.VISIBLE else View.GONE

            when (text!!.length) {
                0 -> {
                    searchClearButton.visibility = View.GONE
                    searchProgressBar.visibility = View.GONE
                }
                else -> searchClearButton.visibility = View.VISIBLE
            }

            if (text.isNotEmpty()) {
                searchDebounce()
                trackList.clear()
            }

            if (searchEditText.text!!.isEmpty()) {
                searchNothingFound.visibility = View.GONE
                searchErrorImage.visibility = View.GONE
                adapter.updateTrackList(trackHistoryList)
                adapter.notifyDataSetChanged()
            } else {
                adapter.updateTrackList(trackList)
                adapter.notifyDataSetChanged()
            }
        }
    }

    fun onDestroy() {
        handler.removeCallbacks(searchRunnable)
    }

    private fun examinationFocusEditText() {
        searchEditText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus && historyTrackManager.getHistory().size > 0) {
               searchLinerLayoutHistoryTrack.visibility = View.VISIBLE
                trackHistoryList.addAll(historyTrackManager.getHistory())
                adapter.updateTrackList(trackHistoryList)
            } else {
                searchLinerLayoutHistoryTrack.visibility = View.GONE
            }
        }
    }

    private fun hideSoftKeyboard() {
        val hide =
            activity.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        hide.hideSoftInputFromWindow(
            searchEditText.windowToken,
            0
        )
    }


    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        val searchText = searchEditText.text.toString().trim()
        if (searchText.isNotEmpty()) {
            handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
        } else {
            trackList.clear()
            adapter.notifyDataSetChanged()
            searchProgressBar.visibility = View.GONE
        }
    }

    private fun updatePageSearch() {
        if (searchEditText.text!!.isNotEmpty()) {

            searchProgressBar.visibility = View.VISIBLE

            trackInteractor.searchTrack(
                searchEditText.text.toString(),
                object :
                    TrackInteractor.TrackConsumer {
                    override fun consume(foundTrack: List<Track>?, errorMessage: String?) {
                        handler.post {
                            searchProgressBar.visibility = View.GONE

                            if (foundTrack != null) {
                                trackList.clear()
                                trackList.addAll(foundTrack)
                                searchNothingFound.visibility = View.GONE
                                searchErrorImage.visibility = View.GONE
                            }

                            if (errorMessage != null) {
                                showMessage(
                                    searchErrorImage,
                                    searchNothingFound,
                                    errorMessage
                                )
                            } else if (trackList.isEmpty()) {
                                // Если список треков после поиска пуст
                                showMessage(
                                    searchNothingFound,
                                    searchErrorImage,
                                    "GG"
                                )
                            } else {
                                adapter.updateTrackList(trackList)
                                adapter.notifyDataSetChanged()
                            }

                        }
                    }
                })
        }
    }

    private fun searchMusicTrack() {
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (searchEditText.text.isNotEmpty()) {
                    updatePageSearch()
                }
            }
            false
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showMessage(
        oneLinear: ViewGroup,
        twoLinear: ViewGroup,
        toastMakeText: String
    ) {
        oneLinear.visibility = View.VISIBLE
        twoLinear.visibility = View.GONE
        trackList.clear()
        adapter.notifyDataSetChanged()
        if (toastMakeText.isNotEmpty()) {
            Toast.makeText(
                activity,
                toastMakeText,
                Toast.LENGTH_LONG
            ).show()
        }
    }
}