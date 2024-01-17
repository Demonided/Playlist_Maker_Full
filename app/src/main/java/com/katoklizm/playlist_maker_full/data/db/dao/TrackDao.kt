package com.katoklizm.playlist_maker_full.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.katoklizm.playlist_maker_full.data.db.entity.TrackEntity
import com.katoklizm.playlist_maker_full.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {

    @Insert(entity = TrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun getTrackJob(track: TrackEntity)

    @Insert(entity = TrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: List<TrackEntity>)

    @Delete(entity = TrackEntity::class)
    suspend fun deleteTrack(track: TrackEntity)

    @Query("DELETE FROM track_table")
    suspend fun deleteAllTrack()
//
    @Query("SELECT * FROM track_table")
    fun getAllTrack(): Flow<List<TrackEntity>>

    @Query("SELECT * FROM track_table WHERE id LIKE :trackId")
    suspend fun getTrack(trackId: Int): TrackEntity
}