package es.jasalvador.recipeapp.network.responses

import com.google.gson.annotations.SerializedName
import es.jasalvador.recipeapp.network.model.RecipeNetworkEntity

class RecipeSearchResponse(
    @SerializedName("count")
    var count: Int,
    @SerializedName("results")
    var recipes: List<RecipeNetworkEntity>
)