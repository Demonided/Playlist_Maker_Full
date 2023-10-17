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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val isCheckedThemeSwitch = Creator.provideThemeSettings()
        val isChecked = isCheckedThemeSwitch.lookAtTheme()

        settingsViewModel = ViewModelProvider(
            this,
            SettingsViewModel.getViewModelFactory()
        )[SettingsViewModel::class.java]

        binding.settingsShareAppText
        binding.settingsWriteSupportText
        binding.settingsTermUseText

        settingsViewModel.finishActivity.observe(this, Observer {
            finish()
        })

        binding.settingBack.setOnClickListener {
            settingsViewModel.onBackClick()
        }

        binding.themeSwitcher.isChecked = isChecked is ThemeState.LightTheme
        binding.themeSwitcher.setOnClickListener {
            settingsViewModel.themeSetting()
            binding.themeSwitcher.isChecked = isChecked is ThemeState.DarkTheme

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