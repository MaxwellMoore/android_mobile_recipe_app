package com.gamecodeschool.recipeapp

import androidx.lifecycle.LiveData
import com.gamecodeschool.recipeapp.api.Constant
import com.gamecodeschool.recipeapp.api.IngredientsModel
import com.gamecodeschool.recipeapp.api.InstructionsModel
import com.gamecodeschool.recipeapp.api.NetworkResponse
import com.gamecodeschool.recipeapp.api.RecipeApi
import com.gamecodeschool.recipeapp.api.RecipeSearchModel
import com.gamecodeschool.recipeapp.api.RecommendedModel
import com.gamecodeschool.recipeapp.db.Favorites
import com.gamecodeschool.recipeapp.db.FavoritesDao
import com.gamecodeschool.recipeapp.db.Recents
import com.gamecodeschool.recipeapp.db.RecentsDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecipeRepository(
    private val recipeApi: RecipeApi,
    private val recentsDao: RecentsDao,
    private val favoritesDao: FavoritesDao
) : RecipeRepositoryInterface {

    override suspend fun getRecommended(number: Int): NetworkResponse<RecommendedModel> {
        return try {
            val response = recipeApi.getRecommended(Constant.apiKey, number)
            if (response.isSuccessful) {
                response.body()?.let {
                    NetworkResponse.Success(it)
                } ?: NetworkResponse.Error("Empty response body")
            } else {
                NetworkResponse.Error("Network response error")
            }
        } catch (e: Exception) {
            NetworkResponse.Error("Exception: ${e.message}")
        }
    }

    // Search
    override suspend fun getRecipeSearch(query: String, cuisine: String?, diet: String?, intolerances: String?): NetworkResponse<RecipeSearchModel> {
        return try {
            val response = recipeApi.getRecipeSearch(Constant.apiKey, query, cuisine, diet, intolerances)
            if (response.isSuccessful) {
                response.body()?.let {
                    NetworkResponse.Success(it)
                } ?: NetworkResponse.Error("Empty response body")
            } else {
                NetworkResponse.Error("Network response error")
            }
        } catch (e: Exception) {
            NetworkResponse.Error("Exception: ${e.message}")
        }
    }

    // Recipe Page
    override suspend fun getIngredients(id: Int): NetworkResponse<IngredientsModel> {
        return try {
            val response = recipeApi.getIngredients(id, Constant.apiKey)
            if (response.isSuccessful) {
                response.body()?.let {
                    NetworkResponse.Success(it)
                } ?: NetworkResponse.Error("Empty response body")
            } else {
                NetworkResponse.Error("Failed to load ingredients")
            }
        } catch (e: Exception) {
            NetworkResponse.Error("Failed to load ingredients")
        }
    }

    override suspend fun getInstructions(id: Int): NetworkResponse<InstructionsModel> {
        return try {
            val response = recipeApi.getInstructions(id, Constant.apiKey)
            if (response.isSuccessful) {
                response.body()?.let {
                    NetworkResponse.Success(it)
                } ?: NetworkResponse.Error("Empty response body")
            } else {
                NetworkResponse.Error("Failed to load instructions")
            }
        } catch (e: Exception) {
            NetworkResponse.Error("Failed to load instructions")
        }
    }

    // Recents
    override fun getRecentsList(): LiveData<List<Recents>> {
        return recentsDao.getAllRecents()
    }

    override suspend fun isRecent(spoonacularId: Int): Boolean {
        return withContext(Dispatchers.IO) {
            recentsDao.findRecentById(spoonacularId) != null
        }
    }

    override suspend fun getRecentsCount(): Int {
        return withContext(Dispatchers.IO) {
            recentsDao.getRecentsCount()
        }
    }

    override suspend fun addRecent(spoonacularId: Int, title: String, image: String) {
        withContext(Dispatchers.IO) {
            recentsDao.addRecent(
                Recents(
                    spoonacularId = spoonacularId,
                    title = title,
                    image = image
                )
            )
        }
    }

    override suspend fun deleteOldestRecent() {
        withContext(Dispatchers.IO) {
            recentsDao.deleteOldestRecent()
        }
    }

    // Favorites
    override fun getFavoritesList(): LiveData<List<Favorites>> {
        return favoritesDao.getAllFavorites()
    }

    override suspend fun isFavorite(spoonacularId: Int): Boolean {
        return withContext(Dispatchers.IO) {
            favoritesDao.findFavoriteById(spoonacularId) != null
        }
    }

    override suspend fun addFavorite(spoonacularId: Int, title: String, image: String) {
        withContext(Dispatchers.IO) {
            favoritesDao.addFavorite(
                Favorites(
                    spoonacularId = spoonacularId,
                    title = title,
                    image = image
                )
            )
        }
    }

    override suspend fun deleteFavorite(spoonacularId: Int) {
        withContext(Dispatchers.IO) {
            favoritesDao.deleteFavorite(spoonacularId)
        }
    }
}