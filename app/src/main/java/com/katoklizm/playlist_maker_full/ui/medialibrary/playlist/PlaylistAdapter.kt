package com.katoklizm.playlist_maker_full.ui.medialibrary.playlist

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.katoklizm.playlist_maker_full.domain.album.model.AlbumPlaylist

class PlaylistAdapter :
    RecyclerView.Adapter<PlaylistViewHolder>() {

    val albumPlaylist = ArrayList<AlbumPlaylist>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder =
        PlaylistViewHolder(parent)

    override fun getItemCount(): Int = albumPlaylist.size

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(albumPlaylist.get(position))
    }
}