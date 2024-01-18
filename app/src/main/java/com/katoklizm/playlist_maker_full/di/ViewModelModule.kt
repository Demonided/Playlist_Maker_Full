package com.katoklizm.playlist_maker_full.di

import com.katoklizm.playlist_maker_full.domain.search.model.Track
import com.katoklizm.playlist_maker_full.presentation.audioplayer.AudioPlayerViewModel
import com.katoklizm.playlist_maker_full.presentation.medialibrary.favorite_track.FavoriteTrackViewModel
import com.katoklizm.playlist_maker_full.presentation.medialibrary.MediaLibraryViewModel
import com.katoklizm.playlist_maker_full.presentation.medialibrary.playlist.PlaylistViewModel
import com.katoklizm.playlist_maker_full.presentation.search.SearchViewModel
import com.katoklizm.playlist_maker_full.presentation.setting.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        SearchViewModel(get())
    }

    viewModel {
        AudioPlayerViewModel(get(), get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }

    viewModel {
        MediaLibraryViewModel()
    }

    // Fragment Media_library с которой в дальнейшем будем работать
    viewModel {
        FavoriteTrackViewModel()
    }

    // Fragment Media_library с которой в дальнейшем будем работать
    viewModel {
        PlaylistViewModel()
    }
}