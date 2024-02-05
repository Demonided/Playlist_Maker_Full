package com.katoklizm.playlist_maker_full.ui.medialibrary.playlist

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.katoklizm.playlist_maker_full.ui.medialibrary.favorite.FavoriteTrackFragment

class MediaLibraryAdapter(fragmentManager: FragmentManager,lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> FavoriteTrackFragment.newInstance()
            else -> PlaylistFragment.newInstance()
        }
    }
}