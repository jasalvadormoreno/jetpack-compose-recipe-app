package es.jasalvador.recipeapp.interactors.recipe

import android.util.Log
import es.jasalvador.recipeapp.cache.RecipeDao
import es.jasalvador.recipeapp.cache.model.RecipeEntityMapper
import es.jasalvador.recipeapp.domain.data.DataState
import es.jasalvador.recipeapp.domain.model.Recipe
import es.jasalvador.recipeapp.network.RecipeService
import es.jasalvador.recipeapp.network.model.RecipeDtoMapper
import es.jasalvador.recipeapp.util.TAG
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetRecipe(
    private val recipeService: RecipeService,
    private val recipeDao: RecipeDao,
    private val dtoMapper: RecipeDtoMapper,
    private val entityMapper: RecipeEntityMapper,
) {

    fun execute(
        token: String,
        recipeId: Int,
        isNetworkAvailable: Boolean,
    ): Flow<DataState<Recipe>> = flow {
        try {
            emit(DataState.loading<Recipe>())

            delay(1000)

            var recipe = getRecipeFromCache(recipeId)

            if (recipe == null) {
                if (isNetworkAvailable) {
                    val networkRecipe = getRecipeFromNetwork(token, recipeId)
                    recipeDao.insertRecipe(entityMapper.mapFromDomainModel(networkRecipe))
                }

                recipe = getRecipeFromCache(recipeId)
                    ?: throw Exception("Unable to fetch recipe from cache.")
            }

            emit(DataState.success(recipe))
        } catch (e: Exception) {
            Log.e(TAG, "execute: ${e.message}")
            emit(DataState.error<Recipe>(e.message ?: "Unknown error"))
        }
    }

    private suspend fun getRecipeFromCache(recipeId: Int): Recipe? {
        return recipeDao.getRecipeById(recipeId)?.let { recipeEntity ->
            entityMapper.mapToDomainModel(recipeEntity)
        }
    }

    private suspend fun getRecipeFromNetwork(token: String, recipeId: Int): Recipe {
        return dtoMapper.mapToDomainModel(recipeService.get(token, recipeId))
    }
}
