package com.katoklizm.playlist_maker_full.domain.setting.impl

import com.katoklizm.playlist_maker_full.domain.setting.SettingsInteractor
import com.katoklizm.playlist_maker_full.domain.setting.model.ThemeState
import com.katoklizm.playlist_maker_full.domain.setting.model.ThemeSettings

class SettingInteractorImpl(
    private var themeSettings: ThemeSettings
) : SettingsInteractor {

    override fun getAppTheme(): ThemeState {
        return themeSettings.lookAtTheme()
    }

    override fun setAppTheme(theme: ThemeState) {
        when (theme) {
            is ThemeState.LightTheme -> {
                themeSettings.setLightTheme()
                themeSettings.saveCurrentTheme(ThemeState.LightTheme) // Сохранение светлой темы
            }

            is ThemeState.DarkTheme -> {
                themeSettings.setDarkTheme()
                themeSettings.saveCurrentTheme(ThemeState.DarkTheme) // Сохранение темной темы
            }
        }
    }
}