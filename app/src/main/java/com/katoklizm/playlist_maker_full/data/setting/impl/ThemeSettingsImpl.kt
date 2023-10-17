package com.katoklizm.playlist_maker_full.data.setting.impl

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.katoklizm.playlist_maker_full.app.App
import com.katoklizm.playlist_maker_full.domain.setting.model.ThemeSettings
import com.katoklizm.playlist_maker_full.domain.setting.model.ThemeState

class ThemeSettingsImpl(private val application: App): ThemeSettings {

    companion object{
        const val THEME_PREFS = "theme_prefs"
        const val THEME_KEY = "app_theme"
    }

    private val themeSharedPreference: SharedPreferences =
        application.getSharedPreferences(THEME_PREFS, Context.MODE_PRIVATE)

    override fun setLightTheme(): ThemeState {
        themeSharedPreference.edit().putBoolean(THEME_KEY, false).apply()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        return ThemeState.LightTheme
    }

    override fun setDarkTheme(): ThemeState {
        themeSharedPreference.edit().putBoolean(THEME_KEY, true).apply()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        return ThemeState.DarkTheme
    }

    override fun lookAtTheme(): ThemeState {
        return if (themeSharedPreference.getBoolean(THEME_KEY, false)){
            ThemeState.DarkTheme
        } else {
            ThemeState.LightTheme
        }
    }

    override fun saveCurrentTheme(theme: ThemeState) {
        when (theme) {
            is ThemeState.LightTheme -> setLightTheme()
            is ThemeState.DarkTheme -> setDarkTheme()
        }
    }
}