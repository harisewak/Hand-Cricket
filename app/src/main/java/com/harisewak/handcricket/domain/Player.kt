package com.harisewak.handcricket.domain

interface Player {
    fun chooseOddOrEven(): PlayerResponse
    fun choosePlayAgain(): PlayerResponse
    fun chooseBatOrBowl(): PlayerResponse
    fun chooseNumber(): PlayerResponse
}