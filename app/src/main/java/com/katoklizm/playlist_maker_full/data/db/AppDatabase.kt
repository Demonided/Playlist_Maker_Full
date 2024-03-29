package com.katoklizm.playlist_maker_full.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.katoklizm.playlist_maker_full.app.App
import com.katoklizm.playlist_maker_full.data.db.dao.TrackDao
import com.katoklizm.playlist_maker_full.data.db.entity.TrackEntity

@Database(version = 1, entities = [TrackEntity::class])
abstract class AppDatabase: RoomDatabase() {

    abstract fun trackDao(): TrackDao
}