package fr.xebia.magritte.data.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = arrayOf(MagritteLabel::class), version = 1)
abstract class MagritteDatabase : RoomDatabase() {

    abstract fun magritteLabelDao(): MagritteLabelDao
}