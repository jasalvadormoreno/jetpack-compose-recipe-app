package es.jasalvador.recipeapp.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun PulsingDemo() {
    val pulseMagnitude by rememberInfiniteTransition().animateValue(
        initialValue = 40.dp,
        targetValue = 60.dp,
        typeConverter = Dp.VectorConverter,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 500,
                easing = LinearEasing
            ),
        )
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        Image(
            imageVector = Icons.Default.Favorite,
            contentDescription = "",
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .height(pulseMagnitude)
                .width(pulseMagnitude),
        )
    }

    val color = MaterialTheme.colors.primary
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp),
        onDraw = {
            drawCircle(
                radius = pulseMagnitude.value,
                brush = SolidColor(color),
            )
        })
}

@Preview
@Composable
fun PulsingDemoPreview() {
    PulsingDemo()
}
