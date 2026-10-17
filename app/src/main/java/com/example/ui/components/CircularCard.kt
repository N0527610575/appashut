package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CardTheme
import com.example.model.CardType
import com.example.model.CoupleCard
import com.example.ui.theme.*

@Composable
fun CircularCard(
  card: CoupleCard?,
  isFlipped: Boolean,
  cardNumber: Int,
  totalCards: Int,
  activePlayerName: String,
  partnerName: String,
  isReadyToFlip: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  cardDiameter: Dp = 310.dp
) {
  val rotationY by animateFloatAsState(
    targetValue = if (isFlipped) 180f else 0f,
    animationSpec = tween(durationMillis = 620, easing = FastOutSlowInEasing),
    label = "cardFlipRotation"
  )

  Box(
    modifier = modifier
      .size(cardDiameter)
      .testTag("circular_card_deck"),
    contentAlignment = Alignment.Center
  ) {
    // Stack layers underneath (visual depth of physical deck)
    Box(
      modifier = Modifier
        .size(cardDiameter)
        .offset(x = 6.dp, y = 7.dp)
        .rotate(5f)
        .scale(0.98f)
        .shadow(12.dp, CircleShape)
        .clip(CircleShape)
        .background(
          Brush.linearGradient(
            listOf(ThemeMintLight, ThemeMintBg)
          )
        )
    )

    Box(
      modifier = Modifier
        .size(cardDiameter)
        .offset(x = (-4).dp, y = 5.dp)
        .rotate(-4f)
        .scale(0.99f)
        .shadow(8.dp, CircleShape)
        .clip(CircleShape)
        .background(
          Brush.linearGradient(
            listOf(ThemeSunflowerLight, ThemeSunflowerBg)
          )
        )
    )

    Box(
      modifier = Modifier
        .size(cardDiameter)
        .offset(x = 2.dp, y = 3.dp)
        .rotate(2f)
        .scale(0.995f)
        .shadow(6.dp, CircleShape)
        .clip(CircleShape)
        .background(
          Brush.linearGradient(
            listOf(ThemePeachLight, ThemePeachBg)
          )
        )
    )

    // Active Top Card
    Box(
      modifier = Modifier
        .size(cardDiameter)
        .graphicsLayer {
          this.rotationY = rotationY
          cameraDistance = 14f * density
        }
        .shadow(
          elevation = if (isReadyToFlip && !isFlipped) 16.dp else 10.dp,
          shape = CircleShape,
          spotColor = if (isReadyToFlip && !isFlipped) AccentSky else Color(0x33302010)
        )
        .clip(CircleShape)
        .clickable(
          interactionSource = remember { MutableInteractionSource() },
          indication = ripple(bounded = true),
          onClick = onClick
        ),
      contentAlignment = Alignment.Center
    ) {
      if (rotationY <= 90f) {
        // Back of card
        CardBackView(isReadyToFlip = isReadyToFlip)
      } else {
        // Front of card (mirrored rotationY)
        Box(
          modifier = Modifier
            .fillMaxSize()
            .graphicsLayer { this.rotationY = 180f }
        ) {
          if (card != null) {
            CardFrontView(
              card = card,
              cardNumber = cardNumber,
              totalCards = totalCards,
              activePlayerName = activePlayerName,
              partnerName = partnerName
            )
          }
        }
      }
    }
  }
}

@Composable
private fun CardBackView(isReadyToFlip: Boolean) {
  Box(
    modifier = Modifier
      .fillMaxSize()
      .border(
        width = if (isReadyToFlip) 3.5.dp else 2.dp,
        color = if (isReadyToFlip) AccentSky else Color(0x66FFFFFF),
        shape = CircleShape
      ),
    contentAlignment = Alignment.Center
  ) {
    PashutLogoView(
      modifier = Modifier.fillMaxSize(),
      size = 295.dp,
      showTopSubtitle = true
    )
  }
}

@Composable
private fun CardFrontView(
  card: CoupleCard,
  cardNumber: Int,
  totalCards: Int,
  activePlayerName: String,
  partnerName: String
) {
  val themeColors = when (card.theme) {
    CardTheme.SUNFLOWER -> listOf(ThemeSunflowerLight, ThemeSunflowerBg, ThemeSunflowerDark)
    CardTheme.PEACH -> listOf(ThemePeachLight, ThemePeachBg, ThemePeachDark)
    CardTheme.MINT -> listOf(ThemeMintLight, ThemeMintBg, ThemeMintDark)
    CardTheme.LILAC -> listOf(ThemeLilacLight, ThemeLilacBg, ThemeLilacDark)
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(
        Brush.radialGradient(
          colors = themeColors,
          radius = 480f
        )
      )
      .border(2.dp, Color(0x99FFFFFF), CircleShape)
      .padding(14.dp),
    contentAlignment = Alignment.Center
  ) {
    // Inner white octagonal/rounded frame (authentic to the wooden box photo)
    Box(
      modifier = Modifier
        .fillMaxWidth(0.92f)
        .fillMaxHeight(0.80f)
        .shadow(6.dp, RoundedCornerShape(26.dp))
        .clip(RoundedCornerShape(26.dp))
        .background(Color(0xF6FFFFFF))
        .border(1.5.dp, Color(0x66D8C8B5), RoundedCornerShape(26.dp))
        .padding(horizontal = 14.dp, vertical = 12.dp),
      contentAlignment = Alignment.Center
    ) {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
      ) {
        // Badges Row
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          val isShared = card.type == CardType.SHARED
          val badgeBg = if (isShared) Color(0xFFCCFBF1) else Color(0xFFFEF3C7)
          val badgeBorder = if (isShared) Color(0xFF5EEAD4) else Color(0xFFFDE68A)
          val badgeText = if (isShared) Color(0xFF0F766E) else Color(0xFF92400E)

          Surface(
            shape = RoundedCornerShape(12.dp),
            color = badgeBg,
            border = androidx.compose.foundation.BorderStroke(1.dp, badgeBorder)
          ) {
            Text(
              text = if (isShared) "משימה משותפת 🤝" else "משימה אישית ✨",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = badgeText,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
            )
          }

          Text(
            text = "$cardNumber/$totalCards",
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            color = InkLight
          )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Target Assignee text
        Text(
          text = if (card.type == CardType.SHARED) {
            "לשניכם יחד: $activePlayerName ו$partnerName"
          } else {
            "המשימה של: $activePlayerName"
          },
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          color = AccentSky,
          textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Title
        Text(
          text = card.title,
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold,
          fontFamily = FontFamily.Serif,
          color = InkDark,
          textAlign = TextAlign.Center,
          lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Body Text
        Text(
          text = card.text,
          fontSize = 12.5.sp,
          fontWeight = FontWeight.Normal,
          color = InkDark,
          textAlign = TextAlign.Center,
          lineHeight = 17.sp,
          modifier = Modifier.padding(horizontal = 4.dp)
        )
      }
    }
  }
}
