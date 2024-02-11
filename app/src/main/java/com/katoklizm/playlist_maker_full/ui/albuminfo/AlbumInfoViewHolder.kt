package com.katoklizm.playlist_maker_full.ui.albuminfo

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.katoklizm.playlist_maker_full.R
import com.katoklizm.playlist_maker_full.data.ConstTrack
import com.katoklizm.playlist_maker_full.domain.search.model.Track

class AlbumInfoViewHolder(parent: ViewGroup):
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.search_track, parent, false)
    ){

    private val trackName: TextView = itemView.findViewById(R.id.track_name_song)
    private val artistName: TextView = itemView.findViewById(R.id.track_name_musician)
    private val trackTime: TextView = itemView.findViewById(R.id.track_time)
    private val artworkUrl160: ImageView = itemView.findViewById(R.id.track_image)

    fun bind(model: Track) {
        trackName.text = model.trackName
        artistName.text = model.artistName
        trackTime.text = model.trackTime
        val imageUrl = model.artworkUrl160

        Glide.with(itemView)
            .load(imageUrl)
            .transform(RoundedCorners(ConstTrack.ROUNDED_CORNERS_RADIUS))
            .into(artworkUrl160)
    }
}