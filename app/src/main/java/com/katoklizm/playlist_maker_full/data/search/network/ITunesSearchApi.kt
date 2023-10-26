package com.katoklizm.playlist_maker_full.data.search.network

import com.katoklizm.playlist_maker_full.data.search.dto.TrackSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesSearchApi {
    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<TrackSearchResponse>
}