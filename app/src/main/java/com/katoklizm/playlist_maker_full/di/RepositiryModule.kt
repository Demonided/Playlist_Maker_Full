package com.katoklizm.playlist_maker_full.di

import com.katoklizm.playlist_maker_full.data.search.network.TrackRepositoryImpl
import com.katoklizm.playlist_maker_full.data.sharing.impl.SettingRepositoryImpl
import com.katoklizm.playlist_maker_full.domain.search.api.TrackRepository
import com.katoklizm.playlist_maker_full.domain.sharing.SettingRepository
import org.koin.dsl.module

val repositoryModule = module {

    single<TrackRepository> {
        TrackRepositoryImpl(get(), get())
    }

    single<SettingRepository> {
        SettingRepositoryImpl(get())
    }


}