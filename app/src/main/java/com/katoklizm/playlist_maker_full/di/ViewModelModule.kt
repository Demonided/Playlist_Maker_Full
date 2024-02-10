package com.katoklizm.playlist_maker_full.di

import com.katoklizm.playlist_maker_full.presentation.albuminfo.AlbumInfoViewModel
import com.katoklizm.playlist_maker_full.presentation.audioplayer.AudioPlayerViewModel
import com.katoklizm.playlist_maker_full.presentation.medialibrary.MediaLibraryViewModel
import com.katoklizm.playlist_maker_full.presentation.medialibrary.favorite_track.FavoriteTrackViewModel
import com.katoklizm.playlist_maker_full.presentation.medialibrary.playlist.PlaylistViewModel
import com.katoklizm.playlist_maker_full.presentation.newplaylist.NewPlaylistViewModel
import com.katoklizm.playlist_maker_full.presentation.search.SearchViewModel
import com.katoklizm.playlist_maker_full.presentation.setting.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        SearchViewModel(get())
    }

    viewModel {
        AudioPlayerViewModel(androidContext(), get(), get(), get(), get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }

    viewModel {
        MediaLibraryViewModel()
    }

    // Fragment Media_library с которой в дальнейшем будем работать
    viewModel {
        FavoriteTrackViewModel(get())
    }

    // Fragment Media_library с которой в дальнейшем будем работать
    viewModel {
        PlaylistViewModel(get())
    }

    //
    viewModel {
        NewPlaylistViewModel(get())
    }

    viewModel {
        AlbumInfoViewModel(get(), get())
    }
}