package es.jasalvador.recipeapp.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import es.jasalvador.recipeapp.presentation.ui.recipe_list.FoodCategory
import es.jasalvador.recipeapp.presentation.ui.recipe_list.getAllFoodCategories
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
@Composable
fun SearchAppBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    onExecuteSearch: () -> Unit,
    categoryScrollPositionItem: Int,
    categoryScrollPositionOffset: Int,
    selectedCategory: FoodCategory?,
    onSelectedCategoryChanged: (String) -> Unit,
    onChangeCategoryScrollPosition: (Int, Int) -> Unit,
    onToggleTheme: () -> Unit,
) {
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
                    onValueChange = { onQueryChanged(it) },
                    label = {
                        Text(text = "Search")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Search,
                    ),
                    keyboardActions = KeyboardActions(onSearch = {
                        onExecuteSearch()
                        keyboardController?.hideSoftwareKeyboard()
                    }),
                    leadingIcon = { Icon(Icons.Filled.Search, "") },
                    textStyle = MaterialTheme.typography.button,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        backgroundColor = MaterialTheme.colors.surface
                    ),
                )

                ConstraintLayout(
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    val menu = createRef()
                    IconButton(
                        onClick = { onToggleTheme() },
                        modifier = Modifier.constrainAs(menu) {
                            end.linkTo(parent.end)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        },
                    ) {
                        Icon(imageVector = Icons.Filled.MoreVert, contentDescription = null)
                    }
                }
            }

            val state = rememberLazyListState()
            val scope = rememberCoroutineScope()
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                state = state,
                content = {
                    scope.launch {
                        state.scrollToItem(
                            categoryScrollPositionItem,
                            categoryScrollPositionOffset
                        )
                    }
                    items(getAllFoodCategories()) { category ->
                        FoodCategoryChip(
                            category = category.value,
                            isSelected = selectedCategory == category,
                            onSelectedCategoryChanged = {
                                onSelectedCategoryChanged(it)
                                onChangeCategoryScrollPosition(
                                    state.firstVisibleItemIndex,
                                    state.firstVisibleItemScrollOffset
                                )
                            },
                            onExecuteSearch = { onExecuteSearch() }
                        )
                    }
                })
        }
    }
}
