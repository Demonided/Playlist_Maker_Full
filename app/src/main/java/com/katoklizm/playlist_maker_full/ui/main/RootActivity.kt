package com.katoklizm.playlist_maker_full.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.katoklizm.playlist_maker_full.R
import com.katoklizm.playlist_maker_full.databinding.ActivityRootBinding

class RootActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRootBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.rootFragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id) {
                R.id.newPlaylistFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                }
                else -> {
                    binding.bottomNavigationView.visibility = View.VISIBLE
                }

            }
        }

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