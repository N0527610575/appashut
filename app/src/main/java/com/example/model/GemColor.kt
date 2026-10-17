package com.example.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.*

enum class GemColor(
  val primary: Color,
  val light: Color,
  val dark: Color,
  val hebrewName: String
) {
  TURQUOISE(
    primary = GemTurquoisePrimary,
    light = GemTurquoiseLight,
    dark = GemTurquoiseDark,
    hebrewName = "טורקיז"
  ),
  EMERALD(
    primary = GemEmeraldPrimary,
    light = GemEmeraldLight,
    dark = GemEmeraldDark,
    hebrewName = "אמרלד"
  ),
  AMBER(
    primary = GemAmberPrimary,
    light = GemAmberLight,
    dark = GemAmberDark,
    hebrewName = "ענבר"
  ),
  PEARL(
    primary = GemPearlPrimary,
    light = GemPearlLight,
    dark = GemPearlDark,
    hebrewName = "פנינה"
  );

  companion object {
    fun fromIndex(index: Int): GemColor {
      val values = entries
      return values[(index % values.size + values.size) % values.size]
    }
  }
}
