package com.gamecodeschool.recipeapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gamecodeschool.recipeapp.api.IngredientsModel

@Composable
fun Ingredients(data : IngredientsModel) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, shape = RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(14.dp)
    ) {
        data.ingredients.forEach {ingredient ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "${ingredient.name} : ${ingredient.amount.us.value}${ingredient.amount.us.unit}",
                    style = TextStyle(
                        fontSize = 22.sp,
                        color = Color.DarkGray
                    )
                )
            }
        }
    }
}