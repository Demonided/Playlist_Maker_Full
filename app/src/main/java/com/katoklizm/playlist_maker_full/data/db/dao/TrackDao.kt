package com.katoklizm.playlist_maker_full.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.katoklizm.playlist_maker_full.data.db.entity.TrackEntity

@Dao
interface TrackDao {

    @Insert(entity = TrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Delete(entity = TrackEntity::class)
    suspend fun deleteTrack(track: TrackEntity)

    @Query("DELETE FROM track_table WHERE id = :trackId")
    suspend fun deleteTrack(trackId: String)

    @Query("SELECT id FROM track_table")
    suspend fun getAllFavoriteTrackIds(): List<String>

//    @Query("SELECT * FROM track_table")
//    fun getAllTracksFlow(): Flow<List<TrackEntity>>

    @Query("SELECT * FROM track_table")
    suspend fun getAllTrack(): List<TrackEntity>

    @Query("SELECT * FROM track_table WHERE id=:trackId")
    suspend fun getTrack(trackId: String): TrackEntity
}