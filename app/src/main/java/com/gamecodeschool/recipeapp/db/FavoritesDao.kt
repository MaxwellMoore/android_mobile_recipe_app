package com.gamecodeschool.recipeapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavoritesDao {

    @Query("SELECT * FROM Favorites")
    fun getAllFavorites() : LiveData<List<Favorites>>

    @Query("SELECT * FROM Favorites WHERE spoonacularId = :spoonacularId")
    fun findFavoriteById(spoonacularId: Int) : Favorites?

    @Insert
    fun addFavorite(favoritesItem : Favorites)

    @Query("DELETE FROM Favorites WHERE spoonacularId = :spoonacularId")
    fun deleteFavorite(spoonacularId : Int)
}