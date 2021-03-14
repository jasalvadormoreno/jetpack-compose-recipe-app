package es.jasalvador.recipeapp.cache

import es.jasalvador.recipeapp.cache.model.RecipeEntity

class AppDatabaseFake {

    val recipes = mutableListOf<RecipeEntity>()
}