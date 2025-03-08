package com.gamecodeschool.recipeapp

import androidx.lifecycle.LiveData
import com.gamecodeschool.recipeapp.api.IngredientsModel
import com.gamecodeschool.recipeapp.api.InstructionsModel
import com.gamecodeschool.recipeapp.api.NetworkResponse
import com.gamecodeschool.recipeapp.api.RecipeSearchModel
import com.gamecodeschool.recipeapp.api.RecommendedModel
import com.gamecodeschool.recipeapp.db.Favorites
import com.gamecodeschool.recipeapp.db.Recents

interface RecipeRepositoryInterface {
    // Recommended
    suspend fun getRecommended(number: Int): NetworkResponse<RecommendedModel>

    // Search
    suspend fun getRecipeSearch(query: String, cuisine: String?, diet: String?, intolerances: String?): NetworkResponse<RecipeSearchModel>

    // Recipe Page
    suspend fun getIngredients(id: Int): NetworkResponse<IngredientsModel>
    suspend fun getInstructions(id: Int): NetworkResponse<InstructionsModel>

    // Recents
    fun getRecentsList(): LiveData<List<Recents>>
    suspend fun isRecent(spoonacularId: Int): Boolean
    suspend fun getRecentsCount(): Int
    suspend fun addRecent(spoonacularId: Int, title: String, image: String)
    suspend fun deleteOldestRecent()

    // Favorites
    fun getFavoritesList(): LiveData<List<Favorites>>
    suspend fun isFavorite(spoonacularId: Int): Boolean
    suspend fun addFavorite(spoonacularId: Int, title: String, image: String)
    suspend fun deleteFavorite(spoonacularId: Int)
}