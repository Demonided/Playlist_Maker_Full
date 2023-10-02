package com.katoklizm.playlist_maker_full.data

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ConstTrack {
    const val ROUNDED_CORNERS_RADIUS = 10
    const val USER_TEXT = "USER_TEXT"

    const val PREFERENCE_NAME = "history_pref"
    const val HISTORY_KEY = "history_key"

    const val SAVE_TRACK = "save_track"

    fun formatDate(releasedDate: Date): String {
        return SimpleDateFormat("yyyy", Locale.getDefault()).format(releasedDate)
    }
}