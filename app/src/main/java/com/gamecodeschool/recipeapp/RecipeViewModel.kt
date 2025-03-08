package com.gamecodeschool.recipeapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.gamecodeschool.recipeapp.api.Constant
import com.gamecodeschool.recipeapp.api.IngredientsModel
import com.gamecodeschool.recipeapp.api.InstructionsModel
import com.gamecodeschool.recipeapp.api.NetworkResponse
import com.gamecodeschool.recipeapp.api.RecipeSearchModel
import com.gamecodeschool.recipeapp.api.RecommendedModel
import com.gamecodeschool.recipeapp.api.RetrofitInstance
import com.gamecodeschool.recipeapp.db.Favorites
import com.gamecodeschool.recipeapp.db.Recents
import kotlinx.coroutines.Dispatchers
//import com.gamecodeschool.recipeapp.db.favoritesDao
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class RecipeViewModel(private val repository: RecipeRepositoryInterface) : ViewModel() {
    // Recommended
    private val _recommendedResults = MutableLiveData<NetworkResponse<RecommendedModel>>()
    val recommendedResults: LiveData<NetworkResponse<RecommendedModel>> = _recommendedResults

    fun getRecommended(number: Int) {
        _recommendedResults.value = NetworkResponse.Loading
        viewModelScope.launch {
            _recommendedResults.value = repository.getRecommended(number)
        }
    }

    // Search
    private val _searchResult = MutableLiveData<NetworkResponse<RecipeSearchModel>>()
    val searchResult: LiveData<NetworkResponse<RecipeSearchModel>> = _searchResult

    fun getData(query: String, cuisine: String?, diet: String?, intolerances: String?) {
        _searchResult.value = NetworkResponse.Loading
        viewModelScope.launch {
            _searchResult.value = repository.getRecipeSearch(query, cuisine, diet, intolerances)
        }
    }

    // Recipe Page
    private val _ingredients = MutableLiveData<NetworkResponse<IngredientsModel>>()
    val ingredients: LiveData<NetworkResponse<IngredientsModel>> = _ingredients

    fun getIngredients(id: Int) {
        _ingredients.value = NetworkResponse.Loading
        viewModelScope.launch {
            _ingredients.value = repository.getIngredients(id)
        }
    }

    private val _instructions = MutableLiveData<NetworkResponse<InstructionsModel>>()
    val instructions: LiveData<NetworkResponse<InstructionsModel>> = _instructions

    fun getInstructions(id: Int) {
        _instructions.value = NetworkResponse.Loading
        viewModelScope.launch {
            _instructions.value = repository.getInstructions(id)
        }
    }

    // Recents
    val recentsList: LiveData<List<Recents>> = repository.getRecentsList()

    fun isRecent(spoonacularId: Int): Boolean {
        return runBlocking {
            repository.isRecent(spoonacularId)
        }
    }

    fun getRecentsCount(): Int {
        return runBlocking {
            repository.getRecentsCount()
        }
    }

    fun addRecent(spoonacularId: Int, title: String, image: String) {
        viewModelScope.launch {
            repository.addRecent(spoonacularId, title, image)
        }
    }

    fun deleteOldestRecent() {
        viewModelScope.launch {
            repository.deleteOldestRecent()
        }
    }

    // Favorites
    val favoritesList: LiveData<List<Favorites>> = repository.getFavoritesList()

    fun isFavorite(spoonacularId: Int): Boolean {
        return runBlocking {
            repository.isFavorite(spoonacularId)
        }
    }

    fun addFavorite(spoonacularId: Int, title: String, image: String) {
        viewModelScope.launch {
            repository.addFavorite(spoonacularId, title, image)
        }
    }

    fun deleteFavorite(spoonacularId: Int) {
        viewModelScope.launch {
            repository.deleteFavorite(spoonacularId)
        }
    }
}