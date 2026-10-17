package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.GemColor
import com.example.ui.components.GlassGemView
import com.example.ui.theme.AccentGold
import com.example.ui.theme.AccentSky
import com.example.ui.theme.InkDark

@Composable
fun VictoryDialog(
  winnerName: String,
  partnerName: String,
  stones: List<GemColor>,
  onContinue: () -> Unit,
  onNewRound: () -> Unit
) {
  Dialog(
    onDismissRequest = onContinue,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth(0.92f)
        .padding(vertical = 24.dp)
        .clip(RoundedCornerShape(26.dp))
        .background(Color(0xFFFFFDF9))
        .border(2.dp, AccentGold, RoundedCornerShape(26.dp))
        .padding(22.dp)
        .testTag("victory_dialog")
    ) {
      Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Crown Icon Badge
        Box(
          modifier = Modifier
            .size(68.dp)
            .shadow(6.dp, CircleShape)
            .clip(CircleShape)
            .background(Color(0xFFFEF3C7))
            .border(2.dp, Color(0xFFFDE68A), CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Text(text = "👑", fontSize = 34.sp)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
          text = "כל הכבוד, $winnerName!",
          fontSize = 22.sp,
          fontWeight = FontWeight.Bold,
          fontFamily = FontFamily.Serif,
          color = InkDark,
          textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
          text = "השגת 10 אבנים טובות! זה הזמן שלך לבקש בקשה מיוחדת מ$partnerName ✨",
          fontSize = 14.sp,
          fontWeight = FontWeight.Normal,
          color = Color(0xFF4A3E36),
          textAlign = TextAlign.Center,
          lineHeight = 20.sp,
          modifier = Modifier.padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Display the 10 gems in a clean container
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF7F3EB))
            .padding(horizontal = 12.dp, vertical = 8.dp),
          horizontalArrangement = Arrangement.Center,
          verticalAlignment = Alignment.CenterVertically
        ) {
          stones.take(10).forEach { gem ->
            GlassGemView(
              gemColor = gem,
              size = 20.dp,
              modifier = Modifier.padding(horizontal = 2.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          OutlinedButton(
            onClick = onContinue,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.weight(1f)
          ) {
            Text(text = "המשך משחק", fontSize = 12.sp, fontWeight = FontWeight.Bold)
          }

          Button(
            onClick = onNewRound,
            colors = ButtonDefaults.buttonColors(
              containerColor = AccentSky
            ),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.weight(1f)
          ) {
            Text(text = "סבב חדש (איפוס)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}
