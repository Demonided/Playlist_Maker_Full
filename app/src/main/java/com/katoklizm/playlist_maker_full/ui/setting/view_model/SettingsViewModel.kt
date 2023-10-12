package com.katoklizm.playlist_maker_full.ui.setting.view_model

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

    init {
        sharingInteractor = Creator.provideSharingInteractor()
        settingsInteractor = Creator.provideSettingInteractor()
    }

    private val _themeLiveData = MutableLiveData(settingsInteractor.getAppTheme())
    var themeLiveData: LiveData<ThemeState> = _themeLiveData

    private val finishActivityLiveData = MutableLiveData<Any>()
    val finishActivity: LiveData<Any> = finishActivityLiveData

    fun onBackClick() {
        finishActivityLiveData.value = Any()
    }

    fun themeSetting() {
        settingsInteractor.getAppTheme()
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