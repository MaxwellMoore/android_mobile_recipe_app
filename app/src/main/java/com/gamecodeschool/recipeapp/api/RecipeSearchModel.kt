package com.gamecodeschool.recipeapp.api

data class RecipeSearchModel(
    val number: String,
    val offset: String,
    val results: List<Result>,
    val totalResults: String
)