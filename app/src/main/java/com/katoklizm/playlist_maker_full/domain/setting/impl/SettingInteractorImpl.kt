package com.katoklizm.playlist_maker_full.domain.setting.impl

import com.katoklizm.playlist_maker_full.domain.setting.SettingsInteractor
import com.katoklizm.playlist_maker_full.domain.setting.model.ThemeState
import com.katoklizm.playlist_maker_full.domain.setting.model.ThemeSettings

class SettingInteractorImpl(
    private var themeSettings: ThemeSettings
) : SettingsInteractor {

    override fun getAppTheme(): ThemeState {
        val isDarkTheme = themeSettings.setLightTheme()
        return if (isDarkTheme) ThemeState.DarkTheme else ThemeState.LightTheme
    }

    override fun setAppTheme(theme: ThemeState) {
        when (theme) {
            is ThemeState.LightTheme -> themeSettings.setLightTheme()
            is ThemeState.DarkTheme -> themeSettings.setDarkTheme()
        }
    }

}