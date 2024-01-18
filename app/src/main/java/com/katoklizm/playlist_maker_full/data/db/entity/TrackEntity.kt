package com.katoklizm.playlist_maker_full.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.time.Instant
import java.util.Date

@Entity(tableName = "track_table")
data class TrackEntity(
    @PrimaryKey @ColumnInfo(name = "id")
    val id: Int, // Id трека
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
    val isFavorite: Boolean // Добавлен ли трек в избранное
)
