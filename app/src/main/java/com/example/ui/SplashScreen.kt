package com.example.ui

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.InkDark
import com.example.ui.theme.InkMuted

@Composable
fun SplashScreen(
  onEnterGame: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .fillMaxSize()
      .background(
        Brush.radialGradient(
          colors = listOf(Color(0xFFFFFFFF), Color(0xFFFAF6F0), Color(0xFFF1E9DC)),
          radius = 900f
        )
      )
      .padding(24.dp)
      .testTag("splash_screen"),
    contentAlignment = Alignment.Center
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center,
      modifier = Modifier.fillMaxWidth()
    ) {
      // Circular Logo Card Frame with authentic hand-drawn artwork
      Box(
        modifier = Modifier
          .size(200.dp)
          .shadow(16.dp, CircleShape)
          .clip(CircleShape)
          .clickable { onEnterGame() },
        contentAlignment = Alignment.Center
      ) {
        com.example.ui.components.PashutLogoView(
          modifier = Modifier.fillMaxSize(),
          size = 200.dp,
          showTopSubtitle = true
        )
      }

      Spacer(modifier = Modifier.height(24.dp))

      Text(
        text = "פשוט להיפגש",
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.Serif,
        color = InkDark,
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = "37 קלפי משחק • 20 אבנים טובות • קובייה וזמן איכות",
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        color = InkMuted,
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(28.dp))

      Button(
        onClick = onEnterGame,
        colors = ButtonDefaults.buttonColors(
          containerColor = Color(0xFF2C2520)
        ),
        shape = RoundedCornerShape(18.dp),
        contentPadding = PaddingValues(horizontal = 32.dp, vertical = 14.dp),
        modifier = Modifier
          .fillMaxWidth(0.78f)
          .shadow(6.dp, RoundedCornerShape(18.dp))
          .testTag("enter_game_button")
      ) {
        Text(
          text = "היכנסו למשחק הזוגי",
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold,
          color = Color(0xFFFFFBF0)
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = "השמות, האבנים והקלפים נשמרים אוטומטית במכשירכם",
        fontSize = 11.sp,
        color = InkMuted,
        textAlign = TextAlign.Center
      )
    }
  }
}
