package es.jasalvador.recipeapp.presentation.ui.recipe_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import es.jasalvador.recipeapp.presentation.components.*

@ExperimentalComposeUiApi
@AndroidEntryPoint
class RecipeListFragment : Fragment() {

    private val viewModel: RecipeListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val recipes = viewModel.recipes.value
                val query = viewModel.query.value
                val loading = viewModel.loading.value
                val selectedCategory = viewModel.selectedCategory.value

                Column {
                    SearchAppBar(
                        query = query,
                        onQueryChanged = viewModel::onQueryChanged,
                        onExecuteSearch = viewModel::newSearch,
                        categoryScrollPositionItem = viewModel.categoryScrollPositionItem,
                        categoryScrollPositionOffset = viewModel.categoryScrollPositionOffset,
                        selectedCategory = selectedCategory,
                        onSelectedCategoryChanged = viewModel::onSelectedCategoryChanged,
                        onChangeCategoryScrollPosition = viewModel::onChangeCategoryScrollPosition
                    )

                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        if (loading) {
                            LoadingRecipeListShimmer(imageHeight = 250.dp)
                        } else {
                            LazyColumn(content = {
                                items(recipes) { recipe ->
                                    RecipeCard(recipe = recipe, onClick = {})
                                }
                            })
                        }
                        CircularIndeterminateProgressBar(isDisplayed = loading, verticalBias = 0.3f)
                    }
                }
            }
        }
    }
}

@Composable
fun GradientDemo() {
    val colors = listOf(
        Color.Blue,
        Color.Red,
        Color.Blue,
    )

    val brush = linearGradient(
        colors,
        start = Offset(200f, 200f),
        end = Offset(400f, 400f),
    )

    Surface(
        shape = MaterialTheme.shapes.small,
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = brush)
        )
    }
}
