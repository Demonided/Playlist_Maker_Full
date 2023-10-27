package com.katoklizm.playlist_maker_full.di

import android.content.Context
import com.katoklizm.playlist_maker_full.app.App
import com.katoklizm.playlist_maker_full.data.NetworkClient
import com.katoklizm.playlist_maker_full.data.search.network.ITunesSearchApi
import com.katoklizm.playlist_maker_full.data.search.network.RetrofitNetworkClient
import com.katoklizm.playlist_maker_full.data.search.track.HistoryTrackManager
import com.katoklizm.playlist_maker_full.data.setting.impl.ThemeSettingsImpl
import com.katoklizm.playlist_maker_full.domain.player.PlayerState
import com.katoklizm.playlist_maker_full.domain.setting.model.ThemeSettings
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single {
        androidApplication() as App
    }

    single<ITunesSearchApi> {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://itunes.apple.com")
            .build()
            .create(ITunesSearchApi::class.java)
    }

    single {
        androidContext()
            .getSharedPreferences(App.APP_SHARED, Context.MODE_PRIVATE)
    }

    single<NetworkClient> {
        RetrofitNetworkClient(androidContext())
    }

    single {
        HistoryTrackManager(get())
    }

    single<ThemeSettings> {
        ThemeSettingsImpl(get())
    }

    single {

    }
}
