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
    fun getTrackJob(track: TrackEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: List<TrackEntity>)

    @Delete(entity = TrackEntity::class)
    suspend fun deleteTrack(track: TrackEntity)

    @Query("DELETE FROM track_table")
    suspend fun deleteAllTrack()

    @Query("SELECT * FROM track_table")
    suspend fun getAllTrack(): List<TrackEntity>

    @Query("SELECT * FROM track_table WHERE trackId LIKE :trackId")
    suspend fun getTrack(trackId: Int): TrackEntity
}