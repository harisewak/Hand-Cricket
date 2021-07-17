package com.harisewak.handcricket.presenter

import com.harisewak.handcricket.domain.*
import com.harisewak.handcricket.util.debug
import java.util.ArrayList

object HandCricketPresenter {
    private lateinit var view: View
    val promptType: PromptType
    get() = moderator.promptType

    suspend fun start(view: View) {
        debug("Current thread: ${Thread.currentThread().name}")
        this.view = view
        moderator.startGame()
    }

    suspend fun showOddEvenPrompt() {
        view.showOddOrEvenPrompt()
    }

    suspend fun userSelectedOdd() {
        moderator.postUserResponse(Odd)
    }

    suspend fun userSelectedEven() {
        moderator.postUserResponse(Even)
    }

    suspend fun userSelectedBatting() {
        moderator.postUserResponse(Bat)
    }
    suspend fun userSelectedBowling() {
        moderator.postUserResponse(Bowl)
    }

    suspend fun restart() {
        moderator.postUserResponse(Yes)
    }

    suspend fun stop() {
        view.shutdown()
    }


    suspend fun userSelectedNumber(number: Int) {
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

    suspend fun showSelectNumberForTossPrompt() {
        view.showSelectNumberForTossPrompt()
    }

    suspend fun showBatOrBowlPrompt() {
        view.showBatOrBowlPrompt()
    }

    suspend fun showSelectNumberForPlayingPrompt() {
        view.showSelectNumberForPlayingPrompt()
    }

    suspend fun updateScore(score: String) {
        view.updateScore(score)
    }

    suspend fun showPlayAgainPrompt() {
        view.showPlayAgainPrompt()
    }

    suspend fun updateEventRecord(event: String) {
        view.updateEventRecord(event)
    }

    private val moderator = Moderator


    interface View {

        suspend fun showOddOrEvenPrompt()
        suspend fun shutdown()
        suspend fun showSelectNumberForTossPrompt()
        suspend fun showBatOrBowlPrompt()
        suspend fun showSelectNumberForPlayingPrompt()
        suspend fun updateScore(score: String)
        suspend fun showPlayAgainPrompt()
        suspend fun updateEventRecord(event: String)

    }
}