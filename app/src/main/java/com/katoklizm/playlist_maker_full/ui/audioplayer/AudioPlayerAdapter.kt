package com.katoklizm.playlist_maker_full.ui.audioplayer

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.katoklizm.playlist_maker_full.domain.album.model.AlbumPlaylist
import com.katoklizm.playlist_maker_full.ui.medialibrary.playlist.PlaylistViewHolder

class AudioPlayerAdapter(
    private val context: Context
) : RecyclerView.Adapter<AudioPlayerViewHolder>() {

    val albumListPlaylist = ArrayList<AlbumPlaylist>()

    var itemClickListener: ((Int, AlbumPlaylist) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioPlayerViewHolder =
        AudioPlayerViewHolder(parent, context)

    override fun getItemCount(): Int = albumListPlaylist.size

    override fun onBindViewHolder(holder: AudioPlayerViewHolder, position: Int) {
        val album = albumListPlaylist[position]
        holder.bind(album)

        holder.itemView.setOnClickListener {
            itemClickListener?.invoke(position, album)
        }
    }
}