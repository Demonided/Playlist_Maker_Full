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

//    @Query("DELETE FROM track_table WHERE id = :trackId")
//    suspend fun deleteTrack(trackId: Int): Int

    @Query("SELECT id FROM track_table WHERE isFavorite = 1")
    suspend fun getAllFavoriteTrackIds(): List<Int>

    @Query("SELECT * FROM track_table")
    fun getAllTrack(): Flow<List<TrackEntity>>

    @Query("SELECT * FROM track_table WHERE isFavorite = 1 AND id=:trackId")
    suspend fun getTrack(trackId: Int): TrackEntity
}