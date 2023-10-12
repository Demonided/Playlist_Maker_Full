package com.katoklizm.playlist_maker_full.domain.setting.model

interface ThemeSettings {
    fun setLightTheme(): Boolean
    fun setDarkTheme(): Boolean
    fun lookAtTheme(): Boolean
}