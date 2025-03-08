package com.gamecodeschool.recipeapp.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Recents(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var spoonacularId: Int,
    var title: String,
    var image: String
)

