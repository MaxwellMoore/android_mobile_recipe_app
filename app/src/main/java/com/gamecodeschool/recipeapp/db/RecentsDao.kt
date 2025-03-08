package com.gamecodeschool.recipeapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RecentsDao {

    @Query("SELECT * FROM Recents ORDER BY id DESC")
    fun getAllRecents() : LiveData<List<Recents>>

    @Query("SELECT * FROM Recents WHERE spoonacularId = :spoonacularId")
    fun findRecentById(spoonacularId: Int) : Recents?

    @Query("SELECT COUNT(*) FROM Recents")
    fun getRecentsCount() : Int

    @Insert
    fun addRecent(recentsItem: Recents)

    @Query("DELETE FROM Recents WHERE id = (SELECT MIN(id) FROM Recents)")
    fun deleteOldestRecent()
}