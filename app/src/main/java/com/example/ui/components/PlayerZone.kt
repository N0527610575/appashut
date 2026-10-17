package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GemColor
import com.example.ui.theme.*

@Composable
fun PlayerZone(
  playerNumber: Int,
  playerName: String,
  partnerName: String,
  isActiveTurn: Boolean,
  lastRoll: Int?,
  isRolling: Boolean,
  stones: List<GemColor>,
  withDice: Boolean,
  withStones: Boolean,
  isFirgunOpen: Boolean,
  onDieClick: () -> Unit,
  onStonePileClick: () -> Unit,
  onConfirmFirgun: () -> Unit,
  modifier: Modifier = Modifier
) {
  val borderColor = if (isActiveTurn) AccentSky else Color(0x33B4A591)
  val containerBg = if (isActiveTurn) {
    Brush.linearGradient(listOf(Color(0xFFFFFFFF), Color(0xFFF0F9FF)))
  } else {
    Brush.linearGradient(listOf(Color(0xFFFFFFFF), Color(0xFFFAF6EE)))
  }

  Box(
    modifier = modifier
      .shadow(if (isActiveTurn) 8.dp else 4.dp, RoundedCornerShape(22.dp), spotColor = if (isActiveTurn) AccentSky else Color(0x22000000))
      .clip(RoundedCornerShape(22.dp))
      .background(containerBg)
      .border(if (isActiveTurn) 2.dp else 1.dp, borderColor, RoundedCornerShape(22.dp))
      .padding(10.dp)
      .testTag("player_zone_$playerNumber")
  ) {
    Column(
      modifier = Modifier.fillMaxWidth(),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      // Top row: Player info + Die
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            if (isActiveTurn) {
              Box(
                modifier = Modifier
                  .size(8.dp)
                  .clip(CircleShape)
                  .background(AccentSky)
              )
            }
            Text(
              text = playerName,
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold,
              color = InkDark,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
          }

          if (withDice) {
            Text(
              text = if (lastRoll != null) "יצא: $lastRoll" else "לחצו להטלה",
              fontSize = 11.sp,
              fontWeight = FontWeight.Medium,
              color = if (isActiveTurn) AccentSky else InkLight
            )
          } else {
            Text(
              text = if (isActiveTurn) "תורך להרים קלף" else "ממתין/ה",
              fontSize = 11.sp,
              fontWeight = FontWeight.Medium,
              color = if (isActiveTurn) AccentSky else InkLight
            )
          }
        }

        if (withDice) {
          DiceView(
            value = lastRoll,
            isRolling = isRolling,
            onClick = onDieClick,
            size = 44.dp
          )
        }
      }

      // Bottom Row: Stone Pile area with Firgun Popover
      if (withStones) {
        Box(
          modifier = Modifier.fillMaxWidth()
        ) {
          FirgunPopover(
            visible = isFirgunOpen,
            playerName = playerName,
            partnerName = partnerName,
            stones = stones,
            onConfirmFirgun = onConfirmFirgun
          )

          // Stone Pile Container
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(14.dp))
              .background(Color(0x228B7355))
              .border(1.dp, Color(0x33A08C73), RoundedCornerShape(14.dp))
              .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = true),
                onClick = onStonePileClick
              )
              .padding(horizontal = 8.dp, vertical = 5.dp)
              .testTag("stone_pile_$playerNumber"),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              modifier = Modifier.weight(1f),
              horizontalArrangement = Arrangement.spacedBy((-3).dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              if (stones.isEmpty()) {
                Text(
                  text = "אין אבנים",
                  fontSize = 10.sp,
                  color = InkLight
                )
              } else {
                stones.take(8).forEach { gem ->
                  GlassGemView(
                    gemColor = gem,
                    size = 17.dp
                  )
                }
              }
            }

            Surface(
              shape = RoundedCornerShape(10.dp),
              color = Color.White,
              border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x44D1D5DB))
            ) {
              Text(
                text = "${stones.size}/10",
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                color = if (stones.size >= 10) AccentGold else InkDark,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }
        }
      }
    }
  }
}

@Composable
private fun BoxScope.FirgunPopover(
  visible: Boolean,
  playerName: String,
  partnerName: String,
  stones: List<GemColor>,
  onConfirmFirgun: () -> Unit
) {
  AnimatedVisibility(
    visible = visible,
    enter = fadeIn() + scaleIn(),
    exit = fadeOut() + scaleOut(),
    modifier = Modifier
      .align(Alignment.BottomCenter)
      .offset(y = (-44).dp)
  ) {
    Surface(
      shape = RoundedCornerShape(16.dp),
      color = Color.White,
      shadowElevation = 10.dp,
      border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x440EA5E9)),
      modifier = Modifier.widthIn(min = 150.dp, max = 190.dp)
    ) {
      Column(
        modifier = Modifier.padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        if (stones.isEmpty()) {
          Text(
            text = "עוד אין ל$playerName אבנים לפרגן",
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = InkMuted,
            textAlign = TextAlign.Center
          )
        } else {
          Text(
            text = "לפרגן אבן טובה ל$partnerName?",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = InkDark,
            textAlign = TextAlign.Center
          )
          Spacer(modifier = Modifier.height(6.dp))
          Button(
            onClick = onConfirmFirgun,
            colors = ButtonDefaults.buttonColors(
              containerColor = AccentSky
            ),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
          ) {
            Text(
              text = "פרגן אבן 💎",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
          }
        }
      }
    }
  }
}
