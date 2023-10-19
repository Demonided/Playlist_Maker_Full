package com.katoklizm.playlist_maker_full.domain.setting.model

interface ThemeSettings {
    fun setLightTheme(): ThemeState
    fun setDarkTheme(): ThemeState
    fun lookAtTheme(): ThemeState
    fun saveCurrentTheme(theme: ThemeState)
}