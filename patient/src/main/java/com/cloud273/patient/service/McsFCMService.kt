package com.cloud273.patient.service

import com.google.firebase.messaging.FirebaseMessagingService

class McsFCMService: FirebaseMessagingService() {

    override fun onNewToken(p0: String) {
        println("onNewToken = $p0")
    }

}