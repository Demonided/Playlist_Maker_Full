package com.katoklizm.playlist_maker_full.search.audioplayer

import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.katoklizm.playlist_maker_full.R
import com.katoklizm.playlist_maker_full.databinding.AudioPlayerBinding
import com.katoklizm.playlist_maker_full.search.track.Track

class AudioPlayerActivity : AppCompatActivity() {
    lateinit var binding: AudioPlayerBinding
    lateinit var audioPlayerLoadTrack: AudioPlayerLoadTrack

    var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.audioPlayerBack.setOnClickListener {
            finish()
        }

        audioPlayerLoadTrack = AudioPlayerLoadTrack(this)

        val jsonTrack = intent.getStringExtra("selectedTrack")
        val gson = Gson()
        val selectedTrack = gson.fromJson(jsonTrack, Track::class.java)

        audioPlayerLoadTrack.bind(selectedTrack)
    }
}