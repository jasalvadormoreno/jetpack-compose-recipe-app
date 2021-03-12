package es.jasalvador.recipeapp.cache

import androidx.room.Dao
import androidx.room.Insert
import es.jasalvador.recipeapp.cache.model.RecipeEntity

@Dao
interface RecipeDao {

    @Insert
    suspend fun insertRecipe(recipe: RecipeEntity): Long
}
