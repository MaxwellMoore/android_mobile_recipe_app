package com.gamecodeschool.recipeapp.components

import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.gamecodeschool.recipeapp.RecipeViewModel
import com.gamecodeschool.recipeapp.api.RecipeSearchModel

@Composable
fun SearchResults(data : RecipeSearchModel, recipeViewModel: RecipeViewModel, navController: NavController) {
    LazyColumn {
        itemsIndexed(data.results, itemContent = { index, item ->
            RecipeCard(item.id, item.title, item.image, recipeViewModel, navController)
        })
    }
}