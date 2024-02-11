package com.katoklizm.playlist_maker_full.di

import com.katoklizm.playlist_maker_full.data.album.impl.AlbumInfoRepositoryImpl
import com.katoklizm.playlist_maker_full.data.album.impl.AlbumPlaylistRepositoryImpl
import com.katoklizm.playlist_maker_full.data.album.impl.TrackAlbumPlaylistRepositoryImpl
import com.katoklizm.playlist_maker_full.data.favorite.impl.FavoriteTrackRepositoryImpl
import com.katoklizm.playlist_maker_full.data.player.impl.PlayerRepositoryImpl
import com.katoklizm.playlist_maker_full.data.search.network.TrackRepositoryImpl
import com.katoklizm.playlist_maker_full.data.sharing.impl.SettingRepositoryImpl
import com.katoklizm.playlist_maker_full.domain.album.AlbumPlaylistRepository
import com.katoklizm.playlist_maker_full.domain.album.TrackAlbumPlaylistRepository
import com.katoklizm.playlist_maker_full.domain.albuminfo.AlbumInfoRepository
import com.katoklizm.playlist_maker_full.domain.favorite.FavoriteTrackRepository
import com.katoklizm.playlist_maker_full.domain.player.PlayerRepository
import com.katoklizm.playlist_maker_full.domain.search.api.TrackRepository
import com.katoklizm.playlist_maker_full.domain.sharing.SettingRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {

    single<TrackRepository> {
        TrackRepositoryImpl(get(), get(), get())
    }

    single<SettingRepository> {
        SettingRepositoryImpl(get(), androidContext())
    }

    factory<PlayerRepository> {
        PlayerRepositoryImpl(get())
    }

    single<FavoriteTrackRepository> {
        FavoriteTrackRepositoryImpl(get())
    }

    single<AlbumPlaylistRepository> {
        AlbumPlaylistRepositoryImpl(get())
    }

    single<TrackAlbumPlaylistRepository> {
        TrackAlbumPlaylistRepositoryImpl(get())
    }

    single<AlbumInfoRepository> {
        AlbumInfoRepositoryImpl()
    }
}