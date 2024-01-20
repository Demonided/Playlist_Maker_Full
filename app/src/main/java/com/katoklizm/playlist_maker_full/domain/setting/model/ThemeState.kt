package com.katoklizm.playlist_maker_full.domain.setting.model

sealed class ThemeState {
    object LightTheme: ThemeState()
    object DarkTheme: ThemeState()
}