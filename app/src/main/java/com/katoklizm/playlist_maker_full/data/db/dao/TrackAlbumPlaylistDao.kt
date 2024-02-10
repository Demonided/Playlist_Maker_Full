package com.katoklizm.playlist_maker_full.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.katoklizm.playlist_maker_full.data.db.entity.TrackAlbumPlaylistEntity
import com.katoklizm.playlist_maker_full.domain.album.model.TrackAlbumPlaylist

@Dao
interface TrackAlbumPlaylistDao {

    @Insert(entity = TrackAlbumPlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrackAlbum(albumTrack: TrackAlbumPlaylistEntity)

    @Query("DELETE FROM album_track_playlist WHERE trackId = :trackId")
    suspend fun deleteTrackAlbum(trackId: Int)

    @Query("SELECT * FROM album_track_playlist")
    suspend fun getAllTrackAlbum(): List<TrackAlbumPlaylistEntity>
}