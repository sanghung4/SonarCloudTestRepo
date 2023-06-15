package com.reece.pickingapp.networking

import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import com.reece.pickingapp.R
import com.reece.pickingapp.utils.ActivityService
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Inject

@Module
@InstallIn(ActivityComponent::class)
class ConnectivityService @Inject constructor(
    private val activityService: ActivityService
) {
    private lateinit var connectivityManager: ConnectivityManager
    private var alert: AlertDialog? = null
    private lateinit var networkCallback: ConnectivityManager.NetworkCallback

    fun start() {
        setUpNetworkCallback()
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        activityService.activity?.let { activity ->
            connectivityManager =
                activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            connectivityManager.requestNetwork(networkRequest, networkCallback)
        }
    }

    private fun setUpNetworkCallback() {
        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)

                alert?.dismiss()
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                super.onCapabilitiesChanged(network, networkCapabilities)
                Log.d("NETWORK", "onCapabilitiesChanged: $network")
            }

            override fun onLost(network: Network) {
                super.onLost(network)

                showConnectivityLostDialog()
            }
        }
    }

    private fun showConnectivityLostDialog() {
        val alertBuilder = AlertDialog.Builder(activityService.activity)
        alertBuilder.setCancelable(false)
        alertBuilder.setTitle(activityService.getString(R.string.no_internet_connection))
        alert = alertBuilder.create()
        alert?.show()
    }
}