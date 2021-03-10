package es.jasalvador.recipeapp.presentation.components

import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import es.jasalvador.recipeapp.R
import es.jasalvador.recipeapp.domain.model.Recipe
import es.jasalvador.recipeapp.presentation.components.util.SnackbarController
import es.jasalvador.recipeapp.presentation.ui.recipe_list.PAGE_SIZE
import es.jasalvador.recipeapp.presentation.ui.recipe_list.RecipeListEvent

@Composable
fun RecipeList(
    loading: Boolean,
    recipes: List<Recipe>,
    onChangeRecipeScrollPosition: (Int) -> Unit,
    page: Int,
    onNextPage: (RecipeListEvent) -> Unit,
    scaffoldState: ScaffoldState,
    snackbarController: SnackbarController,
    navController: NavController,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
    ) {
        if (loading && recipes.isEmpty()) {
            LoadingRecipeListShimmer(imageHeight = 250.dp)
        } else {
            LazyColumn(
                content = {
                    itemsIndexed(recipes) { index, recipe ->
                        onChangeRecipeScrollPosition(index)
                        if ((index + 1) >= (page * PAGE_SIZE) && !loading) {
                            onNextPage(RecipeListEvent.NextPageEvent)
                        }
                        RecipeCard(recipe = recipe, onClick = {
                            if (recipe.id != null) {
                                val bundle = Bundle()
                                bundle.putInt("recipeId", recipe.id)
                                navController.navigate(R.id.to_recipe, bundle)
                            } else {
                                snackbarController.showSnackbar(
                                    scaffoldState = scaffoldState,
                                    message = "Recipe Error",
                                    actionLabel = "Ok",
                                )
                            }
                        })
                    }
                },
                contentPadding = PaddingValues(
                    horizontal = 16.dp,
                    vertical = 8.dp,
                )
            )
        }
    }
}
