package com.katoklizm.playlist_maker_full.ui.track

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.katoklizm.playlist_maker_full.R
import com.katoklizm.playlist_maker_full.creator.Creator
import com.katoklizm.playlist_maker_full.databinding.ActivitySearchBinding
import com.katoklizm.playlist_maker_full.ui.audioplayer.AudioPlayerActivity
import com.katoklizm.playlist_maker_full.search.track.ConstTrack.SAVE_TRACK
import com.katoklizm.playlist_maker_full.search.track.ConstTrack.USER_TEXT
import com.katoklizm.playlist_maker_full.search.track.HistoryTrackManager
import com.katoklizm.playlist_maker_full.domain.model.Track

class SearchActivity : AppCompatActivity(), TrackAdapter.OnSaveTrackManagersClickListener {

    var binding: ActivitySearchBinding? = null

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
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



//    private fun updatePageSearch() {
//        if (binding?.searchEditText?.text!!.isNotEmpty()) {
//            binding?.searchProgressBar?.visibility = View.VISIBLE
//            iTunesService.search(binding?.searchEditText?.text.toString())
//                .enqueue(object : Callback<TrackSearchResponse> {
//                    @SuppressLint("NotifyDataSetChanged")
//                    override fun onResponse(
//                        call: Call<TrackSearchResponse>,
//                        response: Response<TrackSearchResponse>
//                    ) {
//                        if (response.code() == 200) {
//                            trackList.clear()
//                            if (response.body()?.results?.isNotEmpty() == true) {
//                                binding?.searchProgressBar?.visibility = View.GONE
//                                trackList.addAll(response.body()?.results!!)
//                                trackAdapter.notifyDataSetChanged()
//                                binding?.searchNothingFound?.visibility = View.GONE
//                                binding?.searchErrorImage?.visibility = View.GONE
//                            } else showMessage(
//                                binding!!.searchNothingFound,
//                                binding!!.searchErrorImage,
//                                ""
//                            )
//
//                        } else showMessage(
//                            binding!!.searchErrorImage,
//                            binding!!.searchNothingFound,
//                            response.code().toString()
//                        )
//                    }
//
//                    override fun onFailure(call: Call<TrackSearchResponse>, t: Throwable) {
//                        showMessage(
//                            binding!!.searchErrorImage,
//                            binding!!.searchNothingFound,
//                            t.message.toString()
//                        )
//                    }
//                })
//        }
//    }



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

