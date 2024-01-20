package com.katoklizm.playlist_maker_full.data.search.network

import com.katoklizm.playlist_maker_full.data.NetworkClient
import com.katoklizm.playlist_maker_full.data.search.dto.TrackSearchRequest
import com.katoklizm.playlist_maker_full.data.search.dto.TrackSearchResponse
import com.katoklizm.playlist_maker_full.data.search.track.HistoryTrackManager
import com.katoklizm.playlist_maker_full.domain.search.api.TrackRepository
import com.katoklizm.playlist_maker_full.domain.search.model.Track
import com.katoklizm.playlist_maker_full.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackRepositoryImpl(
    private val networkClient: NetworkClient,
    private val localStorage: HistoryTrackManager
) : TrackRepository {
    override fun searchTrack(term: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TrackSearchRequest(term = term))

        when(response.resultCode) {
            -1 -> {
                emit(Resource.Error("Проверьте подключение к интернету"))
            }
            200 -> {
                with(response as TrackSearchResponse){
                    val data = results.map {
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
                    emit(Resource.Success(data))
                }
            }
            else -> {
                emit(Resource.Error("Ошибка сервера"))
            }
        }
    }

    override fun readSearchHistory(): ArrayList<Track> {
        return localStorage.getHistory()
    }

    override fun addTrackToSearchHistory(track: Track) {
        localStorage.saveHistory(track)
    }

    override fun clearSearchHistory() {
        localStorage.clearSearchHistory()
    }
}