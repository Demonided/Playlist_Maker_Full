package com.katoklizm.playlist_maker_full.data.dto

import android.os.Parcel
import android.os.Parcelable
import com.katoklizm.playlist_maker_full.domain.model.Track
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class TrackDto(
    val id: Int,
    val trackName: String?, // Название композиции
    val artistName: String?, // Имя исполнителя
    val trackTimeMillis: String?, // Продолжительность трека
    val artworkUrl100: String?, // Ссылка на изображение обложки
    val collectionName: String?, // Название альбома
    val releaseDate: Date?, // Год релиза трека
    val primaryGenreName: String?, // Жанр трека
    val country: String?, // Страна
    val previewUrl: String? // 30сек проигрывание трека
)