package com.katoklizm.playlist_maker_full.domain.sharing

import com.katoklizm.playlist_maker_full.domain.album.model.AlbumPlaylist
import com.katoklizm.playlist_maker_full.domain.sharing.model.EmailData

interface SettingRepository {
    fun shareLink(shareAppLink: String)
    fun openLink(offer: String)
    fun shareAlbum(album: AlbumPlaylist)
    fun openEmail(email: EmailData)
    fun getShapeLink(): String
    fun getTermsLink(): String
    fun getSupportEmailData(): EmailData
}