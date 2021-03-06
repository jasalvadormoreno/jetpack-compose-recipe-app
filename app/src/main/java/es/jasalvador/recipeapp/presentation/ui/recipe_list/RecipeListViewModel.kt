package es.jasalvador.recipeapp.presentation.ui.recipe_list

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import es.jasalvador.recipeapp.BaseApp
import javax.inject.Inject

@HiltViewModel
class RecipeListViewModel @Inject constructor(
    private val app: BaseApp
) : ViewModel() {

    init {
        println("VIEWMODEL: $app")
    }
}