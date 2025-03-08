package com.gamecodeschool.recipeapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.gamecodeschool.recipeapp.api.Amount
import com.gamecodeschool.recipeapp.api.Equipment
import com.gamecodeschool.recipeapp.api.Ingredient
import com.gamecodeschool.recipeapp.api.IngredientX
import com.gamecodeschool.recipeapp.api.IngredientsModel
import com.gamecodeschool.recipeapp.api.InstructionsModel
import com.gamecodeschool.recipeapp.api.InstructionsModelItem
import com.gamecodeschool.recipeapp.api.Length
import com.gamecodeschool.recipeapp.api.Metric
import com.gamecodeschool.recipeapp.api.NetworkResponse
import com.gamecodeschool.recipeapp.api.RecipeApi
import com.gamecodeschool.recipeapp.api.RecipeSearchModel
import com.gamecodeschool.recipeapp.api.RecommendedModel
import com.gamecodeschool.recipeapp.api.Result
import com.gamecodeschool.recipeapp.api.Step
import com.gamecodeschool.recipeapp.api.Temperature
import com.gamecodeschool.recipeapp.api.Us
import com.gamecodeschool.recipeapp.db.AppDatabase
import com.gamecodeschool.recipeapp.db.Favorites
import com.gamecodeschool.recipeapp.db.FavoritesDao
import com.gamecodeschool.recipeapp.db.Recents
import com.gamecodeschool.recipeapp.db.RecentsDao
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.createTestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class RecipeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    
    private lateinit var repository: RecipeRepositoryInterface
    private lateinit var viewModel: RecipeViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        val recentsList: LiveData<List<Recents>> = MutableLiveData(emptyList())
        coEvery { repository.getRecentsList() } returns recentsList
        coEvery { repository.isRecent(any()) } returns false
        coEvery { repository.isFavorite(any()) } returns false
        coEvery { repository.getRecentsCount() } returns 0
        coEvery { repository.getFavoritesList() } returns MutableLiveData(emptyList())
        val favoritesList: LiveData<List<Favorites>> = MutableLiveData(emptyList())
        coEvery { repository.getFavoritesList() } returns favoritesList
        coEvery { repository.isFavorite(any()) } returns false
        coEvery { repository.addFavorite(any(), any(), any()) } returns Unit
        coEvery { repository.deleteFavorite(any()) } returns Unit
        viewModel = RecipeViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cancel()
    }

    @Test
    fun `test getRecommended`() = testScope.runTest {
        val mockResponse = NetworkResponse.Success(RecommendedModel(emptyList()))
        val number = 5
        coEvery { repository.getRecommended(number) } returns mockResponse
        val observer = Observer<NetworkResponse<RecommendedModel>> {}
        try {
            viewModel.recommendedResults.observeForever(observer)
            viewModel.getRecommended(number)
            assertEquals(viewModel.recommendedResults.value, NetworkResponse.Loading)
            testDispatcher.scheduler.runCurrent()
            assertEquals(viewModel.recommendedResults.value, mockResponse)
        } finally {
            viewModel.recommendedResults.removeObserver(observer)
        }
    }

    @Test
    fun `test getData`() = testScope.runTest {
        val mockQuery = "chicken"
        val mockCuisine = "Italian"
        val mockDiet = "Gluten Free"
        val mockIntolerances = "Dairy"
        val mockResults = listOf(
            Result(
                id = 1,
                image = "image1.jpg",
                imageType = "jpg",
                title = "Chicken Alfredo"
            ),
            Result(
                id = 2,
                image = "image2.jpg",
                imageType = "jpg",
                title = "Chicken Parmesan"
            )
        )
        val mockResponse = NetworkResponse.Success(
            RecipeSearchModel(
                number = "2",
                offset = "0",
                results = mockResults,
                totalResults = "2"
            )
        )
        coEvery { repository.getRecipeSearch(mockQuery, mockCuisine, mockDiet, mockIntolerances) } returns mockResponse
        val observer = Observer<NetworkResponse<RecipeSearchModel>> {}
        try {
            viewModel.searchResult.observeForever(observer)
            viewModel.getData(mockQuery, mockCuisine, mockDiet, mockIntolerances)
            assertEquals(viewModel.searchResult.value, NetworkResponse.Loading)
            testDispatcher.scheduler.runCurrent()
            assertEquals(viewModel.searchResult.value, mockResponse)
        } finally {
            viewModel.searchResult.removeObserver(observer)
        }
    }

    @Test
    fun `test getIngredients`() = testScope.runTest {
        val mockId = 123
        val mockIngredients = listOf(
            Ingredient(
                amount = Amount(
                    metric = Metric(value = 200.0, unit = "g"),
                    us = Us(value = 7.05, unit = "oz")
                ),
                image = "image1.jpg",
                name = "Chicken Breast"
            )
        )
        val mockResponse = NetworkResponse.Success(
            IngredientsModel(
                ingredients = mockIngredients
            )
        )
        coEvery { repository.getIngredients(mockId) } returns mockResponse
        val observer = Observer<NetworkResponse<IngredientsModel>> {}
        try {
            viewModel.ingredients.observeForever(observer)
            viewModel.getIngredients(mockId)
            assertEquals(viewModel.ingredients.value, NetworkResponse.Loading)
            testDispatcher.scheduler.runCurrent()
            assertEquals(viewModel.ingredients.value, mockResponse)
        } finally {
            viewModel.ingredients.removeObserver(observer)
        }
    }

    @Test
    fun `test getInstructions`() = testScope.runTest {
        val mockId = 123
        val mockInstructions = listOf(
            InstructionsModelItem(
                name = "Step 1",
                steps = listOf(
                    Step(
                        equipment = listOf(
                            Equipment(id = 1, image = "image1.jpg", name = "Pan", temperature = Temperature(180.0, "Celsius"))
                        ),
                        ingredients = listOf(
                            IngredientX(id = 1, image = "image1.jpg", name = "Chicken Breast")
                        ),
                        length = Length(number = 1, unit = "unit"),
                        number = 1,
                        step = "Preheat pan to medium heat"
                    )
                )
            )
        )
        val mockResponse = NetworkResponse.Success(
            InstructionsModel().apply {
                addAll(mockInstructions)
            }
        )
        coEvery { repository.getInstructions(mockId) } returns mockResponse
        val observer = Observer<NetworkResponse<InstructionsModel>> {}
        try {
            viewModel.instructions.observeForever(observer)
            viewModel.getInstructions(mockId)
            assertEquals(viewModel.instructions.value, NetworkResponse.Loading)
            testDispatcher.scheduler.runCurrent()
            assertEquals(viewModel.instructions.value, mockResponse)
        } finally {
            viewModel.instructions.removeObserver(observer)
        }
    }

    @Test
    fun `test isRecent`() = testScope.runTest {
        val spoonacularId = 123
        val mockIsRecent = true
        coEvery { repository.isRecent(spoonacularId) } returns mockIsRecent
        val result = viewModel.isRecent(spoonacularId)
        assertEquals(mockIsRecent, result)
    }

    @Test
    fun `test getRecentsCount`() = testScope.runTest {
        val mockRecentsCount = 5
        coEvery { repository.getRecentsCount() } returns mockRecentsCount
        val result = viewModel.getRecentsCount()
        assertEquals(mockRecentsCount, result)
    }

    @Test
    fun `test addRecent`() = testScope.runTest {
        val spoonacularId = 123
        val title = "Chicken Alfredo"
        val image = "image1.jpg"
        coEvery { repository.addRecent(spoonacularId, title, image) } returns Unit
        viewModel.addRecent(spoonacularId, title, image)
        delay(100)
        coVerify { repository.addRecent(spoonacularId, title, image) }
    }

    @Test
    fun `test deleteOldestRecent`() = testScope.runTest {
        coEvery { repository.deleteOldestRecent() } returns Unit
        viewModel.deleteOldestRecent()
        delay(100)
        coVerify { repository.deleteOldestRecent() }
    }

    @Test
    fun `test isFavorite`() = testScope.runTest {
        val spoonacularId = 123
        val result = viewModel.isFavorite(spoonacularId)
        coVerify { repository.isFavorite(spoonacularId) }
        assert(result == false)
    }

    @Test
    fun `test addFavorite`() = testScope.runTest {
        val spoonacularId = 123
        val title = "Chicken Alfredo"
        val image = "image1.jpg"
        coEvery { repository.addFavorite(spoonacularId, title, image) } returns Unit
        viewModel.addFavorite(spoonacularId, title, image)
        delay(100)
        coVerify { repository.addFavorite(spoonacularId, title, image) }
    }

    @Test
    fun `test deleteFavorite`() = testScope.runTest {
        val spoonacularId = 123
        coEvery { repository.deleteFavorite(spoonacularId) } returns Unit
        viewModel.deleteFavorite(spoonacularId)
        delay(100)
        coVerify { repository.deleteFavorite(spoonacularId) }
    }
}