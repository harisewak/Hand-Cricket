package com.harisewak.handcricket.domain

object Computer : Player {

    override fun chooseOddOrEven(): PlayerResponse {
        val resp = (0..1).random()
        return if (resp == Odd.value) Odd else Even
    }

    override fun choosePlayAgain(): PlayerResponse {
        val resp = (0..1).random()
        return if (resp == Yes.value) Yes else No
    }

    override fun chooseBatOrBowl(): PlayerResponse {
        val resp = (0..1).random()
        return if (resp == Bat.value) Bat else Bowl
    }

    override fun chooseNumber(): PlayerResponse {
        val resp = (1..6).random()

        return when (resp) {
            One.value -> One
            Two.value -> Two
            Three.value -> Three
            Four.value -> Four
            Five.value -> Five
            Six.value -> Six
            else -> UnExpected
        }
    }
}