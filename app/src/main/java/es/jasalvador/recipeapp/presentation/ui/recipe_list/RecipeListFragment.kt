package es.jasalvador.recipeapp.presentation.ui.recipe_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.TextStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import es.jasalvador.recipeapp.BaseApp
import es.jasalvador.recipeapp.presentation.components.RecipeList
import es.jasalvador.recipeapp.presentation.components.SearchAppBar
import es.jasalvador.recipeapp.presentation.components.util.SnackbarController
import es.jasalvador.recipeapp.presentation.theme.AppTheme
import javax.inject.Inject

@ExperimentalComposeUiApi
@AndroidEntryPoint
class RecipeListFragment : Fragment() {

    @Inject
    lateinit var application: BaseApp

    private val snackbarController = SnackbarController(lifecycleScope)

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

                    val page = viewModel.page.value

                    val scaffoldState = rememberScaffoldState()

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
                                    if (viewModel.selectedCategory.value?.value == "Milk") {
                                        snackbarController.showSnackbar(
                                            scaffoldState = scaffoldState,
                                            message = "Invalid category: Milk",
                                            actionLabel = "Hide",
                                        )
                                    } else {
                                        viewModel.onTriggerEvent(RecipeListEvent.NewSearchEvent)
                                    }
                                },
                                categoryScrollPositionItem = viewModel.categoryScrollPositionItem,
                                categoryScrollPositionOffset = viewModel.categoryScrollPositionOffset,
                                selectedCategory = selectedCategory,
                                onSelectedCategoryChanged = viewModel::onSelectedCategoryChanged,
                                onChangeCategoryScrollPosition = viewModel::onChangeCategoryScrollPosition,
                                onToggleTheme = application::toggleTheme
                            )
                        },
                        content = {
                            RecipeList(
                                loading = loading,
                                recipes = recipes,
                                onChangeRecipeScrollPosition = viewModel::onChangeRecipeScrollPosition,
                                page = page,
                                onNextPage = {
                                    viewModel.onTriggerEvent(it)
                                },
                                scaffoldState = scaffoldState,
                                snackbarController = snackbarController,
                                navController = findNavController(),
                            )
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

