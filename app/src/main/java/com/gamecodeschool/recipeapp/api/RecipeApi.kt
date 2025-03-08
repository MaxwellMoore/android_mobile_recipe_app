package com.gamecodeschool.recipeapp.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeApi {

    @GET("/recipes/complexSearch")
    suspend fun getRecipeSearch(
        @Query("apiKey") apiKey : String,
        @Query("query") query : String,
        @Query("cuisine") cuisine : String? = null,
        @Query("diet") diet : String? = null,
        @Query("intolerances") intolerances : String? = null
    ) : Response<RecipeSearchModel>

    @GET("/recipes/{id}/ingredientWidget.json")
    suspend fun getIngredients(
        @Path("id") id: Int,
        @Query("apiKey") apiKey : String
    ) : Response<IngredientsModel>

    @GET("/recipes/{id}/analyzedInstructions")
    suspend fun getInstructions(
        @Path("id") id: Int,
        @Query("apiKey") apiKey : String
    ) : Response<InstructionsModel>

    @GET("https://api.spoonacular.com/recipes/random")
    suspend fun getRecommended(
        @Query("apiKey") apiKey : String,
        @Query("number") number : Int
    ) : Response<RecommendedModel>
}