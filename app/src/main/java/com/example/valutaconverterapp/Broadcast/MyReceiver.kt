package com.example.valutaconverterapp.Broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.valutaconverterapp.Utils.MyData
import com.example.valutaconverterapp.Utils.MyNetworkHelper

class MyReceiver : BroadcastReceiver() {

    private lateinit var myNetworkHelper: MyNetworkHelper

    override fun onReceive(context: Context, intent: Intent) {
        myNetworkHelper = MyNetworkHelper(context)
        MyData.internetLiveData.postValue(myNetworkHelper.isNetworkConnected())
    }
}