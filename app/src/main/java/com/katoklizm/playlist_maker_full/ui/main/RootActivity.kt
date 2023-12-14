package com.katoklizm.playlist_maker_full.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.katoklizm.playlist_maker_full.R
import com.katoklizm.playlist_maker_full.databinding.ActivityRootBinding
import com.katoklizm.playlist_maker_full.ui.medialibrary.MediaLibraryFragment
import com.katoklizm.playlist_maker_full.ui.search.SearchFragment
import com.katoklizm.playlist_maker_full.ui.setting.SettingFragment

class RootActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRootBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.rootFragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

//        supportFragmentManager.commit {
//            add(R.id.rootFragmentContainerView, MainActivity())
//        }

//        supportFragmentManager.commit {
//            add(R.id.rootFragmentContainerView, SearchFragment())
//        }
//
//        supportFragmentManager.commit {
//            add(R.id.rootFragmentContainerView, MediaLibraryFragment())
//        }
//
//        supportFragmentManager.commit {
//            add(R.id.rootFragmentContainerView, SettingFragment())
//        }
    }
}