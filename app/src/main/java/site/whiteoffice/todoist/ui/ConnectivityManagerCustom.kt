package site.whiteoffice.todoist.ui

import android.app.Application
import android.content.Context
import android.net.*
import android.net.ConnectivityManager.NetworkCallback
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ConnectivityManagerCustom(
    application: Application,
    private val savedStateHandle: SavedStateHandle
) :
    AndroidViewModel((application)) {
    private val DELOG_TAG: String = ConnectivityManagerCustom::class.java.simpleName

    companion object {
        const val connectedToInternetKey = "connectedToInternetKey"
    }


    fun  setUp () {
        registerNetworkCallback()

    }

    val isConnectedToInternetLiveData: LiveData<Boolean>
        get() = savedStateHandle.getLiveData(
            connectedToInternetKey,
            false
        )



    private fun registerNetworkCallback() {
        Log.d(DELOG_TAG, "registerNetworkCallback")
        val builder: NetworkRequest.Builder = NetworkRequest.Builder()
        val check: ConnectivityManager = getApplication<Application>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        check.registerNetworkCallback(builder.build(), networkCallback)
    }

    private val networkCallback: NetworkCallback = object : NetworkCallback() {

        override fun onAvailable(network: Network) {
            Log.d("TAG", "NetworkCallback, onAvailable")
            setInternetConnection(true)
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            Log.d(DELOG_TAG, "NetworkCallback, onCapabilitiesChanged")

        }


        override fun onLost(network: Network) {
            Log.d("TAG", "NetworkCallback, onLost")
            setInternetConnection(false)
        }

        override fun onUnavailable() {
            super.onUnavailable()
            Log.d("TAG", "NetworkCallback, onUnavailable")
            setInternetConnection(false)

        }

        override fun onLosing(network: Network, maxMsToLive: Int) {
            super.onLosing(network, maxMsToLive)
            Log.d("TAG", "NetworkCallback, onLosing")

        }
    }


    private fun setInternetConnection(boolean: Boolean) {
        Handler(Looper.getMainLooper()).post {
            savedStateHandle.set(connectedToInternetKey, boolean)

        }

    }



    override fun onCleared() {
        super.onCleared()
        val check: ConnectivityManager? =
            getApplication<Application>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (check != null && networkCallback != null) {
            check.unregisterNetworkCallback(networkCallback)
        }
    }
}