package com.katoklizm.playlist_maker_full.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.katoklizm.playlist_maker_full.util.Creator

class App : Application() {
    var currentTheme: Boolean = false
    private var themeChanged = false

    override fun onCreate() {
        super.onCreate()
        instance = this
        Creator.init(this)

        val settingInteractor = Creator.provideSettingInteractor()
        currentTheme = settingInteractor.lookAtThemeBoolean()

        render(currentTheme)
    }

    private fun render(state: Boolean) {
        if (state) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    companion object {
        lateinit var instance: App
            private set
    }
}