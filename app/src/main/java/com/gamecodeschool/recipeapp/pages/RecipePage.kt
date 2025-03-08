package com.gamecodeschool.recipeapp.pages

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.gamecodeschool.recipeapp.RecipeViewModel
import com.gamecodeschool.recipeapp.api.IngredientsModel
import com.gamecodeschool.recipeapp.api.InstructionsModel
import com.gamecodeschool.recipeapp.api.NetworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.gamecodeschool.recipeapp.R
import com.gamecodeschool.recipeapp.components.Ingredients
import com.gamecodeschool.recipeapp.components.Instructions

@Composable
fun RecipePage(id: Int?, title: String?, image: String?, recipeViewModel: RecipeViewModel) {
    val scrollState = rememberScrollState()
    val decodedImage = Uri.decode(image)
    var isFavoriteState by remember {
        mutableStateOf(false)
    }
    val ingredients = recipeViewModel.ingredients.observeAsState()
    val instructions = recipeViewModel.instructions.observeAsState()

    LaunchedEffect(id) {
        if (id != null) {
            val isFavorite = withContext(Dispatchers.IO) {
                recipeViewModel.isFavorite(id)
            }
            isFavoriteState = isFavorite

            recipeViewModel.getIngredients(id)
            recipeViewModel.getInstructions(id)

            val inRecents = withContext(Dispatchers.IO) {
                recipeViewModel.isRecent(id)
            }
            if (!inRecents) {
                if (title != null && decodedImage != null) {
                    val count = withContext(Dispatchers.IO) {
                        recipeViewModel.getRecentsCount()
                    }
                    if (count >= 10) {
                        recipeViewModel.deleteOldestRecent()
                    }
                    recipeViewModel.addRecent(id, title, decodedImage)
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = decodedImage,
            contentDescription = title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (title != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .shadow(8.dp, shape = RoundedCornerShape(16.dp))
                        .background(colorResource(id = R.color.blue))
                        .padding(vertical = 12.dp),
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = title,
                        textAlign = TextAlign.Center,
                        style = TextStyle(fontSize = 26.sp, fontWeight = FontWeight.Normal, color = colorResource(
                            id = R.color.light_white
                        )),
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
            Icon(
                imageVector = if (isFavoriteState) {
                    Icons.Filled.Favorite
                } else Icons.Outlined.FavoriteBorder,
                contentDescription = "Favorites Icon",
                modifier = Modifier
                    .size(30.dp)
                    .clickable {
                        if (id != null && title != null && image != null) {
                            if (!isFavoriteState) {
                                recipeViewModel.addFavorite(id, title, image)
                                isFavoriteState = true
                            } else {
                                recipeViewModel.deleteFavorite(id)
                                isFavoriteState = false
                            }
                        }
                    }
            )
        }

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Start)
                    .padding(vertical = 16.dp),
                text = "Ingredients",
                style = TextStyle(fontSize = 26.sp, fontWeight = FontWeight.Medium, color = Color.DarkGray),
            )

            when (val result = ingredients.value) {
                is NetworkResponse.Error -> {
                    Text(text = result.message)
                }
                NetworkResponse.Loading -> {
                    Text(text = "Loading")
                }
                is NetworkResponse.Success -> {
                    Ingredients(data = result.data)
                }
                null -> {}
            }

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Start)
                    .padding(vertical = 16.dp),
                text = "Instructions",
                style = TextStyle(fontSize = 26.sp, fontWeight = FontWeight.Medium, color = Color.DarkGray),
            )

            when (val result = instructions.value) {
                is NetworkResponse.Error -> {
                    Text(text = result.message)
                }
                NetworkResponse.Loading -> {
                    Text(text = "Loading")
                }
                is NetworkResponse.Success -> {
                    Instructions(data = result.data)
                }
                null -> {}
            }
        }
    }
}

