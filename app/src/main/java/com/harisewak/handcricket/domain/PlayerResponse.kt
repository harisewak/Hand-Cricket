package com.harisewak.handcricket.domain

sealed class PlayerResponse(val value: Int)

object Odd: PlayerResponse(1)
object Even: PlayerResponse(0)
object Bat: PlayerResponse(1)
object Bowl: PlayerResponse(0)
object Yes: PlayerResponse(1)
object No: PlayerResponse(0)
object One: PlayerResponse(1)
object Two: PlayerResponse(2)
object Three: PlayerResponse(3)
object Four: PlayerResponse(4)
object Five: PlayerResponse(5)
object Six: PlayerResponse(6)
object UnExpected: PlayerResponse(-1)
