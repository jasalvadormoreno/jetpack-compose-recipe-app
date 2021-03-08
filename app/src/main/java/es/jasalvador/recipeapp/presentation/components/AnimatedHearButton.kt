package es.jasalvador.recipeapp.presentation.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import es.jasalvador.recipeapp.R
import es.jasalvador.recipeapp.util.loadPicture

enum class HeartButtonState {
    IDLE, ACTIVE
}

@Composable
fun HeartButton(
    modifier: Modifier,
    buttonState: MutableState<HeartButtonState>,
    onToggle: () -> Unit,
) {
    val currentState by remember { buttonState }
    val transition = updateTransition(currentState)

    val size by transition.animateDp(
        transitionSpec = {
            keyframes {
                durationMillis = 350
                40.dp at 0
                55.dp at 175
                40.dp at 350
            }
        },
    ) { 40.dp }

    val color by transition.animateColor { state ->
        when (state) {
            HeartButtonState.IDLE -> Color.LightGray
            HeartButtonState.ACTIVE -> Color.Red
        }
    }

    loadPicture(drawable = R.drawable.heart_red).value?.let { image ->
        Image(
            bitmap = image.asImageBitmap(),
            contentDescription = null,
            modifier = modifier
                .size(size)
                .clickable(
                    onClick = onToggle,
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                ),
            colorFilter = ColorFilter.tint(color)
        )
    }
}

