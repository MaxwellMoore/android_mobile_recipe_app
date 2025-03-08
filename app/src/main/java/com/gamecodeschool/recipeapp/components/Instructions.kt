package com.gamecodeschool.recipeapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gamecodeschool.recipeapp.api.InstructionsModel

@Composable
fun Instructions(data : InstructionsModel) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, shape = RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(14.dp)
    ) {
        data.forEach {instructionItem ->
            if (instructionItem.name != "") {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Start)
                        .padding(bottom = 8.dp),
                    text = instructionItem.name,
                    style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Medium)
                )
            }

            instructionItem.steps.forEach { step ->
                Text(
                    text = "Step ${step.number}:",
                    style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Medium),
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                Text(
                    text = step.step,
                    style = TextStyle(fontSize = 20.sp),
                    color = Color.DarkGray,
                    modifier = Modifier.padding(bottom = 18.dp)
                )
            }
        }
    }
}