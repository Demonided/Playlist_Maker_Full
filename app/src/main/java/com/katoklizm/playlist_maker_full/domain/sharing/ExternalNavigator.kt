package com.katoklizm.playlist_maker_full.domain.sharing

import com.katoklizm.playlist_maker_full.domain.sharing.model.EmailData

interface ExternalNavigator {
    fun shareLink(shareAppLink: String)
    fun openLink(offer: String)
    fun openEmail(email: EmailData)
    fun getShapeLink(): String
    fun getTermsLink(): String
    fun getSupportEmailData(): EmailData
}