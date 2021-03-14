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

class GenericDialogInfo
private constructor(builder: Builder) {
    val title: String
    val onDismiss: () -> Unit
    val message: String?
    val positiveAction: PositiveAction?
    val dismissAction: DismissAction?

    init {
        if (builder.title == null) throw NullPointerException("GenericDialogInfo title cannot be null.")
        if (builder.onDismiss == null) throw NullPointerException("GenericDialogInfo onDismiss cannot be null.")

        this.title = builder.title!!
        this.onDismiss = builder.onDismiss!!
        this.message = builder.message
        this.positiveAction = builder.positiveAction
        this.dismissAction = builder.dismissAction
    }

    class Builder {
        var title: String? = null
            private set
        var onDismiss: (() -> Unit)? = null
            private set
        var message: String? = null
            private set
        var positiveAction: PositiveAction? = null
            private set
        var dismissAction: DismissAction? = null
            private set

        fun title(title: String): Builder {
            this.title = title
            return this
        }

        fun onDismiss(onDismiss: (() -> Unit)): Builder {
            this.onDismiss = onDismiss
            return this
        }

        fun message(message: String): Builder {
            this.message = message
            return this
        }

        fun positiveAction(positiveAction: PositiveAction): Builder {
            this.positiveAction = positiveAction
            return this
        }

        fun dismissAction(dismissAction: DismissAction): Builder {
            this.dismissAction = dismissAction
            return this
        }

        fun build() = GenericDialogInfo(this)
    }
}
