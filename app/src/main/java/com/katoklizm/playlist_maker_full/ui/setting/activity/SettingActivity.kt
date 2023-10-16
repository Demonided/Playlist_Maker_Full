package com.katoklizm.playlist_maker_full.ui.setting.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    private lateinit var themeSettings: ThemeSettings
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        themeSettings = Creator.provideThemeSettings()

        settingsViewModel = ViewModelProvider(
            this,
            SettingsViewModel.getViewModelFactory()
        )[SettingsViewModel::class.java]

        val currentTheme = themeSettings.lookAtTheme()
        if (currentTheme is ThemeState.DarkTheme) {
            // Включить темный режим
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            // Выключить темный режим
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        binding.settingsShareAppText
        binding.settingsWriteSupportText
        binding.settingsTermUseText

        settingsViewModel.finishActivity.observe(this, Observer {
            finish()
        })

        binding.settingBack.setOnClickListener {
            settingsViewModel.onBackClick()
        }

        binding.settingsNightSubject.setOnClickListener {
            settingsViewModel.themeSetting()
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
}