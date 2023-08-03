package com.katoklizm.playlist_maker_full.search.track

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface iTunesSearchApi {

    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<TrackResponse>
}