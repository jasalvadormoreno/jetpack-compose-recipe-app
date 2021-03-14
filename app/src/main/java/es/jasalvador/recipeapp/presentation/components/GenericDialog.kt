package es.jasalvador.recipeapp.presentation.components

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun GenericDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    title: String,
    message: String? = null,
    positiveAction: PositiveAction? = null,
    dismissAction: DismissAction? = null,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier,
        title = { Text(text = title) },
        text = { message?.let { Text(text = it) } },
        confirmButton = {
            positiveAction?.let { TextButton(onClick = it.action) { Text(text = it.text) } }
        },
        dismissButton = {
            dismissAction?.let {
                TextButton(
                    onClick = it.action,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colors.onSurface,
                    ),
                ) { Text(text = it.text) }
            }
        },
    )
}

data class PositiveAction(
    val text: String,
    val action: () -> Unit,
)

data class DismissAction(
    val text: String,
    val action: () -> Unit,
)
