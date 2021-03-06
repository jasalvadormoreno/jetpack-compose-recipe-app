package es.jasalvador.recipeapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import es.jasalvador.recipeapp.domain.model.Recipe
import es.jasalvador.recipeapp.network.model.RecipeNetworkMapper

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
