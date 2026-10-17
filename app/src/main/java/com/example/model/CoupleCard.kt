package com.example.model

enum class CardType {
  INDIVIDUAL,
  SHARED
}

enum class CardTheme(val index: Int) {
  SUNFLOWER(0),
  PEACH(1),
  MINT(2),
  LILAC(3)
}

data class CoupleCard(
  val id: Long,
  val type: CardType,
  val title: String,
  val text: String,
  val challengeMode: Boolean = false,
  val isCustom: Boolean = false,
  val theme: CardTheme = CardTheme.SUNFLOWER
)
