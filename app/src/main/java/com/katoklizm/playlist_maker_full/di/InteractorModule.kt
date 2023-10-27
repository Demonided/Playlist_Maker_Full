package com.katoklizm.playlist_maker_full.di

import com.katoklizm.playlist_maker_full.domain.player.PlayerInteractor
import com.katoklizm.playlist_maker_full.domain.player.PlayerInteractorImpl
import com.katoklizm.playlist_maker_full.domain.search.api.TrackInteractor
import com.katoklizm.playlist_maker_full.domain.search.impl.TrackInteractorImpl
import com.katoklizm.playlist_maker_full.domain.setting.SettingsInteractor
import com.katoklizm.playlist_maker_full.domain.setting.impl.SettingInteractorImpl
import com.katoklizm.playlist_maker_full.domain.sharing.SharingInteractor
import com.katoklizm.playlist_maker_full.domain.sharing.impl.SharingInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    single<TrackInteractor> {
        TrackInteractorImpl(get())
    }

    single <SharingInteractor> {
        SharingInteractorImpl(get())
    }

    single<SettingsInteractor> {
        SettingInteractorImpl(get())
    }

    single<PlayerInteractor> {
        PlayerInteractorImpl()
    }
}