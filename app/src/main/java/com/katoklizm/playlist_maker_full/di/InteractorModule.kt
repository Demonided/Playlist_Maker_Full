package com.katoklizm.playlist_maker_full.di

import com.katoklizm.playlist_maker_full.domain.player.PlayerInteractor
import com.katoklizm.playlist_maker_full.domain.player.PlayerInteractorImpl
import com.katoklizm.playlist_maker_full.domain.search.api.TrackInteractor
import com.katoklizm.playlist_maker_full.domain.search.impl.TrackInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    single<TrackInteractor> {
        TrackInteractorImpl(get())
    }

}