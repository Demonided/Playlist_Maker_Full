package com.katoklizm.playlist_maker_full.ui.medialibrary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.katoklizm.playlist_maker_full.R
import com.katoklizm.playlist_maker_full.databinding.ActivityMediaLibraryBinding

class MediaLibraryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMediaLibraryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.settingBack.setOnClickListener {
            finish()
        }
    }
}