package com.katoklizm.playlist_maker_full.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "album_track_playlist")
data class TrackAlbumPlaylistEntity(
    @PrimaryKey @ColumnInfo(name = "trackId")
    val trackId: Int, // Id трека
    val trackName: String?, // Название композиции
    val artistName: String?, // Имя исполнителя
    val trackTimeMillis: String?, // Продолжительность трека
    val artworkUrl100: String?, // Ссылка на изображение обложки
    val collectionName: String?, // Название альбома
    @ColumnInfo(name = "releaseDate")
    val releaseDate: String?, // Год релиза трека
    val primaryGenreName: String?, // Жанр трека
    val country: String?, // Страна
    val previewUrl: String?, // 30сек проигрывание трека
    val insertionTime: Long // Время вставки
)
