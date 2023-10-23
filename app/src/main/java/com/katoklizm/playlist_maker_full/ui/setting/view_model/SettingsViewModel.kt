package com.katoklizm.playlist_maker_full.ui.setting.view_model

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.katoklizm.playlist_maker_full.domain.setting.SettingsInteractor
import com.katoklizm.playlist_maker_full.domain.setting.model.ThemeSettings
import com.katoklizm.playlist_maker_full.domain.setting.model.ThemeState
import com.katoklizm.playlist_maker_full.domain.sharing.SharingInteractor
import com.katoklizm.playlist_maker_full.util.Creator

class SettingsViewModel(
    private var sharingInteractor: SharingInteractor,
    private var settingsInteractor: SettingsInteractor
) : ViewModel() {

    init {
        sharingInteractor = Creator.provideSharingInteractor()
        settingsInteractor = Creator.provideSettingInteractor()
    }

    private val _themeStateSetting = MutableLiveData<ThemeState>()
    val themeStateSetting: LiveData<ThemeState> = _themeStateSetting

    private val _stateCheked = MutableLiveData<Boolean>()
    val stateCheked:LiveData<Boolean> = _stateCheked

    fun themeSetting() {
        val currentTheme = settingsInteractor.getAppTheme()
        val themeStateBoolean = settingsInteractor.lookAtThemeBoolean()
        val newTheme = if (currentTheme is ThemeState.DarkTheme) {
            ThemeState.DarkTheme
        } else {
            ThemeState.LightTheme
        }

        settingsInteractor.setAppTheme(newTheme)
        _themeStateSetting.value = newTheme

        if (newTheme is ThemeState.DarkTheme) {
            _themeStateSetting.postValue(ThemeState.DarkTheme)
            _stateCheked.postValue(themeStateBoolean)
        } else {
            _themeStateSetting.postValue(ThemeState.LightTheme)
            _stateCheked.postValue(themeStateBoolean)
        }
    }

    fun settingShareApp() {
        sharingInteractor.shareApp()
    }

    fun settingWriteSupport() {
        sharingInteractor.openSupport()
    }

    fun settingTermUse() {
        sharingInteractor.openTerms()
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SettingsViewModel(
                        Creator.provideSharingInteractor(),
                        Creator.provideSettingInteractor()
                    ) as T
                }
            }
    }
}