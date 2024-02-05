package com.katoklizm.playlist_maker_full.domain.album.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class AlbumPlaylist(
    val id: Long = 0,
    val name: String = "",
    val description: String = "",
    val image: String = "",
    val quantity: Int = 0,
    val track: String = ""
)
