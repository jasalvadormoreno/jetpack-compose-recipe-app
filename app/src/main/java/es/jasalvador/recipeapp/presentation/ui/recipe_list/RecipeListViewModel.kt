package es.jasalvador.recipeapp.presentation.ui.recipe_list

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.jasalvador.recipeapp.domain.model.Recipe
import es.jasalvador.recipeapp.interactors.recipe_list.RestoreRecipes
import es.jasalvador.recipeapp.interactors.recipe_list.SearchRecipes
import es.jasalvador.recipeapp.util.TAG
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

const val PAGE_SIZE = 30

const val STATE_KEY_PAGE = "recipe.state.page.key"
const val STATE_KEY_QUERY = "recipe.state.query.key"
const val STATE_KEY_LIST_POSITION = "recipe.state.query.list_position"
const val STATE_KEY_SELECTED_CATEGORY = "recipe.state.query.selected_category"

@HiltViewModel
class RecipeListViewModel @Inject constructor(
    private val searchRecipes: SearchRecipes,
    private val restoreRecipes: RestoreRecipes,
    @Named("auth_token") private val token: String,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val recipes: MutableState<List<Recipe>> = mutableStateOf(emptyList())
    val query = mutableStateOf("")
    val selectedCategory: MutableState<FoodCategory?> = mutableStateOf(null)
    val loading = mutableStateOf(false)

    var categoryScrollPositionItem: Int = 0
    var categoryScrollPositionOffset: Int = 0

    val page = mutableStateOf(1)
    private var recipeListScrollPosition = 0

    init {
        savedStateHandle.get<Int>(STATE_KEY_PAGE)?.let { p -> setPage(p) }
        savedStateHandle.get<String>(STATE_KEY_QUERY)?.let { q -> setQuery(q) }
        savedStateHandle.get<Int>(STATE_KEY_LIST_POSITION)?.let { p -> setListScrollPosition(p) }
        savedStateHandle.get<FoodCategory>(STATE_KEY_SELECTED_CATEGORY)?.let { c ->
            setSelectedCategory(c)
        }

        if (recipeListScrollPosition != 0) {
            onTriggerEvent(RecipeListEvent.RestoreStateEvent)
        } else {
            onTriggerEvent(RecipeListEvent.NewSearchEvent)
        }
    }

    fun onTriggerEvent(event: RecipeListEvent) {
        viewModelScope.launch {
            try {
                when (event) {
                    is RecipeListEvent.NewSearchEvent -> newSearch()
                    is RecipeListEvent.NextPageEvent -> nextPage()
                    is RecipeListEvent.RestoreStateEvent -> restoreState()
                }
            } catch (e: Exception) {
                Log.e(TAG, "onTriggerEvent: Exception ${e}, ${e.cause}")
            }
        }
    }

    private fun restoreState() {
        restoreRecipes.execute(page.value, query.value).onEach { dataState ->
            loading.value = dataState.loading

            dataState.data?.let { recipes.value = it }
            dataState.error?.let { error ->
                Log.e(TAG, "restoreState: $error")
            }
        }.launchIn(viewModelScope)
    }

    private fun newSearch() {
        resetSearchState()

        searchRecipes.execute(
            token = token,
            page = page.value,
            query = query.value,
        ).onEach { dataState ->
            loading.value = dataState.loading

            dataState.data?.let { recipes.value = it }
            dataState.error?.let { error ->
                Log.e(TAG, "newSearch: $error")
            }
        }.launchIn(viewModelScope)
    }

    private fun nextPage() {
        if ((recipeListScrollPosition + 1) >= (page.value * PAGE_SIZE)) {
            incrementPage()
            if (page.value > 1) {
                searchRecipes.execute(
                    token = token,
                    page = page.value,
                    query = query.value,
                ).onEach { dataState ->
                    loading.value = dataState.loading

                    dataState.data?.let { appendRecipes(it) }
                    dataState.error?.let { error ->
                        Log.e(TAG, "nextPage: $error")
                    }
                }.launchIn(viewModelScope)
            }
        }
    }

    private fun appendRecipes(recipes: List<Recipe>) {
        val current = this.recipes.value.toMutableList()
        current.addAll(recipes)
        this.recipes.value = current
    }

    private fun incrementPage() {
        setPage(page.value + 1)
    }

    fun onChangeRecipeScrollPosition(position: Int) {
        setListScrollPosition(position)
    }

    private fun resetSearchState() {
        recipes.value = listOf()
        page.value = 1
        onChangeRecipeScrollPosition(0)
        if (selectedCategory.value?.value != query.value) {
            clearSelectedCategory()
        }
    }

    private fun clearSelectedCategory() {
        setSelectedCategory(null)
    }

    fun onQueryChanged(query: String) {
        setQuery(query)
    }

    fun onSelectedCategoryChanged(category: String) {
        val newCategory = getFoodCategory(category)
        setSelectedCategory(newCategory)
        onQueryChanged(category)
    }

    fun onChangeCategoryScrollPosition(index: Int, offset: Int) {
        categoryScrollPositionItem = index
        categoryScrollPositionOffset = offset
    }

    private fun setListScrollPosition(position: Int) {
        recipeListScrollPosition = position
        savedStateHandle.set(STATE_KEY_LIST_POSITION, position)
    }

    private fun setPage(page: Int) {
        this.page.value = page
        savedStateHandle.set(STATE_KEY_PAGE, page)
    }

    private fun setSelectedCategory(category: FoodCategory?) {
        selectedCategory.value = category
        savedStateHandle.set(STATE_KEY_SELECTED_CATEGORY, category)
    }

    private fun setQuery(query: String) {
        this.query.value = query
        savedStateHandle.set(STATE_KEY_QUERY, query)
    }
}