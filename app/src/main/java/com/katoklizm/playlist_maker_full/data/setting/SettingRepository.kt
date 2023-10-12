package com.katoklizm.playlist_maker_full.data.setting

import com.katoklizm.playlist_maker_full.domain.setting.model.ThemeSettings

interface SettingRepository {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}