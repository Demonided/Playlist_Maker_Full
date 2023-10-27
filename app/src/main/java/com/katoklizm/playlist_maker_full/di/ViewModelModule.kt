package com.katoklizm.playlist_maker_full.di

import com.katoklizm.playlist_maker_full.ui.audioplayer.viewmodel.AudioPlayerViewModel
import com.katoklizm.playlist_maker_full.ui.search.viewmodel.SearchViewModel
import com.katoklizm.playlist_maker_full.ui.setting.view_model.SettingsViewModel
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
}