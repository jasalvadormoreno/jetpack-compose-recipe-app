package es.jasalvador.recipeapp.domain.data

data class DataState<out T>(
    val data: T? = null,
    val error: String? = null,
    val loading: Boolean = false,
) {

    companion object {

        fun <T> success(data: T): DataState<T> = DataState(data = data)

        fun <T> error(message: String): DataState<T> = DataState(error = message)

        fun <T> loading(): DataState<T> = DataState(loading = true)
    }
}
