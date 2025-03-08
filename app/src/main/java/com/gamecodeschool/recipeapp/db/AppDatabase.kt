package com.gamecodeschool.recipeapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Favorites::class, Recents::class], version = 1)
abstract class AppDatabase : RoomDatabase(){

    abstract fun getFavoritesDao() : FavoritesDao
    abstract fun getRecentsDao() : RecentsDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            // Double-checked locking to ensure only one instance is created
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "App_Db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}