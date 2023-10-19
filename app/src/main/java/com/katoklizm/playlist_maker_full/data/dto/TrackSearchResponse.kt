package com.katoklizm.playlist_maker_full.data.dto

class TrackSearchResponse(
    val searchType: String,
    val term: String,
    val results: List<TrackDto>
) : Response()