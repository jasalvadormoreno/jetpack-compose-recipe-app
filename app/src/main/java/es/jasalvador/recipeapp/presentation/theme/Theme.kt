package es.jasalvador.recipeapp.presentation.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import es.jasalvador.recipeapp.presentation.components.*

private val LightThemeColors = lightColors(
    primary = Blue600,
    primaryVariant = Blue400,
    onPrimary = Black2,
    secondary = Color.White,
    secondaryVariant = Teal300,
    onSecondary = Color.Black,
    error = RedErrorDark,
    onError = RedErrorLight,
    background = Grey1,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Black2,
)

private val DarkThemeColors = darkColors(
    primary = Blue700,
    primaryVariant = Color.White,
    onPrimary = Color.White,
    secondary = Black1,
    onSecondary = Color.White,
    error = RedErrorLight,
    background = Color.Black,
    onBackground = Color.White,
    surface = Black1,
    onSurface = Color.White,
)

@Composable
fun AppTheme(
    darkTheme: Boolean,
    displayProgressBar: Boolean,
    scaffoldState: ScaffoldState,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colors = if (darkTheme) DarkThemeColors else LightThemeColors,
        typography = QuickSandTypography,
        shapes = AppShapes,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = if (!darkTheme) Grey1 else Color.Black),
        ) {
            content()

            CircularIndeterminateProgressBar(
                isDisplayed = displayProgressBar,
                verticalBias = 0.3f,
            )
            DefaultSnackbar(
                snackbarHostState = scaffoldState.snackbarHostState,
                onDismiss = {
                    scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                },
                modifier = Modifier.align(Alignment.BottomCenter),
            )

            val isShowing = remember { mutableStateOf(true) }
            if (isShowing.value) {
                val dialogInfo = GenericDialogInfo.Builder()
                    .title("Error")
                    .onDismiss { isShowing.value = false }
                    .message("Hey look a dialog description.")
                    .positiveAction(PositiveAction("Ok") {
                        isShowing.value = false
                    })
                    .dismissAction(DismissAction("Cancel") {
                        isShowing.value = false
                    })
                    .build()

                GenericDialog(
                    onDismiss = dialogInfo.onDismiss,
                    title = dialogInfo.title,
                    message = dialogInfo.message,
                    positiveAction = dialogInfo.positiveAction,
                    dismissAction = dialogInfo.dismissAction,
                )
            }
        }
    }
}
