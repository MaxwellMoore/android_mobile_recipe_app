package com.gamecodeschool.recipeapp.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Section(title: String, options: Array<String>, maxHeight: Dp, onOptionSelected: (String) -> Unit) {
    var selectedOption by remember { mutableStateOf<String?>(null) }

    Column {
        Text("$title: (Scroll for more)")

        Box(modifier = Modifier
            .height(maxHeight)
            .padding(vertical = 8.dp)
            .fillMaxWidth()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(options) { option ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = selectedOption == option,
                            onClick = {
                                selectedOption = option
                                onOptionSelected(option)
                            }
                        )
                        Text(option)
                    }
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(16.dp))
}