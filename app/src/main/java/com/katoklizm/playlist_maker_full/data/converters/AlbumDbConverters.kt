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

    fun List<AlbumPlaylistEntity>.mapToAlbumPlaylist(): List<AlbumPlaylist> =
        map { it.mapToAlbumPlaylist() }

}