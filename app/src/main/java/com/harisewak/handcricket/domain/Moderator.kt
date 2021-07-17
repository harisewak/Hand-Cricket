package com.harisewak.handcricket.domain

import com.harisewak.handcricket.presenter.HandCricketPresenter
import com.harisewak.handcricket.util.isEven
import com.harisewak.handcricket.util.isOdd

/*
* This class is responsible for managing the game.
* Its primary responsibilities are:
* 1) Prompting users to play
* 2) Tracking stats of the game
* 3) Making decisions
* */

/* Complete responsibilities are:
    * Announce beginning of the game
    Prompt for Odd/Even selection
    Record who chooses what (e.g. user = even, computer = odd)
    Prompt to choose number
    Decide who won
    Prompt winner to choose batting/bowling
    Record decision (e.g. user_selects = batting)
    Prompt to choose number
    Decide runs or out. If same numbers were chosen its out else batter gets runs
    Update batter's score
    When batter gets out, announce the new batsmen
    Prompt to choose number
    Decide runs or out. If same numbers were chosen its out else batter gets runs
    Update batter's score
    When batter gets out, check both scores and announce result
    Restart
    * */

object Moderator {

    private val user = User
    private val computer = Computer
    private val gameState = GameState()

    private val presenter = HandCricketPresenter

    lateinit var promptType: PromptType
        private set

    // The entry point of the game
    suspend fun startGame() {
        promptType = PromptType.CHOOSE_ODD_EVEN
        presenter.showOddEvenPrompt()
    }

    private fun reset() {
        gameState.curInnings = -1
        gameState.userScore = 0
        gameState.computerScore = 0
        gameState.userWicketsFallen = 0
        gameState.computerWicketsFallen = 0
    }

    suspend fun postUserResponse(userResponse: PlayerResponse) {
        gameState.userResponse = userResponse
        when (userResponse) {
            Odd -> {
                promptType = PromptType.CHOOSE_NUMBER_FOR_TOSS
                gameState.userSelects = Odd
                presenter.showSelectNumberForTossPrompt()
            }
            Even -> {
                promptType = PromptType.CHOOSE_NUMBER_FOR_TOSS
                gameState.userSelects = Even
                presenter.showSelectNumberForTossPrompt()
            }
            Bat -> {
                gameState.batsFirst = user
                initScoreBoard(userResponse)
                promptType = PromptType.CHOOSE_NUMBER_TO_PLAY
                presenter.showSelectNumberForPlayingPrompt()
            }
            Bowl -> {
                presenter.updateEventRecord(eventUserBowlsFirst)
                initScoreBoard(userResponse)
                promptType = PromptType.CHOOSE_NUMBER_TO_PLAY
                presenter.showSelectNumberForPlayingPrompt()
            }
            One -> {
                processSelectedNumber(userResponse)
            }
            Two -> {
                processSelectedNumber(userResponse)
            }
            Three -> {
                processSelectedNumber(userResponse)
            }
            Four -> {
                processSelectedNumber(userResponse)
            }
            Five -> {
                processSelectedNumber(userResponse)
            }
            Six -> {
                processSelectedNumber(userResponse)
            }
            Yes -> {
                reset()
                startGame()
            }
            No -> {
                // user wants to exit. This statement will probably not be reached
            }
        }
    }

    private suspend fun processSelectedNumber(userResponse: PlayerResponse) {

        if (promptType == PromptType.CHOOSE_NUMBER_FOR_TOSS) {

            processTossResponse(userResponse)

        } else {

            processPlayResponse(userResponse)

        }
    }

