package es.jasalvador.recipeapp.presentation.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun CircularIndeterminateProgressBar(isDisplayed: Boolean, verticalBias: Float) {
    if (isDisplayed) {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            val (progressBar) = createRefs()
            val topGuideline = createGuidelineFromTop(verticalBias)

            CircularProgressIndicator(
                modifier = Modifier
                    .constrainAs(progressBar) {
                        top.linkTo(topGuideline)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                color = MaterialTheme.colors.primary,
            )
        }
    }
}

@Preview
@Composable
fun CircularIndeterminateProgressBarPreview() {
    CircularIndeterminateProgressBar(isDisplayed = true, 0.3f)
}
