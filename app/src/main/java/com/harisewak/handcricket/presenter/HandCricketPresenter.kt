package com.harisewak.handcricket.presenter

import com.harisewak.handcricket.domain.*
import java.util.ArrayList

object HandCricketPresenter {
    private lateinit var view: View
    val promptType: PromptType
    get() = moderator.promptType

    fun start(view: View) {
        this.view = view
        moderator.startGame()
    }

    fun showOddEvenPrompt() {
        view.showOddOrEvenPrompt()
    }

    fun userSelectedOdd() {
        moderator.postUserResponse(Odd)
    }

    fun userSelectedEven() {
        moderator.postUserResponse(Even)
    }

    fun userSelectedBatting() {
        moderator.postUserResponse(Bat)
    }
    fun userSelectedBowling() {
        moderator.postUserResponse(Bowl)
    }

    fun restart() {
        moderator.postUserResponse(Yes)
    }

    fun stop() {
        view.shutdown()
    }
    fun userSelectedNumber(number: Int) {
        moderator.postUserResponse(identifyResponse(number))
    }

    private fun identifyResponse(number: Int): PlayerResponse {
        return when (number) {
            1 -> One
            2 -> Two
            3 -> Three
            4 -> Four
            5 -> Five
            6 -> Six
            else -> UnExpected
        }
    }

    fun showSelectNumberForTossPrompt() {
        view.showSelectNumberForTossPrompt()
    }

    fun showBatOrBowlPrompt() {
        view.showBatOrBowlPrompt()
    }

    fun showSelectNumberForPlayingPrompt() {
        view.showSelectNumberForPlayingPrompt()
    }

    fun updateScore(score: String) {
        view.updateScore(score)
    }

    fun showPlayAgainPrompt() {
        view.showPlayAgainPrompt()
    }

    fun updateEventRecord(event: String) {
        view.updateEventRecord(event)
    }

    private val moderator = Moderator


    interface View {

        fun showOddOrEvenPrompt()
        fun shutdown()
        fun showSelectNumberForTossPrompt()
        fun showBatOrBowlPrompt()
        fun showSelectNumberForPlayingPrompt()
        fun updateScore(score: String)
        fun showPlayAgainPrompt()
        fun updateEventRecord(event: String)

    }
}