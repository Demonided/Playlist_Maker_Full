package com.katoklizm.playlist_maker_full.ui.track

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.katoklizm.playlist_maker_full.R
import com.katoklizm.playlist_maker_full.databinding.SearchTrackBinding
import com.katoklizm.playlist_maker_full.domain.model.Track
import com.katoklizm.playlist_maker_full.data.ConstTrack

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val binding = SearchTrackBinding.bind(itemView)

    private val trackName: TextView = itemView.findViewById(R.id.track_name_song)
    private val artistName: TextView = itemView.findViewById(R.id.track_name_musician)
    private val trackTime: TextView = itemView.findViewById(R.id.track_time)
    private val artworkUrl100: ImageView = itemView.findViewById(R.id.track_image)

    fun bind(model: Track) {
        trackName.text = model.trackName
        artistName.text = model.artistName
        trackTime.text = model.trackTime
        val imageUrl = model.artworkUrl100

        Glide.with(itemView)
            .load(imageUrl)
            .transform(RoundedCorners(ConstTrack.ROUNDED_CORNERS_RADIUS))
            .into(artworkUrl100)
    }

    fun setOnSaveTrack(saveTrack: OnSaveTrackManager) {
        binding.searchTrackFullButton.setOnClickListener {
            saveTrack.action()
        }
    }
}

fun interface OnSaveTrackManager {
    fun action()
}