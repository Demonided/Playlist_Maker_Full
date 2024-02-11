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

//    fun AlbumPlaylist.getTrackQuantityString(context: Context): String {
//        // Попытка через ресурс передать количество правильно отображает только 1 трек, а дальше пошло
//        // 2 треков, 3 треков. Причину найти не смог
//        return context.resources.getQuantityString(R.plurals.track_quantity, quantity, quantity)
//    }

    fun AlbumPlaylist.getTrackQuantityString(): String =
        when(quantity % 10) {
            1 -> "$quantity трек"
            in 2..4 -> "$quantity трека"
            else -> "$quantity треков"
        }


    fun List<AlbumPlaylistEntity>.mapToAlbumPlaylist(): List<AlbumPlaylist> =
        map { it.mapToAlbumPlaylist() }

}