package com.katoklizm.playlist_maker_full.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.katoklizm.playlist_maker_full.databinding.ActivityMainBinding
import com.katoklizm.playlist_maker_full.ui.medialibrary.MediaLibraryActivity
import com.katoklizm.playlist_maker_full.ui.track.SearchActivity
import com.katoklizm.playlist_maker_full.app.App
import com.katoklizm.playlist_maker_full.ui.setting.activity.SettingActivity

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = applicationContext as App
        app.switchTheme(app.darkTheme)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        binding?.searchPlaylistMaker?.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
        binding?.mediaLibraryPlaylistMaker?.setOnClickListener {
            val intent = Intent(this, MediaLibraryActivity::class.java)
            startActivity(intent)
        }
        binding?.settingPlaylistMaker?.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }
     
    }
}