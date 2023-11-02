package com.katoklizm.playlist_maker_full.ui.medialibrary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.katoklizm.playlist_maker_full.R
import com.katoklizm.playlist_maker_full.databinding.ActivityMediaLibraryBinding
import com.katoklizm.playlist_maker_full.presentation.medialibrary.MediaLibraryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaLibraryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMediaLibraryBinding
    private val mediaLibraryViewModel by viewModel<MediaLibraryViewModel>()

    private lateinit var tabMediator: TabLayoutMediator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.settingBack.setOnClickListener {
            finish()
        }

        binding.viewPager.adapter =MediaLibraryAdapter(supportFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) {tab, position ->
            when(position) {
                0 -> tab.text = getString(R.string.favorite_tracks)
                1 -> tab.text = getString(R.string.playlist)
            }
        }

        tabMediator.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}