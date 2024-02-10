package com.katoklizm.playlist_maker_full.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.katoklizm.playlist_maker_full.data.db.dao.AlbumPlaylistDao
import com.katoklizm.playlist_maker_full.data.db.dao.TrackAlbumPlaylistDao
import com.katoklizm.playlist_maker_full.data.db.dao.TrackDao
import com.katoklizm.playlist_maker_full.data.db.entity.AlbumPlaylistEntity
import com.katoklizm.playlist_maker_full.data.db.entity.TrackAlbumPlaylistEntity
import com.katoklizm.playlist_maker_full.data.db.entity.TrackEntity

@Database(version = 1, entities = [TrackEntity::class, AlbumPlaylistEntity::class, TrackAlbumPlaylistEntity::class])
abstract class AppDatabase: RoomDatabase() {

    abstract fun trackDao(): TrackDao

    abstract fun albumPlaylistDao(): AlbumPlaylistDao

    abstract fun trackAlbumPlaylistDao(): TrackAlbumPlaylistDao
}