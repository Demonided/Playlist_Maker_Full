package com.katoklizm.playlist_maker_full.search.track

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.katoklizm.playlist_maker_full.R
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val trackName: TextView = itemView.findViewById(R.id.track_name_song)
    private val artistName: TextView = itemView.findViewById(R.id.track_name_musician)
    private val trackTime: TextView = itemView.findViewById(R.id.track_time)
    private val artworkUrl100: ImageView = itemView.findViewById(R.id.track_image)

    fun bind(model: Track) {
        trackName.text = model.trackName
        artistName.text = model.artistName
        trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault())
            .format(model.trackTimeMillis.toLong())
        val imageUrl = model.artworkUrl100

        Glide.with(itemView)
            .load(imageUrl)
            .transform(RoundedCorners(ConstTrack.ROUNDED_CORNERS_RADIUS))
//            .placeholder(R.drawable.media_library_playlist)
            .into(artworkUrl100)
    }
}