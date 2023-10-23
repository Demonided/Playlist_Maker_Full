package com.katoklizm.playlist_maker_full.domain.setting

import com.katoklizm.playlist_maker_full.domain.setting.model.ThemeState

interface SettingsInteractor {
    fun getAppTheme(): ThemeState

    fun setAppTheme(theme: ThemeState)

    fun lookAtThemeBoolean(): Boolean
}