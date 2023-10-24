package com.katoklizm.playlist_maker_full.data.setting.impl

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.katoklizm.playlist_maker_full.app.App
import com.katoklizm.playlist_maker_full.domain.setting.model.ThemeSettings
import com.katoklizm.playlist_maker_full.domain.setting.model.ThemeState

class ThemeSettingsImpl(private val application: App): ThemeSettings {

    private val themeSharedPreference: SharedPreferences =
        application.getSharedPreferences(THEME_PREFS, Context.MODE_PRIVATE)

    override fun setLightTheme(): ThemeState {
        themeSharedPreference.edit().putBoolean(THEME_KEY, true).apply()
        Log.d("SaveLog", "Set: false")
        return ThemeState.LightTheme
    }

    override fun setDarkTheme(): ThemeState {
        themeSharedPreference.edit().putBoolean(THEME_KEY, false).apply()
        Log.d("SaveLog", "Set: true")
        return ThemeState.DarkTheme
    }

    override fun lookAtTheme(): ThemeState {
        return if (themeSharedPreference.getBoolean(THEME_KEY, true)){
            ThemeState.LightTheme
        } else {
            ThemeState.DarkTheme
        }
    }

    override fun saveCurrentTheme(theme: ThemeState) {
        when (theme) {
            is ThemeState.LightTheme -> {
                Log.d("SaveLog", "Save: false")
                setLightTheme()
            }
            is ThemeState.DarkTheme -> {
                Log.d("SaveLog", "Save: true")
                setDarkTheme()
            }
        }
    }

    override fun lookAtThemeBoolean(): Boolean {
        return themeSharedPreference.getBoolean(THEME_KEY, true)
    }

    companion object{
        const val THEME_PREFS = "theme_prefs"
        const val THEME_KEY = "app_theme"
    }
}