package com.gamecodeschool.recipeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gamecodeschool.recipeapp.pages.FavoritesPage
import com.gamecodeschool.recipeapp.pages.HomePage
import com.gamecodeschool.recipeapp.pages.RecipePage
import com.gamecodeschool.recipeapp.pages.SearchPage
import com.gamecodeschool.recipeapp.ui.theme.RecipeAppTheme

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

class MainActivity : ComponentActivity() {

    private lateinit var recipeViewModel: RecipeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = (application as MainApplication).repository
        val factory = RecipeViewModelFactory(repository)
        recipeViewModel = ViewModelProvider(this, factory).get(RecipeViewModel::class.java)

        enableEdgeToEdge()
        setContent {
            RecipeAppTheme {
                val navController = rememberNavController()

                val navItems = listOf(
                    BottomNavigationItem(
                        title = "Home",
                        selectedIcon = Icons.Filled.Home,
                        unselectedIcon = Icons.Outlined.Home
                    ),
                    BottomNavigationItem(
                        title = "Search",
                        selectedIcon = Icons.Filled.Search,
                        unselectedIcon = Icons.Outlined.Search
                    ),
                    BottomNavigationItem(
                        title = "Favorites",
                        selectedIcon = Icons.Filled.Favorite,
                        unselectedIcon = Icons.Outlined.FavoriteBorder
                    )
                )

                var selectedItemIndex by rememberSaveable {
                    mutableStateOf(0)
                }

                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Scaffold(
                        bottomBar = {
                            NavigationBar {
                                navItems.forEachIndexed { index, item ->
                                    NavigationBarItem(
                                        selected = selectedItemIndex == index,
                                        onClick = {
                                            selectedItemIndex = index
                                            navController.navigate(item.title)
                                        },
                                        label = {
                                              Text(text = item.title)  
                                        },
                                        icon = {
                                            Icon(
                                                imageVector = if(selectedItemIndex == index) {
                                                    item.selectedIcon
                                                } else item.unselectedIcon,
                                                contentDescription = item.title
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    ) {innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = "Home",
                            modifier = Modifier.padding(innerPadding),
                        ) {
                            composable(route = "Home") {
                                HomePage(recipeViewModel, navController)
                            }
                            composable(route = "Search") {
                                SearchPage(recipeViewModel, navController)
                            }
                            composable(route = "Favorites") {
                                FavoritesPage(recipeViewModel, navController)
                            }
                            composable(
                                route = "Recipe/{id}/{title}/{image}",
                                arguments = listOf(
                                    navArgument("id") { type = NavType.IntType },
                                    navArgument("title") { type = NavType.StringType},
                                    navArgument("image") { type = NavType.StringType}
                                )
                            ) {
                                val id = it.arguments?.getInt("id")
                                val title = it.arguments?.getString("title")
                                val image = it.arguments?.getString("image")
                                RecipePage(id, title, image, recipeViewModel)
                            }
                        }
                    }
                }
            }
        }
    }
}
