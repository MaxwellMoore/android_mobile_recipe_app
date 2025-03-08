package com.gamecodeschool.recipeapp.components

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.gamecodeschool.recipeapp.RecipeViewModel
import com.gamecodeschool.recipeapp.api.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun RecipeCard(id: Int, title: String, image: String, recipeViewModel: RecipeViewModel, navController: NavController) {
    var isFavoriteState by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(id) {
        if (id != null) {
            val isFavorite = withContext(Dispatchers.IO) {
                recipeViewModel.isFavorite(id)
            }
            isFavoriteState = isFavorite
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .shadow(8.dp, shape = RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .clickable {
                val encodedTitle = Uri.encode(title)
                val encodedImage = Uri.encode(image)
                navController.navigate("Recipe/${id}/$encodedTitle/$encodedImage")
            }
    ) {
        AsyncImage(
            model = image,
            contentDescription = title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(135.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .align(alignment = Alignment.CenterVertically),
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = title,
                style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Medium, color = Color.DarkGray)
            )
            Icon(
                imageVector = if (isFavoriteState) {
                    Icons.Filled.Favorite
                } else Icons.Outlined.FavoriteBorder,
                contentDescription = "Favorites Icon",
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .clickable {
                    if (!isFavoriteState) {
                        recipeViewModel.addFavorite(id, title, image)
                        isFavoriteState = true
                    } else {
                        recipeViewModel.deleteFavorite(id)
                        isFavoriteState = false
                    }
                }
            )
        }
    }
}