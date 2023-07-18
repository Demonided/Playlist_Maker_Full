package com.katoklizm.playlist_maker_full

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore.Audio.Media
import android.text.Editable
import android.text.TextWatcher
import com.katoklizm.playlist_maker_full.databinding.ActivityMainBinding
import com.katoklizm.playlist_maker_full.media_library.MediaLibraryActivity
import com.katoklizm.playlist_maker_full.search.SearchActivity
import com.katoklizm.playlist_maker_full.setting.SettingActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchPlaylistMaker.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
        binding.mediaLibraryPlaylistMaker.setOnClickListener {
            val intent = Intent(this, MediaLibraryActivity::class.java)
            startActivity(intent)
        }
        binding.settingPlaylistMaker.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }
    }
}