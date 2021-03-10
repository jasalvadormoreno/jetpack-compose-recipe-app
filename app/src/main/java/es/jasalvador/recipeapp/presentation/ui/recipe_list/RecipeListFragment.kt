package es.jasalvador.recipeapp.presentation.ui.recipe_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import es.jasalvador.recipeapp.BaseApp
import es.jasalvador.recipeapp.presentation.components.CircularIndeterminateProgressBar
import es.jasalvador.recipeapp.presentation.components.LoadingRecipeListShimmer
import es.jasalvador.recipeapp.presentation.components.RecipeCard
import es.jasalvador.recipeapp.presentation.components.SearchAppBar
import es.jasalvador.recipeapp.presentation.theme.AppTheme
import kotlinx.coroutines.launch
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

//                val isShowing = remember { mutableStateOf(false) }
                val snackbarHostState = remember { SnackbarHostState() }

                Column {
                    Button(onClick = {
                        lifecycleScope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Hey look a snackbar",
                                actionLabel = "Hide",
                                duration = SnackbarDuration.Short,
                            )
                        }
                    }) {
                        Text(text = "Show snackbar")
                    }
                    DecoupledSnackbarDemo(snackbarHostState = snackbarHostState)
//                    SnackbarDemo(
//                        isShowing = isShowing.value,
//                        onHideSnackbar = {
//                            isShowing.value = isShowing.value.not()
//                        },
//                    )
                }

                return@setContent
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
fun DecoupledSnackbarDemo(
    snackbarHostState: SnackbarHostState,
) {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val snackbar = createRef()
        SnackbarHost(
            modifier = Modifier
                .constrainAs(snackbar) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            hostState = snackbarHostState,
            snackbar = {
                Snackbar(
                    action = {
                        TextButton(
                            onClick = {
                                snackbarHostState.currentSnackbarData?.dismiss()
                            },
                        ) {
                            Text(
                                text = snackbarHostState.currentSnackbarData?.actionLabel ?: "",
                                style = TextStyle(Color.White),
                            )
                        }
                    },
                ) {
                    Text(text = snackbarHostState.currentSnackbarData?.message ?: "")
                }
            }
        )
    }
}

@Composable
fun SnackbarDemo(
    isShowing: Boolean,
    onHideSnackbar: () -> Unit,
) {
    if (isShowing) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val snackbar = createRef()
            Snackbar(
                modifier = Modifier
                    .constrainAs(snackbar) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                action = {
                    Text(
                        text = "Hide",
                        modifier = Modifier
                            .clickable(onClick = onHideSnackbar),
                        style = MaterialTheme.typography.button,
                        color = MaterialTheme.colors.primary,
                    )
                },
            ) {
                Text(text = "Hey look a Snackbar")
            }
        }
    }
}

