package com.katoklizm.playlist_maker_full.data

import com.katoklizm.playlist_maker_full.data.search.dto.Response

interface NetworkClient {
    suspend fun executeNetworkRequest(dto: Any): Response
}