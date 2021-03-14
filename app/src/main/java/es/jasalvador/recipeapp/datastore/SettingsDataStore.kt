package es.jasalvador.recipeapp.datastore

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import es.jasalvador.recipeapp.BaseApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsDataStore
@Inject constructor(private val app: BaseApp) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private val scope = CoroutineScope(Dispatchers.Main)

    val isDark = mutableStateOf(false)

    init {
        observeDataStore()
    }

    fun toggleTheme() {
        scope.launch {
            app.dataStore.edit { prefs ->
                val current = prefs[DARK_THEME_KEY] ?: false
                prefs[DARK_THEME_KEY] = !current
            }
        }
    }

    private fun observeDataStore() {
        app.dataStore.data.onEach { prefs ->
            prefs[DARK_THEME_KEY]?.let {
                isDark.value = it
            }
        }.launchIn(scope)
    }

    companion object {
        private val DARK_THEME_KEY = booleanPreferencesKey("dark_theme_key")
    }
}