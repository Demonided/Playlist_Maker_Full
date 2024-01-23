package com.katoklizm.playlist_maker_full.data.search.network

import android.util.Log
import com.katoklizm.playlist_maker_full.data.NetworkClient
import com.katoklizm.playlist_maker_full.data.db.AppDatabase
import com.katoklizm.playlist_maker_full.data.search.dto.TrackSearchRequest
import com.katoklizm.playlist_maker_full.data.search.dto.TrackSearchResponse
import com.katoklizm.playlist_maker_full.data.search.dto.getDataRelease
import com.katoklizm.playlist_maker_full.data.search.track.HistoryTrackManager
import com.katoklizm.playlist_maker_full.domain.search.api.TrackRepository
import com.katoklizm.playlist_maker_full.domain.search.model.Track
import com.katoklizm.playlist_maker_full.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackRepositoryImpl(
    private val networkClient: NetworkClient,
    private val localStorage: HistoryTrackManager,
    private val appDatabase: AppDatabase
) : TrackRepository {

    override fun searchTrack(term: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.executeNetworkRequest(TrackSearchRequest(term = term))
        Log.d("Examination", "Данный АПИ в $term" +
                "\n Из этого данный ")

        when(response.resultCode) {
            -1 -> {
                emit(Resource.Error(ERROR_MESSAGE_1))
            }
            200 -> {
                with(response as TrackSearchResponse){
//                    val favoriteTrackIds = appDatabase.trackDao().getAllFavoriteTrackIds()

                    val data = results.map {
                        Track(
                            id = it.id,
                            trackName = it.trackName,
                            artistName = it.artistName,
                            trackTimeMillis = it.trackTimeMillis,
                            artworkUrl100 = it.artworkUrl100,
                            collectionName = it.getDataRelease(),
                            releaseDate = it.releaseDate,
                            primaryGenreName = it.primaryGenreName,
                            country = it.country,
                            previewUrl = it.previewUrl,
                            isFavorite = it.isFavorite
                        )
                    }
                    Log.d("Examination", "Данный Id в data = ${data.map { it.id }}" +
                            "\n Из этого данный ")

                    emit(Resource.Success(data))
                }
            }
            else -> {
                emit(Resource.Error(ERROR_MESSAGE_2))
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

    companion object {
        const val ERROR_MESSAGE_1 = "Проверьте подключение к интернету"
        const val ERROR_MESSAGE_2 = "Ошибка сервера"
    }
}