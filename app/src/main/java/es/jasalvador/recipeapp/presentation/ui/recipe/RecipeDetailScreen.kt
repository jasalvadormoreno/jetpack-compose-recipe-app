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
import es.jasalvador.recipeapp.presentation.ui.recipe.RecipeDetailEvent.GetRecipeDetailEvent

@Composable
fun RecipeDetailScreen(
    isDarkTheme: Boolean,
    recipeId: Int?,
    viewModel: RecipeDetailViewModel,
) {
    if (recipeId == null) {
        TODO("Show Invalid Recipe")
    } else {
        val onLoad = viewModel.onLoad.value
        if (!onLoad) {
            viewModel.onLoad.value = true
            viewModel.onTriggerEvent(GetRecipeDetailEvent(recipeId))
        }

        val loading = viewModel.loading.value
        val recipe = viewModel.recipe.value
        val scaffoldState = rememberScaffoldState()
        val dialogQueue = viewModel.dialogQueue

        AppTheme(
            darkTheme = isDarkTheme,
            displayProgressBar = loading,
            scaffoldState = scaffoldState,
            dialogQueue = dialogQueue.queue.value
        ) {
            Scaffold(
                scaffoldState = scaffoldState,
                snackbarHost = {
                    scaffoldState.snackbarHostState
                }
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    if (loading && recipe == null) {
                        LoadingRecipeShimmer(imageHeight = IMAGE_HEIGHT.dp)
                    } else {
                        recipe?.let { RecipeView(recipe = it) }
                    }
                }
            }
        }
    }
}
