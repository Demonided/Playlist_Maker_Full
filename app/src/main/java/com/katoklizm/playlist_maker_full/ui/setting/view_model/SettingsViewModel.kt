package com.katoklizm.playlist_maker_full.ui.setting.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.katoklizm.playlist_maker_full.domain.setting.SettingsInteractor
import com.katoklizm.playlist_maker_full.domain.setting.model.ThemeState
import com.katoklizm.playlist_maker_full.domain.sharing.SharingInteractor
import com.katoklizm.playlist_maker_full.util.Creator

class SettingsViewModel(
    private var sharingInteractor: SharingInteractor,
    private var settingsInteractor: SettingsInteractor
) : ViewModel() {

    private val _themeStateSetting = MutableLiveData<ThemeState>()
    val themeStateSetting: LiveData<ThemeState> = _themeStateSetting

    private val _stateChecked = MutableLiveData<Boolean>()
    val stateChecked:LiveData<Boolean> = _stateChecked

    init {
        sharingInteractor = Creator.provideSharingInteractor()
        settingsInteractor = Creator.provideSettingInteractor()

        _themeStateSetting.postValue(settingsInteractor.getAppTheme())
        _stateChecked.postValue(settingsInteractor.lookAtThemeBoolean())
    }

    fun themeSetting() {
        val checkedState = settingsInteractor.lookAtThemeBoolean()
        if (getThemeState() is ThemeState.LightTheme) {
            settingsInteractor.setAppTheme(ThemeState.DarkTheme)
            _themeStateSetting.postValue(ThemeState.DarkTheme)
            _stateChecked.postValue(!checkedState)
        } else {
            settingsInteractor.setAppTheme(ThemeState.LightTheme)
            _themeStateSetting.postValue(ThemeState.LightTheme)
            _stateChecked.postValue(!checkedState)
        }
    }

    fun getThemeState(): ThemeState {
        return settingsInteractor.getAppTheme()
    }

    fun getThemeStateBoolean(): Boolean {
        return settingsInteractor.lookAtThemeBoolean()
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