package com.katoklizm.playlist_maker_full.data

import com.katoklizm.playlist_maker_full.data.search.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}