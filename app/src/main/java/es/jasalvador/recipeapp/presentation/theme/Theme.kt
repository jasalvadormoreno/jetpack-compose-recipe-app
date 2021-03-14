package es.jasalvador.recipeapp.presentation.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import es.jasalvador.recipeapp.presentation.components.CircularIndeterminateProgressBar
import es.jasalvador.recipeapp.presentation.components.DefaultSnackbar

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
                AlertDialog(
                    onDismissRequest = { isShowing.value = false },
                    title = { Text(text = "Dialog Title") },
                    text = { Text(text = "Dialog Text") },
                    confirmButton = {
                        TextButton(
                            onClick = { isShowing.value = false },
                        ) {
                            Text(text = "Ok")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { isShowing.value = false },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = MaterialTheme.colors.onSurface,
                            ),
                        ) {
                            Text(text = "Cancel")
                        }
                    }
                )
            }
        }
    }
}
