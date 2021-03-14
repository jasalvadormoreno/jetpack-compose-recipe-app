package es.jasalvador.recipeapp.interactors.app

import android.util.Log
import es.jasalvador.recipeapp.util.TAG
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

object DoesNetworkHaveInternet {

    fun execute(): Boolean {
        return try {
            Log.d(TAG, "execute: PINGING GOOGLE")
            val socket = Socket()
            socket.connect(InetSocketAddress("8.8.8.8", 53), 1500)
            socket.close()
            Log.d(TAG, "execute: PING SUCCESS")
            true
        } catch (e: IOException) {
            Log.e(TAG, "execute: No internet connection $e")
            false
        }
    }
}