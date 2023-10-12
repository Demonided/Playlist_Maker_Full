package com.katoklizm.playlist_maker_full.data.setting.impl

import android.content.Context
import android.content.SharedPreferences
import com.katoklizm.playlist_maker_full.app.App
import com.katoklizm.playlist_maker_full.domain.setting.model.ThemeSettings

class ThemeSettingsImpl(private val application: App): ThemeSettings {

    companion object{
        const val THEME_PREFS = "theme_prefs"
        const val THEME_KEY = "app_theme"
    }

    private val themeSharedPreference: SharedPreferences =
        application.getSharedPreferences(THEME_PREFS, Context.MODE_PRIVATE)
    override fun setLightTheme(): Boolean {
        themeSharedPreference.edit().putBoolean(THEME_KEY, false).apply()
        return false
    }

    override fun setDarkTheme(): Boolean {
        themeSharedPreference.edit().putBoolean(THEME_KEY, true).apply()
        return true
    }

    override fun lookAtTheme(): Boolean {
        return themeSharedPreference.getBoolean(THEME_KEY, false)
    }
}