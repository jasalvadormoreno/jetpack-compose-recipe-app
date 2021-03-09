package es.jasalvador.recipeapp.presentation.ui.recipe_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Search
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
import es.jasalvador.recipeapp.BaseApp
import es.jasalvador.recipeapp.presentation.components.*
import es.jasalvador.recipeapp.presentation.theme.AppTheme
import javax.inject.Inject

@ExperimentalComposeUiApi
@AndroidEntryPoint
class RecipeListFragment : Fragment() {

    @Inject
    lateinit var application: BaseApp

    private val viewModel: RecipeListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme(darkTheme = application.isDark.value) {
                    val recipes = viewModel.recipes.value
                    val query = viewModel.query.value
                    val loading = viewModel.loading.value
                    val selectedCategory = viewModel.selectedCategory.value

                    Scaffold(
                        topBar = {
                            SearchAppBar(
                                query = query,
                                onQueryChanged = viewModel::onQueryChanged,
                                onExecuteSearch = viewModel::newSearch,
                                categoryScrollPositionItem = viewModel.categoryScrollPositionItem,
                                categoryScrollPositionOffset = viewModel.categoryScrollPositionOffset,
                                selectedCategory = selectedCategory,
                                onSelectedCategoryChanged = viewModel::onSelectedCategoryChanged,
                                onChangeCategoryScrollPosition = viewModel::onChangeCategoryScrollPosition,
                                onToggleTheme = application::toggleTheme
                            )
                        },
                        bottomBar = {
                            MyBottomBar()
                        },
                        drawerContent = {
                            MyDrawer()
                        },
                        content = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(MaterialTheme.colors.surface)
                            ) {
                                if (loading) {
                                    LoadingRecipeListShimmer(imageHeight = 250.dp)
                                } else {
                                    LazyColumn(
                                        content = {
                                            items(recipes) { recipe ->
                                                RecipeCard(recipe = recipe, onClick = {})
                                            }
                                        },
                                        contentPadding = PaddingValues(
                                            horizontal = 16.dp,
                                            vertical = 8.dp,
                                        )
                                    )
                                }
                                CircularIndeterminateProgressBar(
                                    isDisplayed = loading,
                                    verticalBias = 0.3f,
                                )
                            }
                        },
                    )
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

@Composable
fun MyBottomBar() {
    BottomNavigation(elevation = 12.dp) {
        BottomNavigationItem(
            icon = { Icon(Icons.Default.BrokenImage, contentDescription = null) },
            selected = false,
            onClick = { /*TODO*/ }
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Default.Search, contentDescription = null) },
            selected = true,
            onClick = { /*TODO*/ }
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Default.AccountBalanceWallet, contentDescription = null) },
            selected = false,
            onClick = { /*TODO*/ }
        )
    }
}

@Composable
fun MyDrawer() {
    Column {
        Text(text = "Text 1")
        Text(text = "Text 2")
        Text(text = "Text 3")
        Text(text = "Text 4")
        Text(text = "Text 5")
    }
}
