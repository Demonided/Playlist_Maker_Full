package com.katoklizm.playlist_maker_full.app

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.katoklizm.playlist_maker_full.data.ConstSetting.SAVE_SUBJECT_KEY
import com.katoklizm.playlist_maker_full.data.ConstSetting.SHARED_PREF_SAVE_SUBJECT
import com.katoklizm.playlist_maker_full.domain.setting.model.ThemeSettings
import com.katoklizm.playlist_maker_full.domain.setting.model.ThemeState
import com.katoklizm.playlist_maker_full.util.Creator
import kotlin.math.log

class App : Application() {
    var currentTheme: Boolean = false
    private var themeChanged = false

    override fun onCreate() {
        super.onCreate()
        instance = this
        Creator.init(this)

        val settingInteractor = Creator.provideSettingInteractor()
        currentTheme = settingInteractor.lookAtThemeBoolean()

//        switchTheme(currentTheme)
        render(currentTheme)
        Log.d("SaveLog", "$currentTheme")
    }

    private fun render(state: Boolean) {
        if (state) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

//    fun switchTheme(theme: ThemeState) {
//        if (theme != currentTheme) {
//            currentTheme = theme
//            themeChanged = true
//        }
//    }

    companion object {
        lateinit var instance: App
            private set
    }
}