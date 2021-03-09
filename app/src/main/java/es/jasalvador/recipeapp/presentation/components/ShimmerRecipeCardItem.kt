package es.jasalvador.recipeapp.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ShimmerRecipeCardItem(
    colors: List<Color>,
    shimmerX: Float,
    shimmerY: Float,
    cardHeight: Dp,
    gradientWidth: Float,
    padding: Dp,
) {
    val transition = rememberInfiniteTransition()

    val animationX by transition.animateFloat(
        initialValue = 0f,
        targetValue = shimmerX,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1300,
                delayMillis = 300,
                easing = LinearEasing,
            )
        ),
    )
    val animationY by transition.animateFloat(
        initialValue = 0f,
        targetValue = shimmerY,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1300,
                delayMillis = 300,
                easing = LinearEasing,
            )
        ),
    )

    val brush = Brush.linearGradient(
        colors,
        start = Offset(animationX - gradientWidth, animationY - gradientWidth),
        end = Offset(animationX, animationY),
    )

    Column(modifier = Modifier.padding(padding)) {
        Surface(
            shape = MaterialTheme.shapes.medium,
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(cardHeight)
                    .background(brush = brush)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Surface(
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(cardHeight / 10)
                    .background(brush = brush)
            )
        }
    }
}
