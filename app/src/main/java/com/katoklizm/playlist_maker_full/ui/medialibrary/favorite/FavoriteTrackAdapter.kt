package com.katoklizm.playlist_maker_full.ui.medialibrary.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.katoklizm.playlist_maker_full.R
import com.katoklizm.playlist_maker_full.domain.search.model.Track

class FavoriteTrackAdapter() :
    RecyclerView.Adapter<FavoriteTrackViewHolder>() {

    var tracks = ArrayList<Track>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteTrackViewHolder = FavoriteTrackViewHolder(parent)

    override fun getItemCount(): Int = tracks.size

    override fun onBindViewHolder(holder: FavoriteTrackViewHolder, position: Int) {
        holder.bind(tracks.get(position))
    }
}

