package com.katoklizm.playlist_maker_full.app

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.katoklizm.playlist_maker_full.data.ConstSetting.SAVE_SUBJECT_KEY
import com.katoklizm.playlist_maker_full.data.ConstSetting.SHARED_PREF_SAVE_SUBJECT
import com.katoklizm.playlist_maker_full.domain.setting.model.ThemeState
import com.katoklizm.playlist_maker_full.util.Creator

class App : Application() {

//    var darkTheme = ThemeState.LightTheme
    var darkTheme = false
    override fun onCreate() {
        super.onCreate()
        instance = this
        Creator.init(this)

        val settingInteractor = Creator.provideSettingInteractor()
//        val theme = settingInteractor.setAppTheme(darkTheme)

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

    companion object {
        lateinit var instance: App
            private set
    }

}