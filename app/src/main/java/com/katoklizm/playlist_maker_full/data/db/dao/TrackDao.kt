package com.katoklizm.playlist_maker_full.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.katoklizm.playlist_maker_full.data.db.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {

    @Insert(entity = TrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Delete(entity = TrackEntity::class)
    suspend fun deleteTrack(track: TrackEntity)

    @Query("DELETE FROM track_table WHERE trackId = :trackId")
    suspend fun deleteTrack(trackId: Int)

    @Query("SELECT trackId FROM track_table")
    suspend fun getAllFavoriteTrackIds(): List<Int>

    @Query("SELECT * FROM track_table order by insertionTime desc")
    suspend fun getAllTracksFlow(): List<TrackEntity>

    @Query("SELECT * FROM track_table order by insertionTime desc")
    suspend fun getAllTrack(): List<TrackEntity>

    @Query("SELECT * FROM track_table WHERE trackId=:trackId")
    suspend fun getTrack(trackId: String): TrackEntity
}