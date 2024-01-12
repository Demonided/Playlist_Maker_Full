package com.katoklizm.playlist_maker_full.di

import com.katoklizm.playlist_maker_full.data.converters.TrackDbConverters
import com.katoklizm.playlist_maker_full.data.favorite.impl.FavoriteTrackRepositoryImpl
import com.katoklizm.playlist_maker_full.data.player.impl.PlayerRepositoryImpl
import com.katoklizm.playlist_maker_full.data.search.network.TrackRepositoryImpl
import com.katoklizm.playlist_maker_full.data.sharing.impl.SettingRepositoryImpl
import com.katoklizm.playlist_maker_full.domain.favorite.FavoriteTrackRepository
import com.katoklizm.playlist_maker_full.domain.player.PlayerRepository
import com.katoklizm.playlist_maker_full.domain.search.api.TrackRepository
import com.katoklizm.playlist_maker_full.domain.sharing.SettingRepository
import org.koin.dsl.module
import kotlin.math.sin

val repositoryModule = module {

    single<TrackRepository> {
        TrackRepositoryImpl(get(), get())
    }

    single<SettingRepository> {
        SettingRepositoryImpl(get())
    }

    factory<PlayerRepository> {
        PlayerRepositoryImpl(get())
    }

    factory { TrackDbConverters() }

    single<FavoriteTrackRepository> {
        FavoriteTrackRepositoryImpl(get(), get())
    }
}