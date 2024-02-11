package com.katoklizm.playlist_maker_full.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.katoklizm.playlist_maker_full.data.db.entity.AlbumPlaylistEntity

@Dao
interface AlbumPlaylistDao {

    @Insert(entity = AlbumPlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbum(album: AlbumPlaylistEntity)

    @Query("DELETE FROM album_table WHERE id = :albumId")
    suspend fun deleteAlbumPlaylist(albumId: Int)

    @Query("SELECT * FROM album_table")
    suspend fun getAllAlbumPlaylistFlow(): List<AlbumPlaylistEntity>

    @Query("SELECT * FROM album_table WHERE id=:albumId")
    suspend fun getAlbumPlaylist(albumId: Int): AlbumPlaylistEntity

    @Update(entity = AlbumPlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateAlbumPlaylist(album: AlbumPlaylistEntity)
}