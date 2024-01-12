package com.katoklizm.playlist_maker_full.app

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.katoklizm.playlist_maker_full.data.db.AppDatabase
import com.katoklizm.playlist_maker_full.data.db.entity.TrackEntity
import com.katoklizm.playlist_maker_full.data.search.network.ITunesSearchApi
import com.katoklizm.playlist_maker_full.di.dataModule
import com.katoklizm.playlist_maker_full.di.interactorModule
import com.katoklizm.playlist_maker_full.di.repositoryModule
import com.katoklizm.playlist_maker_full.di.viewModelModule
import com.katoklizm.playlist_maker_full.domain.setting.SettingsInteractor
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    var currentTheme: Boolean = false

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(dataModule, repositoryModule, interactorModule, viewModelModule)
        }

        val settingsInteractor: SettingsInteractor by inject()

        currentTheme = settingsInteractor.lookAtThemeBoolean()
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
        const val APP_SHARED = "app_shared"
        const val BASE_URL = "http://itunes.apple.com"
    }
}