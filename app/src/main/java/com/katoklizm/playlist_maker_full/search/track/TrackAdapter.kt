package com.katoklizm.playlist_maker_full.search.track

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.katoklizm.playlist_maker_full.R

class TrackAdapter(val onSaveTrackManagersClickListener: OnSaveTrackManagersClickListener) :
    RecyclerView.Adapter<TrackViewHolder>() {

    var tracks = ArrayList<Track>()

    fun updateTrackList(newTrack: ArrayList<Track>) {
        tracks = newTrack

        val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int {
                return tracks.size
            }

            override fun getNewListSize(): Int {
                return newTrack.size
            }

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return tracks[oldItemPosition].id == newTrack[newItemPosition].id
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return tracks[oldItemPosition] == newTrack[newItemPosition]
            }
        })

        result.dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_track, parent, false)
        return TrackViewHolder(view)
    }

    override fun getItemCount(): Int = tracks.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track)
        holder.setOnSaveTrack { onSaveTrackManagersClickListener.onButtonRecyclerViewSaveTrack(track) }
    }

    interface OnSaveTrackManagersClickListener {
        fun onButtonRecyclerViewSaveTrack(track: Track)
    }
}

