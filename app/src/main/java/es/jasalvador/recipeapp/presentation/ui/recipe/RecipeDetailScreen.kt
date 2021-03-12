package es.jasalvador.recipeapp.presentation.ui.recipe

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import es.jasalvador.recipeapp.presentation.components.IMAGE_HEIGHT
import es.jasalvador.recipeapp.presentation.components.LoadingRecipeShimmer
import es.jasalvador.recipeapp.presentation.components.RecipeView
import es.jasalvador.recipeapp.presentation.theme.AppTheme

@Composable
fun RecipeDetailScreen(
    isDarkTheme: Boolean,
    recipeId: Int?,
    viewModel: RecipeViewModel,
) {
//    val loading = viewModel.loading.value
//    val recipe = viewModel.recipe.value
//
//    val scaffoldState = rememberScaffoldState()
//
//    AppTheme(
//        darkTheme = isDarkTheme,
//        displayProgressBar = loading,
//        scaffoldState = scaffoldState,
//    ) {
//        Scaffold(
//            scaffoldState = scaffoldState,
//            snackbarHost = {
//                scaffoldState.snackbarHostState
//            }
//        ) {
//            Box(modifier = Modifier.fillMaxSize()) {
//                if (loading && recipe == null) {
//                    LoadingRecipeShimmer(imageHeight = IMAGE_HEIGHT.dp)
//                } else {
//                    recipe?.let {
//                        if (recipe.id == 1) {
//                            snackbarController.showSnackbar(
//                                scaffoldState = scaffoldState,
//                                message = "An error occurred with this recipe.",
//                                actionLabel = "Ok",
//                            )
//                        } else {
//                            RecipeView(recipe = it)
//                        }
//                    }
//                }
//            }
//        }
//    }
}
