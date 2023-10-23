package com.katoklizm.playlist_maker_full.ui.setting.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.katoklizm.playlist_maker_full.app.App
import com.katoklizm.playlist_maker_full.databinding.ActivitySettingBinding
import com.katoklizm.playlist_maker_full.domain.setting.model.ThemeSettings
import com.katoklizm.playlist_maker_full.domain.setting.model.ThemeState
import com.katoklizm.playlist_maker_full.ui.setting.view_model.SettingsViewModel
import com.katoklizm.playlist_maker_full.util.Creator

class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding
    private lateinit var settingsViewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        settingsViewModel = ViewModelProvider(
            this,
            SettingsViewModel.getViewModelFactory()
        )[SettingsViewModel::class.java]

        binding.settingsShareAppText
        binding.settingsWriteSupportText
        binding.settingsTermUseText

        settingsViewModel.themeStateSetting.observe(this) {
            render(it)
        }

//        settingsViewModel.stateChecked.observe(this) {
//            isCheked(it)
//        }

        binding.settingBack.setOnClickListener {
            finish()
        }

        binding.themeSwitcher.setOnClickListener {
            settingsViewModel.themeSetting(
                settingsViewModel.getThemeState(),
                settingsViewModel.getThemeStateBoolean()
            )
        }

        binding.settingsShareApp.setOnClickListener {
            settingsViewModel.settingShareApp()
        }

        binding.settingsWriteSupport.setOnClickListener {
            settingsViewModel.settingWriteSupport()
        }

        binding.settingsTermUse.setOnClickListener {
            settingsViewModel.settingTermUse()
        }
    }

//    private fun isCheked(state: Boolean) {
//        binding.themeSwitcher.isChecked = state
//    }

    private fun render(state: ThemeState) {
        when(state) {
            ThemeState.DarkTheme -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.themeSwitcher.isChecked = settingsViewModel.getThemeStateBoolean()
                Log.d("getThemeState", "Такой статус 1 ${settingsViewModel.getThemeStateBoolean()}")
            }

            ThemeState.LightTheme -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.themeSwitcher.isChecked = settingsViewModel.getThemeStateBoolean()
                Log.d("getThemeState", "Такой статус 2 ${settingsViewModel.getThemeStateBoolean()}")
            }
        }
    }
}