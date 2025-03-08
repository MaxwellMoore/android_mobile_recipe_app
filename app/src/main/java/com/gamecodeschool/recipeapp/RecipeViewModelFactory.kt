package com.gamecodeschool.recipeapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RecipeViewModelFactory(private val repository: RecipeRepositoryInterface) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecipeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

