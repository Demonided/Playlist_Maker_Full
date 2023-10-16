package com.katoklizm.playlist_maker_full.app

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.katoklizm.playlist_maker_full.data.ConstSetting.SAVE_SUBJECT_KEY
import com.katoklizm.playlist_maker_full.data.ConstSetting.SHARED_PREF_SAVE_SUBJECT
import com.katoklizm.playlist_maker_full.domain.setting.model.ThemeSettings
import com.katoklizm.playlist_maker_full.domain.setting.model.ThemeState
import com.katoklizm.playlist_maker_full.util.Creator

class App : Application() {
    var currentTheme: ThemeState = ThemeState.LightTheme

    override fun onCreate() {
        super.onCreate()
        instance = this
        Creator.init(this)

        val settingInteractor = Creator.provideSettingInteractor()
        currentTheme = settingInteractor.getAppTheme()
        switchTheme(currentTheme)
    }

    fun switchTheme(theme: ThemeState) {
        currentTheme = theme
        saveThemeToPrefs(currentTheme)

        val nightMode = when (theme) {
            is ThemeState.LightTheme -> AppCompatDelegate.MODE_NIGHT_NO
            is ThemeState.DarkTheme -> AppCompatDelegate.MODE_NIGHT_YES
        }

        AppCompatDelegate.setDefaultNightMode(nightMode)
    }

    private fun saveThemeToPrefs(theme: ThemeState) {
        val sharedPreferences = getSharedPreferences(SHARED_PREF_SAVE_SUBJECT, Context.MODE_PRIVATE)
        sharedPreferences.edit()
            .putBoolean(SAVE_SUBJECT_KEY, theme is ThemeState.DarkTheme)
            .apply()
    }

    companion object {
        lateinit var instance: App
            private set
    }
}