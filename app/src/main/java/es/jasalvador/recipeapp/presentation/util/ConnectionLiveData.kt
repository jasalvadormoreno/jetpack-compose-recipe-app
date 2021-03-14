package es.jasalvador.recipeapp.presentation.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import androidx.core.content.getSystemService
import androidx.lifecycle.LiveData
import es.jasalvador.recipeapp.interactors.app.DoesNetworkHaveInternet
import es.jasalvador.recipeapp.util.TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ConnectionLiveData(context: Context) : LiveData<Boolean>() {

    private lateinit var networkCallback: ConnectivityManager.NetworkCallback
    private val cm = context.getSystemService<ConnectivityManager>()
    private val validNetworks: MutableSet<Network> = HashSet()

    override fun onActive() {
        super.onActive()
        networkCallback = createNetworkCallback()
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        cm?.registerNetworkCallback(networkRequest, networkCallback)
    }

    override fun onInactive() {
        super.onInactive()

        cm?.unregisterNetworkCallback(networkCallback)
    }

    private fun createNetworkCallback() = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)

            val networkCapabilities = cm?.getNetworkCapabilities(network)
            val hasInternetCapability =
                networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    ?: false
            if (hasInternetCapability) {
                CoroutineScope(Dispatchers.IO).launch {
                    val hasInternet = DoesNetworkHaveInternet.execute(network.socketFactory)
                    if (hasInternet) {
                        withContext(Dispatchers.Main) {
                            Log.d(TAG, "onAvailable: This network has internet $network")
                            validNetworks.add(network)
                            checkValidNetworks()
                        }
                    }
                }
            }
        }

        override fun onLost(network: Network) {
            super.onLost(network)

            validNetworks.remove(network)
            checkValidNetworks()
        }
    }

    private fun checkValidNetworks() {
        postValue(validNetworks.size > 0)
    }
}
