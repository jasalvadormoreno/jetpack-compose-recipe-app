package es.jasalvador.recipeapp.interactors.recipe_list

import es.jasalvador.recipeapp.cache.RecipeDao
import es.jasalvador.recipeapp.cache.model.RecipeEntityMapper
import es.jasalvador.recipeapp.domain.data.DataState
import es.jasalvador.recipeapp.domain.model.Recipe
import es.jasalvador.recipeapp.network.RecipeService
import es.jasalvador.recipeapp.network.model.RecipeDtoMapper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchRecipes(
    private val recipeService: RecipeService,
    private val recipeDao: RecipeDao,
    private val dtoMapper: RecipeDtoMapper,
    private val entityMapper: RecipeEntityMapper,
) {

    fun execute(
        token: String,
        page: Int,
        query: String,
    ): Flow<DataState<List<Recipe>>> = flow {
        try {
            emit(DataState.loading<List<Recipe>>())

            delay(1000)

            if (query == "error") throw Exception("Search failed")

            val recipes = getRecipesFromNetwork(token, page, query)

            recipeDao.insertRecipes(entityMapper.fromDomainList(recipes))

            val cacheResult = if (query.isBlank()) {
                recipeDao.getAllRecipes(page = page)
            } else {
                recipeDao.searchRecipes(
                    query = query,
                    page = page,
                )
            }

            val list = entityMapper.toDomainList(cacheResult)

            emit(DataState.success(list))
        } catch (e: Exception) {
            emit(DataState.error<List<Recipe>>(e.message ?: "Unknown error"))
        }
    }

    private suspend fun getRecipesFromNetwork(
        token: String,
        page: Int,
        query: String,
    ): List<Recipe> {
        return dtoMapper.toDomainList(recipeService.search(token, page, query).recipes)
    }
}
