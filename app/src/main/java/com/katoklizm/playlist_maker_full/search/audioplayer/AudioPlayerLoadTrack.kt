package com.katoklizm.playlist_maker_full.search.audioplayer

import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.katoklizm.playlist_maker_full.R
import com.katoklizm.playlist_maker_full.search.track.ConstTrack
import com.katoklizm.playlist_maker_full.search.track.Track
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerLoadTrack(private val activity: AudioPlayerActivity) {

    val audioPlayerImageTrack: ImageView = activity.findViewById(R.id.audio_player_image_track)
    val audioPlayerNameSong: TextView = activity.findViewById(R.id.audio_player_name_song)
    val audioPlayerNameMusician: TextView = activity.findViewById(R.id.audio_player_name_musician)
    val audioPlayerTime: TextView = activity.findViewById(R.id.audio_player_time)
    val audioPlayerTextViewTimeRead: TextView = activity.findViewById(R.id.audio_player_textView_time_read)
    val audioPlayerTextViewTrackNameRead: TextView = activity.findViewById(R.id.audio_player_textView_track_name_read)
    val audioPlayerTextViewYearRead: TextView = activity.findViewById(R.id.audio_player_textView_year_read)
    val audioPlayerTextViewGenreRead: TextView = activity.findViewById(R.id.audio_player_textView_genre_read)
    val audioPlayerTextViewCountryRead: TextView = activity.findViewById(R.id.audio_player_textView_country_read)

    fun bind (track: Track) {

        audioPlayerNameSong.text = track.trackName

        audioPlayerNameMusician.text = track.artistName

        audioPlayerTextViewTrackNameRead.text = track.trackName
        audioPlayerTextViewYearRead.text = track.releaseDate
        audioPlayerTextViewGenreRead.text = track.primaryGenreName
        audioPlayerTextViewCountryRead.text = track.country

        audioPlayerTime.text = SimpleDateFormat("mm:ss", Locale.getDefault())
            .format(track.trackTimeMillis.toLong())

        audioPlayerTextViewTimeRead.text = audioPlayerTime.text

        val originalImageUrl = track.artworkUrl100
        fun getCoverArtwork() = originalImageUrl.replaceAfterLast('/', "512x512bb.jpg")

        Glide.with(activity)
            .load(getCoverArtwork())
            .transform(RoundedCorners(ConstTrack.ROUNDED_CORNERS_RADIUS))
            .placeholder(R.drawable.vector_plug)
            .into(audioPlayerImageTrack)
    }

}