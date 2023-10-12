package com.katoklizm.playlist_maker_full.ui.track

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.katoklizm.playlist_maker_full.util.Creator
import com.katoklizm.playlist_maker_full.databinding.ActivitySearchBinding
import com.katoklizm.playlist_maker_full.ui.audioplayer.AudioPlayerActivity
import com.katoklizm.playlist_maker_full.data.ConstTrack.SAVE_TRACK
import com.katoklizm.playlist_maker_full.data.ConstTrack.USER_TEXT
import com.katoklizm.playlist_maker_full.data.search.track.HistoryTrackManager
import com.katoklizm.playlist_maker_full.domain.search.model.Track

class SearchActivity : AppCompatActivity(), TrackAdapter.OnSaveTrackManagersClickListener {

    var binding: ActivitySearchBinding? = null

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private val handler = Handler(Looper.getMainLooper())

    private val trackAdapter = TrackAdapter(this)

    private val trackSearchController = Creator.provideTrackSearchController(this, trackAdapter)

    private var isClickAllowed = true

    private var enteredText: String? = ""

    private val trackHistoryList = ArrayList<Track>()

    lateinit var historyTrackManager: HistoryTrackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        historyTrackManager = HistoryTrackManager(this)

        trackSearchController.onCreate()

        binding?.settingBack?.setOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        trackSearchController.onDestroy()
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        enteredText = binding?.searchEditText?.text.toString()
        outState.putString(USER_TEXT, enteredText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        enteredText = savedInstanceState.getString(USER_TEXT)
        binding?.searchEditText?.setText(enteredText)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onButtonRecyclerViewSaveTrack(track: Track) {

        openAudioPlayer(track)

        if (clickDebounce()) {
            historyTrackManager.saveHistory(track)
            trackHistoryList.clear()
            trackHistoryList.addAll(historyTrackManager.getHistory())
            trackAdapter.notifyDataSetChanged()
        }
    }
}

