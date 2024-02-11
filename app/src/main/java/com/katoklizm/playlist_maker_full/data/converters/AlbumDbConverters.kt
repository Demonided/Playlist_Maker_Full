package com.katoklizm.playlist_maker_full.data.converters

import android.content.Context
import android.util.Log
import com.katoklizm.playlist_maker_full.R
import com.katoklizm.playlist_maker_full.data.db.entity.AlbumPlaylistEntity
import com.katoklizm.playlist_maker_full.domain.album.model.AlbumPlaylist

object AlbumDbConverters {

    fun AlbumPlaylistEntity.mapToAlbumPlaylist(): AlbumPlaylist {
        return AlbumPlaylist(
            id = id,
            name = name,
            description = description,
            image = image,
            quantity = quantity,
            track = track
        )
    }

    fun AlbumPlaylist.mapToAlbumPlaylistEntity(): AlbumPlaylistEntity {
        return AlbumPlaylistEntity(
            id = id,
            name = name,
            description = description,
            image = image,
            quantity = quantity,
            track = track
        )
    }

    fun AlbumPlaylist.getTrackQuantityString(context: Context): String {
        for (i in 0..15) {
            val a = context.resources.getQuantityString(R.plurals.track_quantity, i, i)
            Log.d("Resourse", "текущий показатель = $a")
        }
        return context.resources.getQuantityString(R.plurals.track_quantity, quantity, quantity)
    }

    fun Long?.toMinutes(): Long {
        return this ?: 0 / 60000
    }

    fun List<AlbumPlaylistEntity>.mapToAlbumPlaylist(): List<AlbumPlaylist> =
        map { it.mapToAlbumPlaylist() }

}