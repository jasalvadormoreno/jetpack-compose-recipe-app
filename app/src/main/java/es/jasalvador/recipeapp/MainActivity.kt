package es.jasalvador.recipeapp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.HiltViewModelFactory
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import es.jasalvador.recipeapp.presentation.navigation.Screen
import es.jasalvador.recipeapp.presentation.ui.recipe.RecipeDetailScreen
import es.jasalvador.recipeapp.presentation.ui.recipe.RecipeViewModel
import es.jasalvador.recipeapp.presentation.ui.recipe_list.RecipeListScreen
import es.jasalvador.recipeapp.presentation.ui.recipe_list.RecipeListViewModel

@ExperimentalComposeUiApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = Screen.RecipeList.route) {
                composable(route = Screen.RecipeList.route) { navBackStackEntry ->
                    val factory = HiltViewModelFactory(LocalContext.current, navBackStackEntry)
                    val viewModel: RecipeListViewModel = viewModel("RecipeList", factory)
                    RecipeListScreen(
                        isDarkTheme = (application as BaseApp).isDark.value,
                        onToggleTheme = { (application as BaseApp)::toggleTheme },
                        viewModel = viewModel,
                    )
                }

                composable(Screen.RecipeDetail.route) { navBackStackEntry ->
                    val factory = HiltViewModelFactory(LocalContext.current, navBackStackEntry)
                    val viewModel: RecipeViewModel = viewModel("RecipeDetail", factory)
                    RecipeDetailScreen(
                        isDarkTheme = (application as BaseApp).isDark.value,
                        recipeId = 1,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}
