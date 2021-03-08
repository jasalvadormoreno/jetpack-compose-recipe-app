package es.jasalvador.recipeapp.presentation.ui.recipe_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import es.jasalvador.recipeapp.presentation.components.HeartButton
import es.jasalvador.recipeapp.presentation.components.HeartButtonState
import es.jasalvador.recipeapp.presentation.components.PulsingDemo
import es.jasalvador.recipeapp.presentation.components.SearchAppBar

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
                    PulsingDemo()

                    val currentState = remember { mutableStateOf(HeartButtonState.IDLE) }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        HeartButton(modifier = Modifier, buttonState = currentState, onToggle = {
                            currentState.value = if (currentState.value == HeartButtonState.ACTIVE)
                                HeartButtonState.IDLE
                            else
                                HeartButtonState.ACTIVE
                        })
                    }
//                    Box(
//                        modifier = Modifier.fillMaxSize()
//                    ) {
//                        LazyColumn(content = {
//                            items(recipes) { recipe ->
//                                RecipeCard(recipe = recipe, onClick = {})
//                            }
//                        })
//
//                        CircularIndeterminateProgressBar(isDisplayed = loading, verticalBias = 0.3f)
//                    }
                }
            }
        }
    }
}