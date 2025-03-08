package com.gamecodeschool.recipeapp.pages

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.gamecodeschool.recipeapp.components.RecipeCard
import com.gamecodeschool.recipeapp.RecipeViewModel
import com.gamecodeschool.recipeapp.api.NetworkResponse
import com.gamecodeschool.recipeapp.api.RecipeSearchModel
import com.gamecodeschool.recipeapp.R
import com.gamecodeschool.recipeapp.components.FilterDialog
import com.gamecodeschool.recipeapp.components.SearchResults

@Composable
fun SearchPage(recipeViewModel: RecipeViewModel, navController: NavController) {
    var query by remember { mutableStateOf("") }
    var selectedCuisine by remember { mutableStateOf("") }
    var selectedDiet by remember { mutableStateOf("") }
    var selectedIntolerances by remember { mutableStateOf("") }
    var showFilterDialog by remember { mutableStateOf(false) }

    val searchResult = recipeViewModel.searchResult.observeAsState()
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = query,
                onValueChange = {
                    query = it
                },
                label = {
                    Text(text = "Search for a recipe")
                },
                shape = RoundedCornerShape(50)
            )
            IconButton(
                onClick = {
                    recipeViewModel.getData(query, selectedCuisine, selectedDiet, selectedIntolerances)
                }
            ) {
                Icon(
                    modifier = Modifier.size(36.dp),
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search Icon Button"
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp, 0.dp, 8.dp, 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Button(
                onClick = {showFilterDialog = true},
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.orange), contentColor = Color.White),
                shape = RoundedCornerShape(50),
                contentPadding = PaddingValues(
                    start = 30.dp,
                    top = 4.dp,
                    end = 30.dp,
                    bottom = 4.dp
                ),
                modifier = Modifier
                    .width(100.dp)
                    .height(30.dp)
            ) {
                Text(
                    text = "filters",
                    style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal),

                )
            }
        }
        if (selectedCuisine != "" || selectedDiet != "" || selectedIntolerances != "") {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp, 0.dp, 8.dp, 16.dp)
                    .background(Color.LightGray, shape = RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(16.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                if (selectedCuisine != "") Text(text = "Cuisine: $selectedCuisine")
                if (selectedDiet != "") Text(text = "Diet: $selectedDiet")
                if (selectedIntolerances != "") Text(text = "Intolerances: $selectedIntolerances")
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (showFilterDialog) {
                FilterDialog(
                    onDismiss = { showFilterDialog = false },
                    onSubmitFilters = { cuisine, diet, intolerances ->
                        selectedCuisine = cuisine
                        selectedDiet = diet
                        selectedIntolerances = intolerances
                    }
                )
            }
        }

        when (val result = searchResult.value) {
            is NetworkResponse.Error -> {
                Text(text = result.message)
            }
            NetworkResponse.Loading -> {
                Text(text = "Loading")
            }
            is NetworkResponse.Success -> {
                SearchResults(data = result.data, recipeViewModel, navController)
            }
            null -> {}
        }
    }
}



