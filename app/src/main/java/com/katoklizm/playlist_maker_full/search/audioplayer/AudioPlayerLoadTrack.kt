package com.katoklizm.playlist_maker_full.search.audioplayer

import android.text.format.DateUtils
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

    private val audioPlayerImageTrack: ImageView = activity.findViewById(R.id.audio_player_image_track)
    private val audioPlayerNameSong: TextView = activity.findViewById(R.id.audio_player_name_song)
    private val audioPlayerNameMusician: TextView = activity.findViewById(R.id.audio_player_name_musician)
    private val audioPlayerTime: TextView = activity.findViewById(R.id.audio_player_time)
    private val audioPlayerTextViewTimeRead: TextView = activity.findViewById(R.id.audio_player_textView_time_read)
    private val audioPlayerTextViewTrackNameRead: TextView = activity.findViewById(R.id.audio_player_textView_track_name_read)
    private val audioPlayerTextViewYearRead: TextView = activity.findViewById(R.id.audio_player_textView_year_read)
    private val audioPlayerTextViewGenreRead: TextView = activity.findViewById(R.id.audio_player_textView_genre_read)
    private val audioPlayerTextViewCountryRead: TextView = activity.findViewById(R.id.audio_player_textView_country_read)

    fun bind (track: Track?) {

        audioPlayerNameSong.text = track?.trackName

        audioPlayerNameMusician.text = track?.artistName

        audioPlayerTextViewTrackNameRead.text = track?.trackName
        audioPlayerTextViewYearRead.text = track?.releaseDate?.let { ConstTrack.formatDate(it) }
        audioPlayerTextViewGenreRead.text = track?.primaryGenreName
        audioPlayerTextViewCountryRead.text = track?.country

        audioPlayerTime.text = "00:00"

        audioPlayerTextViewTimeRead.text = track?.trackTime

        Glide.with(activity)
            .load(track?.artworkUrl512)
            .transform(RoundedCorners(ConstTrack.ROUNDED_CORNERS_RADIUS))
            .placeholder(R.drawable.vector_plug)
            .into(audioPlayerImageTrack)
    }
}