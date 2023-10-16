package com.katoklizm.playlist_maker_full.util

import android.app.Activity
import android.app.Application
import android.content.Context
import com.katoklizm.playlist_maker_full.app.App
import com.katoklizm.playlist_maker_full.data.search.network.RetrofitNetworkClient
import com.katoklizm.playlist_maker_full.data.search.network.TrackRepositoryImpl
import com.katoklizm.playlist_maker_full.data.player.impl.PlayerRepositoryImpl
import com.katoklizm.playlist_maker_full.data.setting.impl.ThemeSettingsImpl
import com.katoklizm.playlist_maker_full.data.sharing.impl.ExternalNavigatorImpl
import com.katoklizm.playlist_maker_full.domain.search.api.TrackInteractor
import com.katoklizm.playlist_maker_full.domain.search.api.TrackRepository
import com.katoklizm.playlist_maker_full.domain.search.impl.TrackInteractorImpl
import com.katoklizm.playlist_maker_full.domain.player.PlayerInteractor
import com.katoklizm.playlist_maker_full.domain.player.PlayerInteractorImpl
import com.katoklizm.playlist_maker_full.domain.player.PlayerRepository
import com.katoklizm.playlist_maker_full.domain.setting.SettingsInteractor
import com.katoklizm.playlist_maker_full.domain.setting.impl.SettingInteractorImpl
import com.katoklizm.playlist_maker_full.domain.setting.model.ThemeSettings
import com.katoklizm.playlist_maker_full.domain.sharing.ExternalNavigator
import com.katoklizm.playlist_maker_full.domain.sharing.SharingInteractor
import com.katoklizm.playlist_maker_full.domain.sharing.impl.SharingInteractorImpl
import com.katoklizm.playlist_maker_full.presentation.TrackSearchController
import com.katoklizm.playlist_maker_full.ui.track.TrackAdapter

object Creator {
    private lateinit var application: App

    fun init(application: App) {
        this.application = application
    }

    private fun getTrackRepository(context: Context): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient(context))
    }

    fun provideTrackInteractor(context: Context): TrackInteractor {
        return TrackInteractorImpl(getTrackRepository(context))
    }

    fun provideTrackSearchController(activity: Activity, adapter: TrackAdapter): TrackSearchController {
        return TrackSearchController(activity, adapter)
    }

    fun providePlayerRepository():PlayerRepository {
        return PlayerRepositoryImpl()
    }

    fun providePlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl()
    }

    //sharing
    fun provideSharingInteractor(): SharingInteractor {
        return SharingInteractorImpl(provideExternalNavigator())
    }

    fun provideExternalNavigator(): ExternalNavigator {
        return ExternalNavigatorImpl(application)
    }

    //setting
    fun provideSettingInteractor(): SettingsInteractor {
        return SettingInteractorImpl(provideThemeSettings())
    }

    fun provideThemeSettings(): ThemeSettings {
        return ThemeSettingsImpl(application)
    }
}