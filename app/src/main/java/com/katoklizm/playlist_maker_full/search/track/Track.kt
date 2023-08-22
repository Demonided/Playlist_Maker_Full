package com.katoklizm.playlist_maker_full.search.track

import java.text.SimpleDateFormat
import java.util.Locale

data class Track(
    val id: Int,
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTimeMillis: String, // Продолжительность трека
    val artworkUrl100: String, // Ссылка на изображение обложки
    val collectionName: String, // Название альбома
    val releaseDate: String, // Год релиза трека
    val primaryGenreName: String, // Жанр трека
    val country: String // Страна
) {

    val artworkUrl512
        get() = artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")

    val trackTime
        get() = SimpleDateFormat("mm:ss", Locale.getDefault())
            .format(trackTimeMillis.toLong())


}