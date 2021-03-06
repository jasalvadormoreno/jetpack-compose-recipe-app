package es.jasalvador.recipeapp.network.responses

import com.google.gson.annotations.SerializedName
import es.jasalvador.recipeapp.network.model.RecipeDto

data class RecipeSearchResponse(
    @SerializedName("count")
    var count: Int,
    @SerializedName("results")
    var recipes: List<RecipeDto>
)