package com.gamecodeschool.recipeapp.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val baseUrl = "https://api.spoonacular.com";

    private fun getInstance() : Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val recipeApi : RecipeApi = getInstance().create(RecipeApi::class.java)

}