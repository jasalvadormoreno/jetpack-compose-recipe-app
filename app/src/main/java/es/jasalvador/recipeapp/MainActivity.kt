package es.jasalvador.recipeapp

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.getSystemService
import androidx.hilt.navigation.HiltViewModelFactory
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import dagger.hilt.android.AndroidEntryPoint
import es.jasalvador.recipeapp.presentation.navigation.Screen
import es.jasalvador.recipeapp.presentation.ui.recipe.RecipeDetailScreen
import es.jasalvador.recipeapp.presentation.ui.recipe.RecipeDetailViewModel
import es.jasalvador.recipeapp.presentation.ui.recipe_list.RecipeListScreen
import es.jasalvador.recipeapp.presentation.ui.recipe_list.RecipeListViewModel
import es.jasalvador.recipeapp.util.TAG

@ExperimentalComposeUiApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var cm: ConnectivityManager
    val networkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .build()
    val networkCallback = object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            Log.d(TAG, "onAvailable: $network")
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            Log.d(TAG, "onLost: $network")
        }
    }

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
                        onToggleTheme = { (application as BaseApp).toggleTheme() },
                        onNavigateToRecipeDetailScreen = navController::navigate,
                        viewModel = viewModel,
                    )
                }

                composable(
                    route = "${Screen.RecipeDetail.route}/{recipeId}",
                    arguments = listOf(navArgument("recipeId") {
                        type = NavType.IntType
                    })
                ) { navBackStackEntry ->
                    val factory = HiltViewModelFactory(LocalContext.current, navBackStackEntry)
                    val viewModel: RecipeDetailViewModel = viewModel("RecipeDetail", factory)
                    RecipeDetailScreen(
                        isDarkTheme = (application as BaseApp).isDark.value,
                        recipeId = navBackStackEntry.arguments?.getInt("recipeId"),
                        viewModel = viewModel
                    )
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        cm = getSystemService()!!
        cm.registerNetworkCallback(networkRequest, networkCallback)
    }

    override fun onDestroy() {
        super.onDestroy()
        cm.unregisterNetworkCallback(networkCallback)
    }
}
