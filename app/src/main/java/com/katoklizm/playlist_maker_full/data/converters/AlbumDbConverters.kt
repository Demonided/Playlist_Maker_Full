package com.katoklizm.playlist_maker_full.data.converters

import android.content.Context
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
        return context.resources.getQuantityString(R.plurals.track_quantity, quantity, quantity)
    }

    fun List<AlbumPlaylistEntity>.mapToAlbumPlaylist(): List<AlbumPlaylist> =
        map { it.mapToAlbumPlaylist() }

}