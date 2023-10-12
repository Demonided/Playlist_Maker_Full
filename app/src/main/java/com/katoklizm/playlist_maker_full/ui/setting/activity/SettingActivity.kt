package com.katoklizm.playlist_maker_full.ui.setting.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.katoklizm.playlist_maker_full.app.App
import com.katoklizm.playlist_maker_full.databinding.ActivitySettingBinding
import com.katoklizm.playlist_maker_full.ui.setting.view_model.SettingsViewModel

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

//    private fun themeSwitch() {
//        binding.themeSwitcher.setOnCheckedChangeListener { switcher, cheked ->
//            (applicationContext as App).switchTheme(cheked)
//        }
//
//        binding.themeSwitcher.isChecked = (applicationContext as App).darkTheme
//        // Это для того что бы switch при смене активити сохранял своё состояние
//    }
}