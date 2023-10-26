package com.katoklizm.playlist_maker_full.domain.sharing.impl

import com.katoklizm.playlist_maker_full.domain.sharing.SettingRepository
import com.katoklizm.playlist_maker_full.domain.sharing.SharingInteractor
import com.katoklizm.playlist_maker_full.domain.sharing.model.EmailData

class SharingInteractorImpl(
    private val externalNavigator: SettingRepository
) : SharingInteractor {
    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    private fun getShareAppLink(): String {
        return externalNavigator.getShapeLink()
    }

    private fun getTermsLink(): String {
        return externalNavigator.getTermsLink()
    }

    private fun getSupportEmailData(): EmailData {
        return externalNavigator.getSupportEmailData()
    }
}