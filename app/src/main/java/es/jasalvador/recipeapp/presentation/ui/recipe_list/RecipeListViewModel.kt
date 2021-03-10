package es.jasalvador.recipeapp.presentation.ui.recipe_list

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.jasalvador.recipeapp.domain.model.Recipe
import es.jasalvador.recipeapp.repository.RecipeRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

const val PAGE_SIZE = 30

@HiltViewModel
class RecipeListViewModel @Inject constructor(
    private val repository: RecipeRepository,
    @Named("auth_token") private val token: String,
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
        onTriggerEvent(RecipeListEvent.NewSearchEvent)
    }

    fun onTriggerEvent(event: RecipeListEvent) {
        viewModelScope.launch {
            try {
                when (event) {
                    is RecipeListEvent.NewSearchEvent -> newSearch()
                    is RecipeListEvent.NextPageEvent -> nextPage()
                }
            } catch (e: Exception) {
                Log.e("RecipeListViewModel", "onTriggerEvent: Exception ${e}, ${e.cause}")
            }
        }
    }

    private suspend fun newSearch() {
        loading.value = true

        resetSearchState()

        delay(3000)

        val result = repository.search(
            token = token,
            page = 1,
            query = query.value,
        )
        recipes.value = result
        loading.value = false
    }

    private suspend fun nextPage() {
        if ((recipeListScrollPosition + 1) >= (page.value * PAGE_SIZE)) {
            loading.value = true
            incrementPage()

            delay(1000)

            if (page.value > 1) {
                val result = repository.search(
                    token = token,
                    page = page.value,
                    query = query.value,
                )
                appendRecipes(result)
            }
            loading.value = false
        }
    }

    private fun appendRecipes(recipes: List<Recipe>) {
        val current = this.recipes.value.toMutableList()
        current.addAll(recipes)
        this.recipes.value = current
    }

    private fun incrementPage() {
        page.value++
    }

    fun onChangeRecipeScrollPosition(position: Int) {
        recipeListScrollPosition = position
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
        selectedCategory.value = null
    }

    fun onQueryChanged(query: String) {
        this.query.value = query
    }

    fun onSelectedCategoryChanged(category: String) {
        val newCategory = getFoodCategory(category)
        selectedCategory.value = newCategory
        onQueryChanged(category)
    }

    fun onChangeCategoryScrollPosition(index: Int, offset: Int) {
        categoryScrollPositionItem = index
        categoryScrollPositionOffset = offset
    }
}