package com.katoklizm.playlist_maker_full.data.search.network

import com.katoklizm.playlist_maker_full.data.NetworkClient
import com.katoklizm.playlist_maker_full.data.search.dto.TrackSearchRequest
import com.katoklizm.playlist_maker_full.data.search.dto.TrackSearchResponse
import com.katoklizm.playlist_maker_full.domain.search.api.TrackRepository
import com.katoklizm.playlist_maker_full.domain.search.model.Track
import com.katoklizm.playlist_maker_full.util.Resource

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {
    override fun searchTrack(term: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TrackSearchRequest(term = term))

        return when(response.resultCode) {
            -1 -> {
                Resource.Error("Проверьте подключение к интернету")
            }
            200 -> {
                Resource.Success((response as TrackSearchResponse).results.map {
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
                })
            }
            else -> {
                Resource.Error("Ошибка сервера")
            }
        }
    }
}