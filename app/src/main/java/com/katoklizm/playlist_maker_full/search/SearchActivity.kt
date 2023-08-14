package com.katoklizm.playlist_maker_full.search

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.katoklizm.playlist_maker_full.R
import com.katoklizm.playlist_maker_full.databinding.ActivitySearchBinding
import com.katoklizm.playlist_maker_full.search.track.ConstTrack.USER_TEXT
import com.katoklizm.playlist_maker_full.search.track.HistoryTrackManager
import com.katoklizm.playlist_maker_full.search.track.Track
import com.katoklizm.playlist_maker_full.search.track.TrackAdapter
import com.katoklizm.playlist_maker_full.search.track.TrackResponse
import com.katoklizm.playlist_maker_full.search.track.iTunesSearchApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity(), TrackAdapter.OnSaveTrackManagersClickListener {
    private lateinit var binding: ActivitySearchBinding
    private var enteredText: String? = ""

    private val imdbBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(imdbBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(iTunesSearchApi::class.java)

    private lateinit var recyclerView: RecyclerView

    private val trackList = ArrayList<Track>()
    private val trackAdapter = TrackAdapter(this)

    private val trackHistoryList = ArrayList<Track>()

    lateinit var historyTrackManager: HistoryTrackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        historyTrackManager = HistoryTrackManager(this)

        binding.settingBack.setOnClickListener {
            finish()
        }

        binding.searchClearButton.setOnClickListener {
            binding.searchEditText.text.clear()
            hideSoftKeyboard()
            trackList.clear()
            trackAdapter.notifyDataSetChanged()
        }

        binding.searchUpdatePage.setOnClickListener {
            updatePageSearch()
        }

        binding.searchClearHistory.setOnClickListener {
            historyTrackManager.prefs.edit().clear().apply()
            binding.searchLinerLayoutHistoryTrack.visibility = View.GONE
            trackAdapter.tracks.clear()
            trackHistoryList.clear()
            trackAdapter.notifyDataSetChanged()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.searchClearButton.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }

        if (savedInstanceState != null) {
            enteredText = savedInstanceState.getString(USER_TEXT)
            binding.searchEditText.setText(enteredText)
        }

        binding.searchEditText.addTextChangedListener(simpleTextWatcher)

        searchMusicTrack()

        recyclerView = findViewById(R.id.search_recycler_music_track)

        trackHistoryList.addAll(historyTrackManager.getHistory())
        if (trackHistoryList.size == 0) {
            binding.searchLinerLayoutHistoryTrack.visibility = View.GONE
        }

        trackAdapter.notifyDataSetChanged()

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = trackAdapter
        trackAdapter.tracks = trackHistoryList

        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.searchLinerLayoutHistoryTrack.visibility =
                    if (binding.searchEditText.hasFocus() && s?.isEmpty() == true && trackHistoryList.size > 0) View.VISIBLE else View.GONE

                if (binding.searchEditText.text.isEmpty()) {
                    binding.searchNothingFound.visibility = View.GONE
                    binding.searchErrorImage.visibility = View.GONE
                    trackAdapter.updateTrackList(historyTrackManager.getHistory())
                    trackAdapter.notifyDataSetChanged()
                } else {
                    trackAdapter.updateTrackList(trackList)
                    trackAdapter.notifyDataSetChanged()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        })
    }

    private fun updatePageSearch() {
        iTunesService.search(binding.searchEditText.text.toString())
            .enqueue(object : Callback<TrackResponse> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<TrackResponse>,
                    response: Response<TrackResponse>
                ) {
                    if (response.code() == 200) {
                        trackList.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            trackList.addAll(response.body()?.results!!)
                            trackAdapter.notifyDataSetChanged()
                            binding.searchNothingFound.visibility = View.GONE
                            binding.searchErrorImage.visibility = View.GONE
                        } else showMessage(binding.searchNothingFound, binding.searchErrorImage, "")

                    } else showMessage(
                        binding.searchErrorImage,
                        binding.searchNothingFound,
                        response.code().toString()
                    )
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    showMessage(
                        binding.searchErrorImage,
                        binding.searchNothingFound,
                        t.message.toString()
                    )
                }
            })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showMessage(oneLinear: ViewGroup, twoLinear: ViewGroup, toastMakeText: String) {
        oneLinear.visibility = View.VISIBLE
        twoLinear.visibility = View.GONE
        trackList.clear()
        trackAdapter.notifyDataSetChanged()
        if (toastMakeText.isNotEmpty()) {
            Toast.makeText(applicationContext, toastMakeText, Toast.LENGTH_LONG).show()
        }
    }

    private fun searchMusicTrack() {
        binding.searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (binding.searchEditText.text.isNotEmpty()) {
                    updatePageSearch()
                }
            }
            false
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        enteredText = binding.searchEditText.text.toString()
        outState.putString(USER_TEXT, enteredText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        enteredText = savedInstanceState.getString(USER_TEXT)
        binding.searchEditText.setText(enteredText)
    }

    private fun hideSoftKeyboard() {
        val hide = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        hide.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0)
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.INVISIBLE
        } else {
            View.VISIBLE
        }
    }

    override fun onButtonRecyclerViewSaveTrack(track: Track) {
        Log.d("ButtonRecyclerView", "Мы смогли реализовать нажатие ${track.trackName}")

        historyTrackManager.saveHistory(track)
        trackHistoryList.clear()
        trackHistoryList.addAll(historyTrackManager.getHistory())
        trackAdapter.notifyDataSetChanged()

        Toast.makeText(
            applicationContext,
            "Мы добавили трек ${track.trackName} and ${historyTrackManager.getHistory().size}",
            Toast.LENGTH_LONG
        )
            .show()
    }
}

