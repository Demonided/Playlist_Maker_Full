package com.katoklizm.playlist_maker_full.setting

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Switch
import com.google.android.material.switchmaterial.SwitchMaterial
import com.katoklizm.playlist_maker_full.R
import com.katoklizm.playlist_maker_full.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.settingsShareAppText
        binding.settingsWriteSupportText
        binding.settingsTermUseText

        binding.settingBack.setOnClickListener {
            finish()
        }

        binding.settingsNightSubject.setOnClickListener {
            // empty
        }

        binding.settingsShareApp.setOnClickListener {
            val url = getString(R.string.setting_link_yandex_practicum)
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, url)
            startActivity(intent)
        }

        binding.settingsWriteSupport.setOnClickListener {
            val message = getString(R.string.setting_message_text_email)
            val subject = getString(R.string.setting_subject_text_email)
            val shareIntent = Intent(Intent.ACTION_SENDTO)
            shareIntent.data = Uri.parse("mailto:")
            shareIntent.putExtra(
                Intent.EXTRA_EMAIL,
                arrayOf(getString(R.string.setting_application_developers_mail))
            )
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
            shareIntent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(shareIntent)
        }

        binding.settingsTermUse.setOnClickListener {
            val url = Uri.parse(getString(R.string.setting_link_offer))
            val intent = Intent(Intent.ACTION_VIEW, url)
            startActivity(intent)
        }

        themeSwitch()
    }

    private fun themeSwitch() {
        binding.themeSwitcher.setOnCheckedChangeListener { switcher, cheked ->
            (applicationContext as App).switchTheme(cheked)
        }
    }
}