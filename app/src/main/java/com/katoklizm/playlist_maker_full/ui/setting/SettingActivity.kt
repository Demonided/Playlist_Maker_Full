package com.katoklizm.playlist_maker_full.ui.setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.katoklizm.playlist_maker_full.databinding.ActivitySettingBinding
import com.katoklizm.playlist_maker_full.domain.setting.model.ThemeState
import com.katoklizm.playlist_maker_full.presentation.setting.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding
    private val settingsViewModel by viewModel<SettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        settingsViewModel = ViewModelProvider(
//            this,
//            SettingsViewModel.getViewModelFactory()
//        )[SettingsViewModel::class.java]

        binding.settingsShareAppText
        binding.settingsWriteSupportText
        binding.settingsTermUseText

        settingsViewModel.themeStateSetting.observe(this) {
            render(it)
        }

        settingsViewModel.stateChecked.observe(this) {
            isCheked(it)
        }

        binding.settingBack.setOnClickListener {
            finish()
        }

        binding.themeSwitcher.setOnClickListener {
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

    private fun isCheked(state: Boolean) {
        binding.themeSwitcher.isChecked = !state
    }

    private fun render(state: ThemeState) {
        when(state) {
            ThemeState.DarkTheme -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }

            ThemeState.LightTheme -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }
}