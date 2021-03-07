package es.jasalvador.recipeapp.presentation.ui.recipe_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import es.jasalvador.recipeapp.presentation.components.FoodCategoryChip
import es.jasalvador.recipeapp.presentation.components.RecipeCard

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
                val selectedCategory = viewModel.selectedCategory.value

                Column {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth(),
                        color = MaterialTheme.colors.surface,
                        elevation = 8.dp,
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                val keyboardController = LocalSoftwareKeyboardController.current
                                TextField(
                                    modifier = Modifier
                                        .fillMaxWidth(0.9f)
                                        .padding(8.dp),
                                    value = query,
                                    onValueChange = { viewModel.onQueryChanged(it) },
                                    label = {
                                        Text(text = "Search")
                                    },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Text,
                                        imeAction = ImeAction.Search,
                                    ),
                                    keyboardActions = KeyboardActions(onSearch = {
                                        viewModel.newSearch()
                                        keyboardController?.hideSoftwareKeyboard()
                                    }),
                                    leadingIcon = { Icon(Icons.Filled.Search, "") },
                                    textStyle = TextStyle(color = MaterialTheme.colors.onSurface),
                                    colors = TextFieldDefaults.outlinedTextFieldColors(
                                        backgroundColor = MaterialTheme.colors.surface
                                    ),
                                )
                            }

                            LazyRow(
                                modifier = Modifier.fillMaxWidth(),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                                content = {
                                    items(getAllFoodCategories()) { category ->
                                        FoodCategoryChip(
                                            category = category.value,
                                            isSelected = selectedCategory == category,
                                            onSelectedCategoryChanged = {
                                                viewModel.onSelectedCategoryChanged(it)
                                            },
                                            onExecuteSearch = viewModel::newSearch
                                        )
                                    }
                                })
                        }
                    }

                    LazyColumn(content = {
                        items(recipes) { recipe ->
                            RecipeCard(recipe = recipe, onClick = {})
                        }
                    })
                }
            }
        }
    }
}