package es.jasalvador.recipeapp.presentation.util

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LifecycleOwner
import es.jasalvador.recipeapp.BaseApp
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConnectivityManager
@Inject constructor(
    app: BaseApp
) {

    private val connectionLiveData = ConnectionLiveData(app)

    val isNetworkAvailable = mutableStateOf(false)

    fun registerConnectionObserver(lifecycleOwner: LifecycleOwner) {
        connectionLiveData.observe(lifecycleOwner, { isConnected ->
            isNetworkAvailable.value = isConnected
        })
    }

    fun unregisterConnectionObserver(lifecycleOwner: LifecycleOwner) {
        connectionLiveData.removeObservers(lifecycleOwner)
    }
}
