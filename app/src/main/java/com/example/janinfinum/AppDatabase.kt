package com.example.janinfinum

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        Show2::class,
        Review2::class
    ], version = 1
)
abstract class AppDatabase : RoomDatabase() {

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val database = Room.databaseBuilder(context, AppDatabase::class.java, "database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries() //todo remove this???
                    .build()
                INSTANCE = database
                database
            }
        }
    }

    abstract fun showsDao(): ShowsDao

    abstract fun reviewDao(): ReviewDao

}