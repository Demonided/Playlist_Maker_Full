package com.katoklizm.playlist_maker_full.domain.album.model

data class TrackAlbumPlaylist(
    val trackId: Int, // Id трека
    val trackName: String?, // Название композиции
    val artistName: String?, // Имя исполнителя
    val trackTimeMillis: String?, // Продолжительность трека
    val artworkUrl100: String?, // Ссылка на изображение обложки
    val collectionName: String?, // Название альбома
    val releaseDate: String?, // Год релиза трека
    val primaryGenreName: String?, // Жанр трека
    val country: String?, // Страна
    val previewUrl: String?, // 30сек проигрывание трека
)