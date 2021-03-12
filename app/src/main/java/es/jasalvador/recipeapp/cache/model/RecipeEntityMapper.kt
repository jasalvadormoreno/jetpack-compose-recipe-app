package es.jasalvador.recipeapp.cache.model

import es.jasalvador.recipeapp.domain.model.Recipe
import es.jasalvador.recipeapp.domain.util.DomainMapper
import es.jasalvador.recipeapp.util.DateUtils

class RecipeEntityMapper : DomainMapper<RecipeEntity, Recipe> {

    override fun mapToDomainModel(model: RecipeEntity): Recipe {
        return Recipe(
            id = model.id,
            title = model.title,
            publisher = model.publisher,
            featuredImage = model.featuredImage,
            rating = model.rating,
            sourceUrl = model.sourceUrl,
            ingredients = convertIngredientsToList(model.ingredients),
            dateAdded = DateUtils.longToDate(model.dateAdded),
            dateUpdated = DateUtils.longToDate(model.dateUpdated),
        )
    }

    override fun mapFromDomainModel(domainModel: Recipe): RecipeEntity {
        return RecipeEntity(
            id = domainModel.id,
            title = domainModel.title,
            publisher = domainModel.publisher,
            featuredImage = domainModel.featuredImage,
            rating = domainModel.rating,
            sourceUrl = domainModel.sourceUrl,
            ingredients = convertIngredientListToString(domainModel.ingredients),
            dateAdded = DateUtils.dateToLong(domainModel.dateAdded),
            dateUpdated = DateUtils.dateToLong(domainModel.dateUpdated),
            dateCached = DateUtils.dateToLong(DateUtils.createTimestamp())
        )
    }

    private fun convertIngredientListToString(ingredients: List<String>): String {
        return ingredients.joinToString(",")
    }

    private fun convertIngredientsToList(ingredientsString: String?): List<String> {
        return ingredientsString?.split(",") ?: emptyList()
    }
}
