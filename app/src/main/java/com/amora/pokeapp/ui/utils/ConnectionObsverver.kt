package com.amora.pokeapp.ui.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject


class ConnectivityObserver @Inject constructor(context: Context) {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val internetStatus: Flow<InternetStatus> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(InternetStatus.Connected("Connected"))
            }

            override fun onLost(network: Network) {
                trySend(InternetStatus.Disconnected("Disconnected"))
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(request, callback)

        val isConnected = connectivityManager.activeNetwork
            ?.let { connectivityManager.getNetworkCapabilities(it) }
            ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).orFalse()
        trySend(if (isConnected) InternetStatus.Connected("Connected") else InternetStatus.Disconnected("Disconnected"))

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }.distinctUntilChanged()
}

sealed class InternetStatus {
    object Idle: InternetStatus()
    data class Connected(val message: String) : InternetStatus()
    data class Disconnected(val message: String) : InternetStatus()
}

suspend fun SnackbarHostState.showInternetStatus(status: InternetStatus) {
    when (status) {
        is InternetStatus.Connected -> {
            showSnackbar("You are connected to the internet")
        }
        is InternetStatus.Disconnected -> {
            showSnackbar("You are disconnected from the internet")
        }
        else -> Unit
    }
}
