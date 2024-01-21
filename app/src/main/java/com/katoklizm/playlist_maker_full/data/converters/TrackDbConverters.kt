package com.katoklizm.playlist_maker_full.data.converters

import com.katoklizm.playlist_maker_full.data.db.entity.TrackEntity
import com.katoklizm.playlist_maker_full.domain.search.model.Track

object TrackDbConverters {

    fun Track.mapToEntity(): TrackEntity {
        return TrackEntity(
            id = id,
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

    fun TrackEntity.mapToTrack(): Track {
        return Track(
            id = id,
            trackName = trackName,
            artistName = artistName,
            trackTimeMillis = trackTimeMillis,
            artworkUrl100 = artworkUrl100,
            collectionName = collectionName,
            releaseDate = releaseDate,
            primaryGenreName = primaryGenreName,
            country = country,
            previewUrl = previewUrl,
            isFavorite = true,
        )
    }

    fun List<TrackEntity>.mapToTracks(): List<Track> = map{ it.mapToTrack() }
}