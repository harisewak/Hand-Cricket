package com.harisewak.handcricket.util

const val TAG = "HandCricketApp"

object TestUtil {
    var enable = false

    fun debug(message: String) {
        if (enable) {
            println("$TAG: $message")
        }
    }

}