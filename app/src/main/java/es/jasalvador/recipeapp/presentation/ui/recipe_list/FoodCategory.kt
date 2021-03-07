package es.jasalvador.recipeapp.presentation.ui.recipe_list

import es.jasalvador.recipeapp.presentation.ui.recipe_list.FoodCategory.*

enum class FoodCategory(val value: String) {
    CHICKEN("Chicken"),
    BEEF("Beef"),
    SOUP("Soup"),
    DESSERT("Dessert"),
    VEGETARIAN("Vegetarian"),
    MILK("Milk"),
    VEGAN("Vegan"),
    PIZZA("Pizza"),
    DONUT("Donut"),
}

fun getAllFoodCategories(): List<FoodCategory> {
    return listOf(
        CHICKEN,
        BEEF,
        SOUP,
        DESSERT,
        VEGETARIAN,
        MILK,
        VEGAN,
        PIZZA,
        DONUT,
    )
}

fun getCoodCategory(value: String): FoodCategory? {
    val map = FoodCategory.values().associateBy(FoodCategory::value)
    return map[value]
}
