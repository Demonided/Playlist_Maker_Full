package com.katoklizm.playlist_maker_full.data.network

import com.katoklizm.playlist_maker_full.data.NetworkClient
import com.katoklizm.playlist_maker_full.data.dto.Response
import com.katoklizm.playlist_maker_full.data.dto.TrackSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient: NetworkClient {

    private val imdbBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(imdbBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(iTunesSearchApi::class.java)
    override fun doRequest(dto: Any): Response {
        if (dto is TrackSearchRequest) {
            val resp = iTunesService.search(dto.term).execute()

            val body = resp.body() ?: Response()

            return body.apply { resultCode = resp.code() }
        } else {
            return Response().apply { resultCode = 400 }
        }
    }
}