package com.realityexpander.appwritetest

import androidx.lifecycle.ViewModel
import io.appwrite.Client
import io.appwrite.services.Account

class MainActivityViewModel: ViewModel() {

    fun gotEvent() {
        println("Got event")
    }
}