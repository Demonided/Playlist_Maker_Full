package com.katoklizm.playlist_maker_full.data.converters

import com.katoklizm.playlist_maker_full.data.db.entity.TrackAlbumPlaylistEntity
import com.katoklizm.playlist_maker_full.domain.album.model.TrackAlbumPlaylist
import java.util.Calendar

object TrackAlbumDbConverters {

    fun TrackAlbumPlaylist.mapAlbumTrackPlaylistEntity(): TrackAlbumPlaylistEntity {
        return TrackAlbumPlaylistEntity(
            trackId = trackId,
            trackName = trackName,
            artistName = artistName,
            trackTimeMillis = trackTimeMillis,
            artworkUrl100 = artworkUrl100,
            collectionName = collectionName,
            releaseDate = releaseDate,
            primaryGenreName = primaryGenreName,
            country = country,
            previewUrl = previewUrl,
            insertionTime = Calendar.getInstance().time.time
        )
    }

    fun TrackAlbumPlaylistEntity.mapAlbumTrackPlaylist(): TrackAlbumPlaylist {
        return TrackAlbumPlaylist(
            trackId = trackId,
            trackName = trackName,
            artistName = artistName,
            trackTimeMillis = trackTimeMillis,
            artworkUrl100 = artworkUrl100,
            collectionName = collectionName,
            releaseDate = releaseDate,
            primaryGenreName = primaryGenreName,
            country = country,
            previewUrl = previewUrl,

        )
    }

    fun List<TrackAlbumPlaylistEntity>.mapAlbumTrackPlaylist(): List<TrackAlbumPlaylist> = map { it.mapAlbumTrackPlaylist() }
}