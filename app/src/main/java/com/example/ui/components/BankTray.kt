package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GemColor
import com.example.ui.theme.InkDark
import com.example.ui.theme.InkMuted

@Composable
fun BankTray(
  stonesCount: Int,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .shadow(4.dp, CircleShape)
      .clip(CircleShape)
      .background(
        Brush.linearGradient(
          colors = listOf(Color(0xFFFFFDF8), Color(0xFFF3ECE0))
        )
      )
      .border(1.dp, Color(0x449E8268), CircleShape)
      .padding(horizontal = 14.dp, vertical = 6.dp)
      .testTag("bank_tray"),
    contentAlignment = Alignment.Center
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Column(horizontalAlignment = Alignment.End) {
        Text(
          text = "הקופה",
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold,
          color = InkMuted,
          lineHeight = 11.sp
        )
        Text(
          text = "$stonesCount אבנים טובות",
          fontSize = 12.sp,
          fontWeight = FontWeight.ExtraBold,
          color = InkDark,
          lineHeight = 13.sp
        )
      }

      // Visual representative stones
      Row(
        horizontalArrangement = Arrangement.spacedBy((-4).dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        val countToShow = stonesCount.coerceAtMost(6)
        for (i in 0 until countToShow) {
          val gem = GemColor.fromIndex(i)
          GlassGemView(
            gemColor = gem,
            size = 18.dp
          )
        }
      }
    }
  }
}