    private suspend fun processPlayResponse(userResponse: PlayerResponse) {

        val compResponse = computer.chooseNumber()

        if (userResponse == compResponse) {

            // user loses wicket
            if (gameState.curBatsman == user) {

                gameState.userWicketsFallen += 1
                presenter.updateEventRecord(eventUserLosesWicket)

                var score = if (gameState.curInnings == 1) {
                    "Your score: ${gameState.userScore}/${gameState.userWicketsFallen}"
                } else {
                    "Your score: ${gameState.userScore}/${gameState.userWicketsFallen}\nTarget: ${gameState.computerScore}"
                }

                presenter.updateScore(score)

                // user is out
                if (gameState.userWicketsFallen == MAX_WICKETS) {

                    if (gameState.curInnings == 1) {
                        gameState.curInnings = 2 // 2nd innings
                        gameState.curBatsman = computer
                        presenter.updateEventRecord(eventComputerStartsBatting)
                        score =
                            "Computer score: ${gameState.computerScore}/${gameState.computerWicketsFallen}\nTarget: ${gameState.userScore}"
                        presenter.updateScore(score)
                        presenter.showSelectNumberForPlayingPrompt()

                    } else {
                        // game over. Determine winner!
                        if (gameState.userScore > gameState.computerScore) {
                            // user won
                            presenter.updateEventRecord(eventUserWonTheMatch)
                        } else if (gameState.computerScore > gameState.userScore) {
                            // computer won
                            presenter.updateEventRecord(eventComputerWonTheMatch)
                        } else {
                            // its a tie!
                            presenter.updateEventRecord(eventMatchTied)
                        }

                        promptType = PromptType.PLAY_AGAIN
                        presenter.showPlayAgainPrompt()
                    }
                } else {
                    presenter.showSelectNumberForPlayingPrompt()
                }

            } else {
                // computer loses a wicket
                gameState.computerWicketsFallen += 1

                presenter.updateEventRecord(eventComputerLosesWicket)

                var score =
                    if (gameState.curInnings == 1) {
                        "Computer score: ${gameState.computerScore}/${gameState.computerWicketsFallen}"
                    } else {
                        "Computer score: ${gameState.computerScore}/${gameState.computerWicketsFallen}\nTarget: ${gameState.userScore}"

                    }

                presenter.updateScore(score)

                // computer is out
                if (gameState.computerWicketsFallen == MAX_WICKETS) {
                    if (gameState.curInnings == 1) {
                        gameState.curInnings = 2 // 2nd innings
                        gameState.curBatsman = user
                        presenter.updateEventRecord(eventUserStartsBatting)
                        score =
                            "Your score: ${gameState.userScore}/${gameState.userWicketsFallen}\nTarget: ${gameState.computerScore}"
                        presenter.updateScore(score)
                        presenter.showSelectNumberForPlayingPrompt()
                    } else {
                        // game over. Determine winner!
                        if (gameState.userScore > gameState.computerScore) {
                            // user won
                            presenter.updateEventRecord(eventUserWonTheMatch)
                        } else if (gameState.computerScore > gameState.userScore) {
                            // computer won
                            presenter.updateEventRecord(eventComputerWonTheMatch)
                        } else {
                            // its a tie!
                            presenter.updateEventRecord(eventMatchTied)
                        }

                        promptType = PromptType.PLAY_AGAIN
                        presenter.showPlayAgainPrompt()
                    }
                } else {
                    presenter.showSelectNumberForPlayingPrompt()
                }
            }

        } else {
            // user scored runs
            if (gameState.curBatsman == user) {
                gameState.userScore += userResponse.value // updating user score

                val eventRuns = "You scored ${userResponse.value} run(s)"
                presenter.updateEventRecord(eventRuns)

                val score = if (gameState.curInnings == 1) {
                    "Your score: ${gameState.userScore}/${gameState.userWicketsFallen}"
                } else {
                    "Your score: ${gameState.userScore}/${gameState.userWicketsFallen}\nTarget: ${gameState.computerScore}"
                }


                presenter.updateScore(score)

                // user chased down the target
                if (gameState.curInnings == 2 && gameState.userScore > gameState.computerScore) {
                    presenter.updateEventRecord(eventUserWonTheMatch)
                    promptType = PromptType.PLAY_AGAIN
                    presenter.showPlayAgainPrompt()
                    return
                }

            } else {
                // computer scored runs
                gameState.computerScore += compResponse.value // updating computer score

                val eventRuns = "Computer scored ${compResponse.value} run(s)"
                presenter.updateEventRecord(eventRuns)

                val score =
                    if (gameState.curInnings == 1) {
                        "Computer score: ${gameState.computerScore}/${gameState.computerWicketsFallen}"
                    } else {
                        "Computer score: ${gameState.computerScore}/${gameState.computerWicketsFallen}\nTarget: ${gameState.userScore}"

                    }

                presenter.updateScore(score)

                // computer chased down the target
                if (gameState.curInnings == 2 && gameState.computerScore > gameState.userScore) {
                    presenter.updateEventRecord(eventComputerWonTheMatch)
                    promptType = PromptType.PLAY_AGAIN
                    presenter.showPlayAgainPrompt()
                    return
                }
            }

            presenter.showSelectNumberForPlayingPrompt()

        }

    }

    private suspend fun processTossResponse(userResponse: PlayerResponse) {

        val compResponse = computer.chooseNumber()

        if (userWonToss(compResponse, userResponse)) {

            gameState.tossWinner = User

            presenter.updateEventRecord(eventUserWonToss)

            promptType = PromptType.CHOOSE_BAT_BOWL

            presenter.showBatOrBowlPrompt()

        } else {

            gameState.tossWinner = Computer

            presenter.updateEventRecord(eventCompWonToss)

            val input = computer.chooseBatOrBowl()

            initScoreBoard(input)

        }
    }

    private suspend fun initScoreBoard(playerResponse: PlayerResponse) {

        if (gameState.tossWinner == User) {

            if (playerResponse == Bat) {

                presenter.updateEventRecord(eventUserBatsFirst)
                gameState.curInnings = 1
                gameState.curBatsman = user
                val score =
                    "Your score: ${gameState.userScore}/${gameState.userWicketsFallen}"
                presenter.updateScore(score)

            } else {

                presenter.updateEventRecord(eventUserBowlsFirst)
                gameState.curInnings = 1
                gameState.curBatsman = computer
                val score =
                    "Computer score: ${gameState.computerScore}/${gameState.computerWicketsFallen}"
                presenter.updateScore(score)

            }

        } else {

            if (playerResponse == Bat) {

                presenter.updateEventRecord(eventComputerBatsFirst)
                gameState.curInnings = 1
                gameState.curBatsman = computer
                val score =
                    "Computer score: ${gameState.computerScore}/${gameState.computerWicketsFallen}"
                presenter.updateScore(score)

            } else {

                presenter.updateEventRecord(eventComputerBowlsFirst)
                gameState.curInnings = 1
                gameState.curBatsman = user
                val score =
                    "Your score: ${gameState.userScore}/${gameState.userWicketsFallen}"
                presenter.updateScore(score)

            }

            promptType = PromptType.CHOOSE_NUMBER_TO_PLAY
            presenter.showSelectNumberForPlayingPrompt()
        }
    }

    private fun userWonToss(compResponse: PlayerResponse, userResponse: PlayerResponse): Boolean {

        val total = userResponse.value + compResponse.value

        return total.isEven() && gameState.userSelects == Even ||
                total.isOdd() && gameState.userSelects == Odd
    }

}