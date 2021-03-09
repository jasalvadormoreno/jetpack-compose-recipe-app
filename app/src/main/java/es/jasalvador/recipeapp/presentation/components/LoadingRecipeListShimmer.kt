package es.jasalvador.recipeapp.presentation.components

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun LoadingRecipeListShimmer(
    imageHeight: Dp,
    padding: Dp = 16.dp,
) {
    BoxWithConstraints {
        val cardWidthPx = with(LocalDensity.current) { (maxWidth - (padding * 2)).toPx() }
        val cardHeightPx = with(LocalDensity.current) { (imageHeight - padding).toPx() }

        val width = 0.2f * cardHeightPx

        val colors = listOf(
            Color.LightGray.copy(alpha = .9f),
            Color.LightGray.copy(alpha = .3f),
            Color.LightGray.copy(alpha = .9f),
        )

        LazyColumn(content = {
            items(5) {
                ShimmerRecipeCardItem(
                    colors = colors,
                    shimmerX = cardWidthPx,
                    shimmerY = cardHeightPx,
                    cardHeight = imageHeight,
                    gradientWidth = width,
                    padding = padding,
                )
            }
        })
    }
}
