package es.jasalvador.recipeapp.presentation.ui.recipe_list

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

    init {
        newSearch()
    }

    fun newSearch() {
        viewModelScope.launch {
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
    }

    private fun resetSearchState() {
        recipes.value = listOf()
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