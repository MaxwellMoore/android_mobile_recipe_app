package com.gamecodeschool.recipeapp

import android.app.Application
import androidx.room.Room
import com.gamecodeschool.recipeapp.api.RetrofitInstance
import com.gamecodeschool.recipeapp.db.AppDatabase

class MainApplication : Application() {

    lateinit var repository: RecipeRepository

    override fun onCreate() {
        super.onCreate()

        // Initialize the database and DAOs
        val database = AppDatabase.getInstance(this)
        val recentsDao = database.getRecentsDao()
        val favoritesDao = database.getFavoritesDao()

        // Initialize the Retrofit API
        val recipeApi = RetrofitInstance.recipeApi

        // Initialize the repository
        repository = RecipeRepository(recipeApi, recentsDao, favoritesDao)
    }
}