package com.katoklizm.playlist_maker_full.domain.albuminfo.impl

import com.katoklizm.playlist_maker_full.domain.album.model.AlbumPlaylist
import com.katoklizm.playlist_maker_full.domain.albuminfo.AlbumInfoInteractor
import com.katoklizm.playlist_maker_full.domain.albuminfo.AlbumInfoRepository
import kotlinx.coroutines.flow.StateFlow

class AlbumInfoInteractorImpl(
    private val albumInfoRepository: AlbumInfoRepository
) : AlbumInfoInteractor{
    override fun setPlaylist(value: AlbumPlaylist?) {
        albumInfoRepository.setPlaylist(value)
    }

    override fun getPlaylist(): StateFlow<AlbumPlaylist?> {
        return albumInfoRepository.getPlaylist()
    }
}