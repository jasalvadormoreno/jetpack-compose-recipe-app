package es.jasalvador.recipeapp.presentation.ui.recipe

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.jasalvador.recipeapp.domain.model.Recipe
import es.jasalvador.recipeapp.interactors.recipe.GetRecipe
import es.jasalvador.recipeapp.presentation.ui.recipe.RecipeDetailEvent.GetRecipeDetailEvent
import es.jasalvador.recipeapp.util.TAG
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

const val STATE_KEY_RECIPE = "state.key.recipeId"

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    private val getRecipe: GetRecipe,
    @Named("auth_token") private val token: String,
    private val state: SavedStateHandle,
) : ViewModel() {

    val recipe: MutableState<Recipe?> = mutableStateOf(null)
    val loading = mutableStateOf(false)
    val onLoad: MutableState<Boolean> = mutableStateOf(false)

    init {
        state.get<Int>(STATE_KEY_RECIPE)?.let { recipeId ->
            onTriggerEvent(GetRecipeDetailEvent(recipeId))
        }
    }

    fun onTriggerEvent(event: RecipeDetailEvent) {
        viewModelScope.launch {
            try {
                when (event) {
                    is GetRecipeDetailEvent -> {
                        if (recipe.value == null) {
                            getRecipe(event.id)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "onTriggerEvent: Exception ${e}, ${e.cause}")
            }
        }
    }

    private fun getRecipe(id: Int) {
        getRecipe.execute(token, id).onEach { dataState ->
            loading.value = dataState.loading

            dataState.data?.let {
                this.recipe.value = it
                state.set(STATE_KEY_RECIPE, id)
            }
            dataState.error?.let {
                Log.d(TAG, "getRecipe: $it")
            }
        }.launchIn(viewModelScope)
    }
}
