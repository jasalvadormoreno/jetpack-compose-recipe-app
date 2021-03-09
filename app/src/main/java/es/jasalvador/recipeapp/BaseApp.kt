package es.jasalvador.recipeapp

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApp : Application() {
    val isDark = mutableStateOf(false)

    fun toggleTheme() {
        isDark.value = isDark.value.not()
    }
}
