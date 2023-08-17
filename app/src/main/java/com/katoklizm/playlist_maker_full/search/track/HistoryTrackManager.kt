package com.katoklizm.playlist_maker_full.search.track

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.katoklizm.playlist_maker_full.search.track.ConstTrack.HISTORY_KEY
import com.katoklizm.playlist_maker_full.search.track.ConstTrack.PREFERENCE_NAME

class HistoryTrackManager(context: Context) {
    val prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveHistory(track: Track) {
        val historyTrackList = getHistory()
        var sizeTrack = historyTrackList.size

        if (sizeTrack < 10 && !historyTrackList.contains(track)) {
            historyTrackList.add(0, track)
        } else if (sizeTrack == 10 && !historyTrackList.contains(track)) {
            historyTrackList.add(0, track)
            historyTrackList.removeAt(9)
        } else {
            historyTrackList.remove(track)
            historyTrackList.add(0, track)
        }

        prefs.edit()
            .putString(HISTORY_KEY, Gson().toJson(historyTrackList))
            .apply()
    }

    fun getHistory(): ArrayList<Track> {
        val json = prefs.getString(HISTORY_KEY, "")
        val type = object : TypeToken<List<Track>>() {}.type
        return gson.fromJson(json, type) ?: arrayListOf()
    }
}