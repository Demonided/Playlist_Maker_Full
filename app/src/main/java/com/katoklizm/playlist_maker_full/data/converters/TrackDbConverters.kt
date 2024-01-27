package com.katoklizm.playlist_maker_full.data.converters

import com.katoklizm.playlist_maker_full.data.db.entity.TrackEntity
import com.katoklizm.playlist_maker_full.domain.search.model.Track
import java.util.Calendar

object TrackDbConverters {

    fun Track.mapToEntity(): TrackEntity {
        return TrackEntity(
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

    fun TrackEntity.mapToTrack(): Track {
        return Track(
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
            isFavorite = true,
        )
    }

    fun List<TrackEntity>.mapToTracks(): List<Track> = map{ it.mapToTrack() }
}