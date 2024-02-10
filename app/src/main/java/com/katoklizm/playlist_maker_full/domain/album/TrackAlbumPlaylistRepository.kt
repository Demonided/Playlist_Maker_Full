package com.katoklizm.playlist_maker_full.domain.album

import com.katoklizm.playlist_maker_full.domain.album.model.TrackAlbumPlaylist
import kotlinx.coroutines.flow.Flow

interface TrackAlbumPlaylistRepository {

    suspend fun insertTrackAlbum(trackAlbum: TrackAlbumPlaylist)

    suspend fun deleteTrackAlbum(trackId: Int)

    fun getAllTrackAlbum(): Flow<List<TrackAlbumPlaylist>>
}