package com.harisewak.handcricket.util

import android.content.Context
import android.view.View
import android.widget.Toast

const val TAG = "HandCricketApp"

const val ENABLE = true


fun debug(message: String) {
    if (ENABLE) {
        println("$TAG: $message")
    }
}


