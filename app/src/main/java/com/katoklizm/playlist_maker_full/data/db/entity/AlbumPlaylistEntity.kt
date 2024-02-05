package com.katoklizm.playlist_maker_full.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "album_table")
data class AlbumPlaylistEntity(
    @PrimaryKey (autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = "album_name")
    val name: String,
    @ColumnInfo(name = "album_description")
    val description: String,
    @ColumnInfo(name = "album_image")
    val image: String,
    @ColumnInfo(name = "album_quantity_track")
    val quantity: Int,
    @ColumnInfo(name = "album_track")
    val track: String
)