package com.harisewak.handcricket.domain

// Holds game information
class GameState {
    lateinit var tossWinner: Player
    lateinit var userSelects: PlayerResponse // will either be Odd or Even
    lateinit var userResponse: PlayerResponse
    var curInnings = -1 // should either be 1 or 2
    lateinit var batsFirst: Player
    var userScore: Int = 0
    var computerScore: Int = 0
    var userWicketsFallen: Int = 0
    var computerWicketsFallen: Int = 0
    lateinit var curBatsman: Player
}