package com.katoklizm.playlist_maker_full.data

import com.katoklizm.playlist_maker_full.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}