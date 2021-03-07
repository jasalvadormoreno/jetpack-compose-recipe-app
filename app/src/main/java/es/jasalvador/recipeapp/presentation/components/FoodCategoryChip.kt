package es.jasalvador.recipeapp.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import es.jasalvador.recipeapp.presentation.ui.recipe_list.FoodCategory

@Composable
fun FoodCategoryChip(category: String, onExecuteSearch: (String) -> Unit) {
    Surface(
        modifier = Modifier.padding(end = 8.dp),
        elevation = 8.dp,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colors.primary,
    ) {
        Row(
            modifier = Modifier.clickable { onExecuteSearch(category) }
        ) {
            Text(
                text = category,
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onPrimary,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Preview
@Composable
fun FoodCategoryChipPreview() {
    FoodCategoryChip(category = FoodCategory.PIZZA.value, onExecuteSearch = { /*TODO*/ })
}
