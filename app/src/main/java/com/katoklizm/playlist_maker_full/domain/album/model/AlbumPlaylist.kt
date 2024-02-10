package com.katoklizm.playlist_maker_full.domain.album.model

import android.os.Parcel
import android.os.Parcelable

data class AlbumPlaylist(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val image: String = "",
    val quantity: Int = 0,
    val track: String = ""
): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: ""
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(image)
        parcel.writeInt(quantity)
        parcel.writeString(track)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AlbumPlaylist> {
        override fun createFromParcel(parcel: Parcel): AlbumPlaylist {
            return AlbumPlaylist(parcel)
        }

        override fun newArray(size: Int): Array<AlbumPlaylist?> {
            return arrayOfNulls(size)
        }
    }
}
