package com.gamecodeschool.recipeapp.pages

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.gamecodeschool.recipeapp.RecipeViewModel
import com.gamecodeschool.recipeapp.api.NetworkResponse
import com.gamecodeschool.recipeapp.api.RecommendedModel
import com.gamecodeschool.recipeapp.components.RecipeCard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.gamecodeschool.recipeapp.R
import com.gamecodeschool.recipeapp.components.Recommended

@Composable
fun HomePage(recipeViewModel: RecipeViewModel, navController: NavController) {
    val scrollState = rememberScrollState()
    val recommendedList = recipeViewModel.recommendedResults.observeAsState()
    val recentsList by recipeViewModel.recentsList.observeAsState()

    val recommendedLoaded = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (!recommendedLoaded.value) {
            recipeViewModel.getRecommended(5)
            recommendedLoaded.value = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Home,
                contentDescription = "Home Icon",
                modifier = Modifier
                    .size(40.dp)
                    .padding(end = 6.dp)
            )
            Text(
                text = "Homepage",
                style = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold)
            )
        }

        Box(
            modifier = Modifier
                .padding(vertical = 12.dp)
                .align(Alignment.Start)
                .shadow(8.dp, shape = RoundedCornerShape(50))
                .background(colorResource(id = R.color.blue))
                .padding(vertical = 8.dp, horizontal = 12.dp),
        ) {
            Text(
                text = "Recommended",
                style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Normal, color = colorResource(
                    id = R.color.light_white
                )),
            )
        }

        when (val result = recommendedList.value) {
            is NetworkResponse.Error -> {
                Text(text = result.message)
            }
            NetworkResponse.Loading -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                        .shadow(8.dp, shape = RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .border(
                            BorderStroke(1.dp, Color.LightGray),
                            RoundedCornerShape(16.dp)
                        )
                        .padding(14.dp)
                ) {
                    items(5) {
                        Text(
                            text = "Loading...",
                            style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 30.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            is NetworkResponse.Success -> {
                Recommended(data = result.data, recipeViewModel, navController)
            }
            null -> {}
        }

        Box(
            modifier = Modifier
                .padding(vertical = 12.dp)
                .align(Alignment.Start)
                .shadow(8.dp, shape = RoundedCornerShape(50))
                .background(colorResource(id = R.color.blue))
                .padding(vertical = 8.dp, horizontal = 12.dp),
        ) {
            Text(
                text = "Recent",
                style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Normal, color = colorResource(
                    id = R.color.light_white
                )),
            )
        }
        recentsList?.let {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .shadow(8.dp, shape = RoundedCornerShape(18.dp))
                    .clip(RoundedCornerShape(18.dp))
                    .background(color = Color.White)
                    .padding(14.dp)
            ) {
                itemsIndexed(recentsList!!, itemContent = { index, item ->
                    RecipeCard(item.spoonacularId, item.title, item.image, recipeViewModel, navController)
                })
            }
        }
    }
}

