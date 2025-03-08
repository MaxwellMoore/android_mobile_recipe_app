package com.gamecodeschool.recipeapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gamecodeschool.recipeapp.RecipeViewModel
import com.gamecodeschool.recipeapp.api.RecommendedModel

@Composable
fun Recommended(data: RecommendedModel, recipeViewModel: RecipeViewModel, navController: NavController) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .shadow(8.dp, shape = RoundedCornerShape(18.dp))
            .clip(RoundedCornerShape(18.dp))
            .background(color = Color.White)
            .padding(14.dp)
    ) {
        itemsIndexed(data.recipes, itemContent = {index, item ->
            RecipeCard(id = item.id, title = item.title, image = item.image, recipeViewModel = recipeViewModel, navController = navController)
        })
    }
}