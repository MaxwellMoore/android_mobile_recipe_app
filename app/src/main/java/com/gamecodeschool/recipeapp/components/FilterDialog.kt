package com.gamecodeschool.recipeapp.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.gamecodeschool.recipeapp.R

@Composable
fun FilterDialog(onDismiss: () -> Unit, onSubmitFilters: (String, String, String) -> Unit) {
    var selectedCuisine by remember { mutableStateOf("") }
    var selectedDiet by remember { mutableStateOf("") }
    var selectedIntolerances by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 8.dp,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Filter Options")

                Spacer(modifier = Modifier.height(16.dp))

                Section(title = "Cuisine", options = stringArrayResource(id = R.array.cuisine_options), maxHeight = 150.dp) {
                    selectedCuisine = it
                }
                Section(title = "Diet", options = stringArrayResource(id = R.array.diet_options), maxHeight = 150.dp) {
                    selectedDiet = it
                }
                Section(title = "Intolerances", options = stringArrayResource(id = R.array.intolerances_options), maxHeight = 150.dp) {
                    selectedIntolerances = it
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = {
                            onSubmitFilters(selectedCuisine, selectedDiet, selectedIntolerances)
                            onDismiss()
                                  },
                        colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.orange), contentColor = Color.White),
                    ) {
                        Text("Submit Filters")
                    }
                }
            }
        }
    }
}