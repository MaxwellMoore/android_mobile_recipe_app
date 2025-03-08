package com.gamecodeschool.recipeapp.pages

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.gamecodeschool.recipeapp.RecipeViewModel
import com.gamecodeschool.recipeapp.components.RecipeCard

@Composable
fun FavoritesPage(recipeViewModel: RecipeViewModel, navController: NavController) {
    val favoritesList by recipeViewModel.favoritesList.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = "Favorite Icon",
                modifier = Modifier
                    .size(36.dp)
                    .padding(end = 6.dp)
                )
            Text(
                text = "Favorites",
                style = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold)
            )
        }

        favoritesList?.let {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(favoritesList!!, itemContent = { index, item ->
                    RecipeCard(item.spoonacularId, item.title, item.image, recipeViewModel, navController)
                })
            }
        }
    }
}



