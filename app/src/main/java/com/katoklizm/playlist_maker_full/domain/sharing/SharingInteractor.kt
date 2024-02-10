package com.katoklizm.playlist_maker_full.domain.sharing

import com.katoklizm.playlist_maker_full.domain.album.model.AlbumPlaylist

interface SharingInteractor {
    fun shareApp()
    fun shareAlbum(album: AlbumPlaylist)
    fun openTerms()
    fun openSupport()
}