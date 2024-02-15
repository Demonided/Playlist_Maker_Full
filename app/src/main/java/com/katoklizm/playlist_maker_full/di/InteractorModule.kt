package com.katoklizm.playlist_maker_full.di

import com.katoklizm.playlist_maker_full.domain.album.AlbumPlaylistInteractor
import com.katoklizm.playlist_maker_full.domain.album.TrackAlbumPlaylistInteractor
import com.katoklizm.playlist_maker_full.domain.album.impl.AlbumPlaylistInteractorImpl
import com.katoklizm.playlist_maker_full.domain.album.impl.TrackAlbumPlaylistInteractorImpl
import com.katoklizm.playlist_maker_full.domain.albuminfo.AlbumInfoInteractor
import com.katoklizm.playlist_maker_full.domain.albuminfo.impl.AlbumInfoInteractorImpl
import com.katoklizm.playlist_maker_full.domain.favorite.FavoriteTrackInteractor
import com.katoklizm.playlist_maker_full.domain.favorite.impl.FavoriteTrackInteractorImpl
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

    factory<PlayerInteractor> {
        PlayerInteractorImpl(get())
    }

    single<FavoriteTrackInteractor> {
        FavoriteTrackInteractorImpl(get())
    }

    single<AlbumPlaylistInteractor> {
        AlbumPlaylistInteractorImpl(get())
    }

    single<AlbumInfoInteractor> {
        AlbumInfoInteractorImpl(get())
    }

    single<TrackAlbumPlaylistInteractor> {
        TrackAlbumPlaylistInteractorImpl(get())
    }
}