package com.katoklizm.playlist_maker_full.ui.medialibrary.playlist

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.katoklizm.playlist_maker_full.R
import com.katoklizm.playlist_maker_full.data.ConstTrack
import com.katoklizm.playlist_maker_full.data.converters.AlbumDbConverters.getTrackQuantityString
import com.katoklizm.playlist_maker_full.domain.album.model.AlbumPlaylist

class PlaylistViewHolder(
    parent: ViewGroup,
    private val context: Context) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.album_playlist, parent, false)
    ) {

    private val albumName: TextView = itemView.findViewById(R.id.album_title)
    private val albumQuantity: TextView = itemView.findViewById(R.id.album_quantity)
    private val image: ImageView = itemView.findViewById(R.id.album_image)

    fun bind(model: AlbumPlaylist) {
        albumName.text = model.name
        albumQuantity.text = model.getTrackQuantityString(context)

        val imageUrl = model.image

        Glide.with(itemView)
            .load(imageUrl)
            .transform(RoundedCorners(ConstTrack.ROUNDED_CORNERS_RADIUS))
            .into(image)
    }
}