package com.katoklizm.playlist_maker_full.data.sharing.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.katoklizm.playlist_maker_full.R
import com.katoklizm.playlist_maker_full.app.App
import com.katoklizm.playlist_maker_full.data.converters.AlbumDbConverters.getTrackQuantityString
import com.katoklizm.playlist_maker_full.domain.album.model.AlbumPlaylist
import com.katoklizm.playlist_maker_full.domain.search.model.Track
import com.katoklizm.playlist_maker_full.domain.sharing.SettingRepository
import com.katoklizm.playlist_maker_full.domain.sharing.model.EmailData

class SettingRepositoryImpl(
    private val application: App,
    private val context: Context) : SettingRepository {
    override fun shareLink(shareAppLink: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, shareAppLink)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        application.startActivity(intent)
    }

    override fun openLink(offer: String) {
        val url = Uri.parse(offer)
        val intent = Intent(Intent.ACTION_VIEW, url)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        application.startActivity(intent)
    }

    override fun shareAlbum(album: AlbumPlaylist) {
        val intent = Intent(Intent.ACTION_SEND)
        val gson = Gson()
        val trackListType = object : TypeToken<List<Track>>() {}.type
        val track: List<Track> = gson.fromJson(album.track, trackListType)
        var stringShareAlbum = "${album.name}\n${album.description}\n${album.getTrackQuantityString()}"
        var count = 1
        for (i in track) {
            stringShareAlbum += "\n  $count. ${i.artistName} - ${i.trackName} (${i.trackTime})"
            ++count
        }
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, stringShareAlbum)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        application.startActivity(intent)
    }

    override fun openEmail(email: EmailData) {
        val message = application.getString(R.string.setting_message_text_email)
        val subject = application.getString(R.string.setting_subject_text_email)
        val shareIntent = Intent(Intent.ACTION_SENDTO)
        shareIntent.data = Uri.parse("mailto:")
        shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email.email))
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        shareIntent.putExtra(Intent.EXTRA_TEXT, message)
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        application.startActivity(shareIntent)
    }

    override fun getShapeLink(): String {
        return application.getString(R.string.setting_link_yandex_practicum)
    }

    override fun getTermsLink(): String {
        return application.getString(R.string.setting_link_offer)
    }

    override fun getSupportEmailData(): EmailData {
        return EmailData(application.getString(R.string.setting_application_developers_mail))
    }
}