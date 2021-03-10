package es.jasalvador.recipeapp.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun LoadingRecipeShimmer(
    imageHeight: Dp,
    padding: Dp = 16.dp,
) {
    BoxWithConstraints {
        val shimmerX = with(LocalDensity.current) { (maxWidth - (padding * 2)).toPx() }
        val shimmerY = with(LocalDensity.current) { (imageHeight - padding).toPx() }

        val gradientWidth = 0.2f * shimmerX

        val colors = listOf(
            Color.LightGray.copy(alpha = .9f),
            Color.LightGray.copy(alpha = .3f),
            Color.LightGray.copy(alpha = .9f),
        )

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
                        .height(imageHeight)
                        .background(brush = brush)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                shape = MaterialTheme.shapes.medium,
            ) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(imageHeight / 10)
                        .background(brush = brush)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                shape = MaterialTheme.shapes.medium,
            ) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(imageHeight / 10)
                        .background(brush = brush)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                shape = MaterialTheme.shapes.medium,
            ) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(imageHeight / 10)
                        .background(brush = brush)
                )
            }
        }
    }
}
