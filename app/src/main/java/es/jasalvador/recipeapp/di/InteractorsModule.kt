package es.jasalvador.recipeapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import es.jasalvador.recipeapp.cache.RecipeDao
import es.jasalvador.recipeapp.cache.model.RecipeEntityMapper
import es.jasalvador.recipeapp.interactors.recipe.GetRecipe
import es.jasalvador.recipeapp.interactors.recipe_list.RestoreRecipes
import es.jasalvador.recipeapp.interactors.recipe_list.SearchRecipes
import es.jasalvador.recipeapp.network.RecipeService
import es.jasalvador.recipeapp.network.model.RecipeDtoMapper

@Module
@InstallIn(ViewModelComponent::class)
object InteractorsModule {

    @ViewModelScoped
    @Provides
    fun provideSearchRecipes(
        recipeService: RecipeService,
        recipeDao: RecipeDao,
        dtoMapper: RecipeDtoMapper,
        entityMapper: RecipeEntityMapper,
    ): SearchRecipes {
        return SearchRecipes(recipeService, recipeDao, dtoMapper, entityMapper)
    }

    @ViewModelScoped
    @Provides
    fun provideRestoreRecipes(
        recipeDao: RecipeDao,
        entityMapper: RecipeEntityMapper,
    ): RestoreRecipes {
        return RestoreRecipes(recipeDao, entityMapper)
    }

    @ViewModelScoped
    @Provides
    fun provideGetRecipe(
        recipeService: RecipeService,
        recipeDao: RecipeDao,
        dtoMapper: RecipeDtoMapper,
        entityMapper: RecipeEntityMapper,
    ): GetRecipe {
        return GetRecipe(recipeService, recipeDao, dtoMapper, entityMapper)
    }
}
