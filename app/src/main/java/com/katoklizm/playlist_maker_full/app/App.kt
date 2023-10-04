package com.katoklizm.playlist_maker_full.app

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.katoklizm.playlist_maker_full.data.ConstSetting.SAVE_SUBJECT_KEY
import com.katoklizm.playlist_maker_full.data.ConstSetting.SHARED_PREF_SAVE_SUBJECT

class App : Application() {

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        val sharedPreferences = getSharedPreferences(SHARED_PREF_SAVE_SUBJECT, Context.MODE_PRIVATE)
        darkTheme = sharedPreferences.getBoolean(SAVE_SUBJECT_KEY, false)
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        saveThemeToPrefs(darkThemeEnabled)

        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    private fun saveThemeToPrefs(darkThemeEnabled: Boolean) {
        val sharedPreferences = getSharedPreferences(SHARED_PREF_SAVE_SUBJECT, Context.MODE_PRIVATE)
        sharedPreferences.edit()
            .putBoolean(SAVE_SUBJECT_KEY, darkThemeEnabled)
            .apply()
    }

}