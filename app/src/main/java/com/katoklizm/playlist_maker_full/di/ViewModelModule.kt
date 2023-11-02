package com.katoklizm.playlist_maker_full.di

import com.katoklizm.playlist_maker_full.presentation.audioplayer.AudioPlayerViewModel
import com.katoklizm.playlist_maker_full.presentation.medialibrary.MediaLibraryViewModel
import com.katoklizm.playlist_maker_full.presentation.search.SearchViewModel
import com.katoklizm.playlist_maker_full.presentation.setting.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        SearchViewModel(get())
    }

    viewModel {
        AudioPlayerViewModel(get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }

    viewModel {
        MediaLibraryViewModel()
    }
}