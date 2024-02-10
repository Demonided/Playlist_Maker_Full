package com.katoklizm.playlist_maker_full.data.converters

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

    fun AlbumPlaylist.getTrackQuantityString(): String {
        return when (quantity % 10) {
            1 -> "$quantity трек"
            in 2..4 -> "$quantity трека"
            else -> "$quantity треков"
        }
    }

    fun List<AlbumPlaylistEntity>.mapToAlbumPlaylist(): List<AlbumPlaylist> =
        map { it.mapToAlbumPlaylist() }

}