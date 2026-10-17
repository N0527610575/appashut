package com.example.model

enum class SeatingMode {
  SIDE_BY_SIDE, // יושבים זה לצד זו (ברירת מחדל)
  FACE_TO_FACE  // יושבים זה מול זו (משני צידי השולחן, שחקן 2 הפוך ב-180 מעלות)
}

data class GameSettings(
  val player1Name: String = "שחקן 1",
  val player2Name: String = "שחקן 2",
  val withDice: Boolean = true,
  val withStones: Boolean = true,
  val soundEnabled: Boolean = true,
  val seatingMode: SeatingMode = SeatingMode.SIDE_BY_SIDE
)

data class GameState(
  val settings: GameSettings = GameSettings(),
  val bankStones: Int = 20,
  val player1Stones: List<GemColor> = emptyList(),
  val player2Stones: List<GemColor> = emptyList(),
  val activePlayer: Int = 1, // 1 or 2
  val hasRolledThisTurn: Boolean = false,
  val lastRollP1: Int? = null,
  val lastRollP2: Int? = null,
  val isCardFlipped: Boolean = false,
  val currentCardIndex: Int = 0,
  val deck: List<CoupleCard> = emptyList(),
  val winnerPlayer: Int? = null,
  val isRolling: Boolean = false,
  val statusMessage: String = "",
  val activeFirgunPlayer: Int? = null // When firgun popover is open
)
