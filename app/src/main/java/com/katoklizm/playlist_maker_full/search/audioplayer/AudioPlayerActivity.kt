package com.katoklizm.playlist_maker_full.search.audioplayer

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.katoklizm.playlist_maker_full.R
import com.katoklizm.playlist_maker_full.databinding.AudioPlayerBinding
import com.katoklizm.playlist_maker_full.search.track.ConstTrack
import com.katoklizm.playlist_maker_full.search.track.Track
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {
    lateinit var binding: AudioPlayerBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.audioPlayerBack.setOnClickListener {
            finish()
        }
        val audioPlayerImageTrack = binding.audioPlayerImageTrack

        val audioPlayerNameSong = binding.audioPlayerNameSong
        val audioPlayerNameMusician = binding.audioPlayerNameMusician

        val audioPlayerTime = binding.audioPlayerTime

        val audioPlayerTextViewTimeRead = binding.audioPlayerTextViewTimeRead
        val audioPlayerTextViewTrackNameRead = binding.audioPlayerTextViewTrackNameRead
        val audioPlayerTextViewYearRead = binding.audioPlayerTextViewYearRead
        val audioPlayerTextViewGenreRead = binding.audioPlayerTextViewGenreRead
        val audioPlayerTextViewCountryRead = binding.audioPlayerTextViewCountryRead

        val jsonTrack = intent.getStringExtra("selectedTrack")
        val gson = Gson()
        val selectedTrack = gson.fromJson(jsonTrack, Track::class.java)

        audioPlayerNameSong.text = selectedTrack.trackName
        audioPlayerNameMusician.text = selectedTrack.artistName

        audioPlayerTextViewTrackNameRead.text = selectedTrack.trackName
        audioPlayerTextViewYearRead.text = selectedTrack.releaseDate
        audioPlayerTextViewGenreRead.text = selectedTrack.primaryGenreName
        audioPlayerTextViewCountryRead.text = selectedTrack.country

        audioPlayerTime.text = SimpleDateFormat("mm:ss", Locale.getDefault())
            .format(selectedTrack.trackTimeMillis.toLong())

        audioPlayerTextViewTimeRead.text = audioPlayerTime.text

        val originalImageUrl = selectedTrack.artworkUrl100
        val imageUrl = originalImageUrl.replace("100x100bb", "512x512bb")

        Glide.with(this)
            .load(imageUrl)
            .transform(RoundedCorners(ConstTrack.ROUNDED_CORNERS_RADIUS))
            .placeholder(R.drawable.vector_plug)
            .into(audioPlayerImageTrack)
    }
}