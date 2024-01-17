package com.katoklizm.playlist_maker_full.di

import android.content.Context
import android.media.MediaPlayer
import androidx.room.Room
import androidx.room.RoomDatabase
import com.katoklizm.playlist_maker_full.app.App
import com.katoklizm.playlist_maker_full.app.App.Companion.BASE_URL
import com.katoklizm.playlist_maker_full.data.NetworkClient
import com.katoklizm.playlist_maker_full.data.db.AppDatabase
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
            .baseUrl(BASE_URL)
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

    factory {
        MediaPlayer()
    }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "get_track_database")
            .build()
    }
}

