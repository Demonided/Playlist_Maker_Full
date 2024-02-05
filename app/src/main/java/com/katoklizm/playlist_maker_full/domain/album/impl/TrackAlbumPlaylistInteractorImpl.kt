package com.katoklizm.playlist_maker_full.domain.album.impl

import com.katoklizm.playlist_maker_full.domain.album.TrackAlbumPlaylistInteractor
import com.katoklizm.playlist_maker_full.domain.album.TrackAlbumPlaylistRepository
import com.katoklizm.playlist_maker_full.domain.album.model.TrackAlbumPlaylist
import kotlinx.coroutines.flow.Flow

class TrackAlbumPlaylistInteractorImpl(
    private val repositoryTrackAlbum: TrackAlbumPlaylistRepository
) : TrackAlbumPlaylistInteractor{
    override suspend fun insertTrackAlbum(trackAlbum: TrackAlbumPlaylist) {
        repositoryTrackAlbum.insertTrackAlbum(trackAlbum)
    }

    override suspend fun deleteTrackAlbum(trackId: Int) {
        repositoryTrackAlbum.deleteTrackAlbum(trackId)
    }

    override fun getAllTrackAlbum(): Flow<List<TrackAlbumPlaylist>> {
        return repositoryTrackAlbum.getAllTrackAlbum()
    }
}