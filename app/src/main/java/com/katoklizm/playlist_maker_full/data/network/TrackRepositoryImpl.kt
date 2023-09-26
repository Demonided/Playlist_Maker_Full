package com.katoklizm.playlist_maker_full.data.network

import com.katoklizm.playlist_maker_full.data.NetworkClient
import com.katoklizm.playlist_maker_full.data.dto.TrackSearchRequest
import com.katoklizm.playlist_maker_full.data.dto.TrackSearchResponse
import com.katoklizm.playlist_maker_full.domain.api.TrackRepository
import com.katoklizm.playlist_maker_full.domain.model.Track

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {
    override fun searchTrack(term: String): List<Track> {
        val response = networkClient.doRequest(TrackSearchRequest(term = term))
        if (response.resultCode == 200) {
            return (response as TrackSearchResponse).results.map {
                Track(
                    it.id,
                    it.trackName,
                    it.artistName,
                    it.trackTimeMillis,
                    it.artworkUrl100,
                    it.collectionName,
                    it.releaseDate,
                    it.primaryGenreName,
                    it.country,
                    it.previewUrl,
                )
            }

        } else {
            return emptyList()
        }
    }
}