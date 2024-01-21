package com.katoklizm.playlist_maker_full.domain.search.model

import android.os.Parcel
import android.os.Parcelable
import java.text.SimpleDateFormat
import java.util.Locale

data class Track(
    val id: String,
    val trackName: String?, // Название композиции
    val artistName: String?, // Имя исполнителя
    val trackTimeMillis: String?, // Продолжительность трека
    val artworkUrl100: String?, // Ссылка на изображение обложки
    val collectionName: String?, // Название альбома
    val releaseDate: String?, // Год релиза трека
    val primaryGenreName: String?, // Жанр трека
    val country: String?, // Страна
    val previewUrl: String?, // 30сек проигрывание трека
    val isFavorite: Boolean = false
): Parcelable {

    val artworkUrl512
        get() = artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")

    val trackTime
        get() = SimpleDateFormat("mm:ss", Locale.getDefault())
            .format(trackTimeMillis?.toLong())

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "missing", // myTag -> нужно разобраться
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(id)
        dest.writeString(trackName)
        dest.writeString(artistName)
        dest.writeString(trackTimeMillis)
        dest.writeString(artworkUrl100)
        dest.writeString(collectionName)
        dest.writeString(releaseDate)
        dest.writeString(primaryGenreName)
        dest.writeString(country)
        dest.writeString(previewUrl)
    }

    companion object CREATOR : Parcelable.Creator<Track> {
        override fun createFromParcel(parcel: Parcel): Track {
            return Track(parcel)
        }

        override fun newArray(size: Int): Array<Track?> {
            return arrayOfNulls(size)
        }
    }
}