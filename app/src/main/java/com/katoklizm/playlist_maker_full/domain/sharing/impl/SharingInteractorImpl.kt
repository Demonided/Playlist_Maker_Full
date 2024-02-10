package com.katoklizm.playlist_maker_full.domain.sharing.impl

import com.katoklizm.playlist_maker_full.domain.album.model.AlbumPlaylist
import com.katoklizm.playlist_maker_full.domain.sharing.SettingRepository
import com.katoklizm.playlist_maker_full.domain.sharing.SharingInteractor
import com.katoklizm.playlist_maker_full.domain.sharing.model.EmailData

class SharingInteractorImpl(
    private val settingRepository: SettingRepository
) : SharingInteractor {
    override fun shareApp() {
        settingRepository.shareLink(getShareAppLink())
    }

    override fun shareAlbum(album: AlbumPlaylist) {
        settingRepository.shareAlbum(album)
    }

    override fun openTerms() {
        settingRepository.openLink(getTermsLink())
    }

    override fun openSupport() {
        settingRepository.openEmail(getSupportEmailData())
    }

    private fun getShareAppLink(): String {
        return settingRepository.getShapeLink()
    }

    private fun getTermsLink(): String {
        return settingRepository.getTermsLink()
    }

    private fun getSupportEmailData(): EmailData {
        return settingRepository.getSupportEmailData()
    }
}