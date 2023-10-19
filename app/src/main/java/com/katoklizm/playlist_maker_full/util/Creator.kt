package com.katoklizm.playlist_maker_full.util

import android.app.Activity
import android.content.Context
import com.katoklizm.playlist_maker_full.data.network.RetrofitNetworkClient
import com.katoklizm.playlist_maker_full.data.network.TrackRepositoryImpl
import com.katoklizm.playlist_maker_full.domain.api.TrackInteractor
import com.katoklizm.playlist_maker_full.domain.api.TrackRepository
import com.katoklizm.playlist_maker_full.domain.impl.TrackInteractorImpl
import com.katoklizm.playlist_maker_full.domain.player.PlayerInteractor
import com.katoklizm.playlist_maker_full.domain.player.PlayerInteractorImpl
import com.katoklizm.playlist_maker_full.domain.player.PlayerRepository
import com.katoklizm.playlist_maker_full.presentation.TrackSearchController
import com.katoklizm.playlist_maker_full.ui.track.TrackAdapter

object Creator {
    private fun getTrackRepository(context: Context): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient(context))
    }

    fun provideTrackInteractor(context: Context): TrackInteractor {
        return TrackInteractorImpl(getTrackRepository(context))
    }

    fun provideTrackSearchController(activity: Activity, adapter: TrackAdapter): TrackSearchController {
        return TrackSearchController(activity, adapter)
    }

    fun providePlayerRepository():PlayerRepository {
        return PlayerRepositoryImpl()
    }

    fun providePlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl()
    }
}