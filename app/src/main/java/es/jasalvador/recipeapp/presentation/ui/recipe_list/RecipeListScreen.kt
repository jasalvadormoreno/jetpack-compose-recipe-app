package es.jasalvador.recipeapp.presentation.ui.recipe_list

import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import es.jasalvador.recipeapp.presentation.components.RecipeList
import es.jasalvador.recipeapp.presentation.components.SearchAppBar
import es.jasalvador.recipeapp.presentation.theme.AppTheme

@ExperimentalComposeUiApi
@Composable
fun RecipeListScreen(
    isDarkTheme: Boolean,
    isNetworkAvailable: Boolean,
    onToggleTheme: () -> Unit,
    onNavigateToRecipeDetailScreen: (String) -> Unit,
    viewModel: RecipeListViewModel,
) {
    val recipes = viewModel.recipes.value
    val query = viewModel.query.value
    val loading = viewModel.loading.value
    val selectedCategory = viewModel.selectedCategory.value
    val page = viewModel.page.value
    val scaffoldState = rememberScaffoldState()
    val dialogQueue = viewModel.dialogQueue

    AppTheme(
        darkTheme = isDarkTheme,
        isNetworkAvailable = isNetworkAvailable,
        displayProgressBar = loading,
        scaffoldState = scaffoldState,
        dialogQueue = dialogQueue.queue.value,
    ) {
        Scaffold(
            scaffoldState = scaffoldState,
            snackbarHost = {
                scaffoldState.snackbarHostState
            },
            topBar = {
                SearchAppBar(
                    query = query,
                    onQueryChanged = viewModel::onQueryChanged,
                    onExecuteSearch = {
                        viewModel.onTriggerEvent(RecipeListEvent.NewSearchEvent)
                    },
                    categoryScrollPositionItem = viewModel.categoryScrollPositionItem,
                    categoryScrollPositionOffset = viewModel.categoryScrollPositionOffset,
                    selectedCategory = selectedCategory,
                    onSelectedCategoryChanged = viewModel::onSelectedCategoryChanged,
                    onChangeCategoryScrollPosition = viewModel::onChangeCategoryScrollPosition,
                    onToggleTheme = { onToggleTheme() }
                )
            },
            content = {
                RecipeList(
                    loading = loading,
                    recipes = recipes,
                    onChangeScrollPosition = viewModel::onChangeRecipeScrollPosition,
                    page = page,
                    onNextPage = {
                        viewModel.onTriggerEvent(it)
                    },
                    onNavigateToRecipeDetailScreen = onNavigateToRecipeDetailScreen,
                )
            },
        )
    }
}
