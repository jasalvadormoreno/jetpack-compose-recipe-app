package es.jasalvador.recipeapp.cache.database

import androidx.room.Database
import androidx.room.RoomDatabase
import es.jasalvador.recipeapp.cache.RecipeDao
import es.jasalvador.recipeapp.cache.model.RecipeEntity

@Database(
    entities = [RecipeEntity::class],
    version = 1,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun recipeDao(): RecipeDao

    companion object {
        val DATABASE_NAME = "recipe_db"
    }
}
