package es.jasalvador.recipeapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.jasalvador.recipeapp.network.RecipeService
import es.jasalvador.recipeapp.network.model.RecipeDtoMapper
import es.jasalvador.recipeapp.repository.RecipeRepository
import es.jasalvador.recipeapp.repository.RecipeRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideRepository(
        recipeService: RecipeService,
        recipeDtoMapper: RecipeDtoMapper
    ): RecipeRepository {
        return RecipeRepositoryImpl(recipeService, recipeDtoMapper)
    }
}
