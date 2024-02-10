package com.katoklizm.playlist_maker_full.ui.albuminfo

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.katoklizm.playlist_maker_full.domain.album.model.AlbumPlaylist
import com.katoklizm.playlist_maker_full.domain.search.model.Track

class AlbumInfoAdapter(
    tracks: List<Track>
) : RecyclerView.Adapter<AlbumInfoViewHolder>() {

    val tracksAlbum = ArrayList<Track>()
    var itemClickListener: ((Int, Track) -> Unit)? = null
    var itemLongClickListener: ((Int, Track) -> Unit)? = null

    init {
        tracksAlbum.addAll(tracks)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumInfoViewHolder =
        AlbumInfoViewHolder(parent)

    override fun getItemCount(): Int = tracksAlbum.size

    override fun onBindViewHolder(holder: AlbumInfoViewHolder, position: Int) {
        val track = tracksAlbum[position]
        holder.bind(track)

        holder.itemView.setOnClickListener {
            itemClickListener?.invoke(position, track)
        }

        holder.itemView.setOnLongClickListener(View.OnLongClickListener {
            itemLongClickListener?.invoke(position, track)
            return@OnLongClickListener true
        })
    }
}