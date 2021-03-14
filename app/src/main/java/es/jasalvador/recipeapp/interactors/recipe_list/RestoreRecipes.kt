package es.jasalvador.recipeapp.interactors.recipe_list

import android.util.Log
import es.jasalvador.recipeapp.cache.RecipeDao
import es.jasalvador.recipeapp.cache.model.RecipeEntityMapper
import es.jasalvador.recipeapp.domain.data.DataState
import es.jasalvador.recipeapp.domain.model.Recipe
import es.jasalvador.recipeapp.util.TAG
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RestoreRecipes(
    private val recipeDao: RecipeDao,
    private val entityMapper: RecipeEntityMapper,
) {

    fun execute(page: Int, query: String): Flow<DataState<List<Recipe>>> = flow {
        try {
            emit(DataState.loading<List<Recipe>>())

            delay(1000)

            val cacheResults = if (query.isBlank()) {
                recipeDao.restoreAllRecipes(page)
            } else {
                recipeDao.restoreRecipes(query, page)
            }

            val list = entityMapper.toDomainList(cacheResults)
            emit(DataState.success(list))
        } catch (e: Exception) {
            Log.e(TAG, "execute: ${e.message}")
            emit(DataState.error<List<Recipe>>(e.message ?: "Unknown error"))
        }
    }
}
