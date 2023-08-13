package com.katoklizm.playlist_maker_full.search.track

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.katoklizm.playlist_maker_full.search.track.ConstTrack.HISTORY_KEY
import com.katoklizm.playlist_maker_full.search.track.ConstTrack.PREFERENCE_NAME

class HistoryTrackManager(context: Context) {
    private val prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveHistory(historyList: ArrayList<Track>) {
        val limitedHistory = historyList
        val json = gson.toJson(limitedHistory)
        prefs.edit()
            .putString(HISTORY_KEY, json)
            .apply()
    }

    fun getHistory(): ArrayList<Track> {
        val json = prefs.getString(HISTORY_KEY, "")
        val type = object : TypeToken<List<Track>>() {}.type
        return gson.fromJson(json, type) ?: arrayListOf()
    }
}