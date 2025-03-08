package com.gamecodeschool.recipeapp.api

data class EquipmentX(
    val id: Int,
    val image: String,
    val localizedName: String,
    val name: String,
    val temperature: TemperatureX
)