package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.InkDark
import com.example.ui.theme.InkMuted

@Composable
fun RulesDialog(
  onDismiss: () -> Unit
) {
  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth(0.92f)
        .padding(vertical = 24.dp)
        .clip(RoundedCornerShape(26.dp))
        .background(
          Brush.radialGradient(
            colors = listOf(Color(0xFFFFFDF9), Color(0xFFFBF7EF), Color(0xFFF4EDE1)),
            radius = 600f
          )
        )
        .border(1.dp, Color(0x55B4A087), RoundedCornerShape(26.dp))
        .padding(20.dp)
        .testTag("rules_dialog")
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.End
      ) {
        // Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          IconButton(
            onClick = onDismiss,
            modifier = Modifier
              .size(32.dp)
              .clip(CircleShape)
              .background(Color(0x33000000))
          ) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "סגור",
              tint = InkDark,
              modifier = Modifier.size(18.dp)
            )
          }

          Column(horizontalAlignment = Alignment.End) {
            Text(
              text = "פשוט להיפגש",
              fontSize = 20.sp,
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily.Serif,
              color = InkDark
            )
            Text(
              text = "37 קלפי משחק • 20 אבנים טובות • קובייה",
              fontSize = 11.sp,
              fontWeight = FontWeight.Medium,
              color = InkMuted
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))
        HorizontalDivider(color = Color(0x33B4A087))
        Spacer(modifier = Modifier.height(14.dp))

        Text(
          text = "וזה מתחיל ככה...",
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold,
          color = InkDark,
          textAlign = TextAlign.Right
        )

        Spacer(modifier = Modifier.height(10.dp))

        RuleItem(
          icon = "🎲",
          title = "הטלת קוביות",
          desc = "בכל תור שניכם זורקים את הקוביה – מי שיצא לו מספר קטן יותר מרים קלף."
        )

        RuleItem(
          icon = "💎",
          title = "עשית מה שכתוב בקלף?",
          desc = "קיבלת אבן טובה מהקופה."
        )

        RuleItem(
          icon = "🤍",
          title = "בחרת שלא לעשות?",
          desc = "זה בסדר, אבל אתה מחזיר אבן טובה לקופה."
        )

        RuleItem(
          icon = "✨",
          title = "פרגון הדדי",
          desc = "מותר לפרגן לבן זוגך אבן טובה משלך אם נהנית במיוחד ממילוי המשימה שלו (בלחיצה על ערימת האבנים שלך!)."
        )

        RuleItem(
          icon = "👑",
          title = "יעד 10 אבנים",
          desc = "מי שמשיג 10 אבנים זוכה לבקש מהשני בקשה מיוחדת."
        )

        RuleItem(
          icon = "🤝",
          title = "משימות משותפות",
          desc = "המשימות המשותפות מזכות את שניכם באבן טובה מהקופה."
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
          text = "פשוט, לא? אז קדימה, תהנו לכם..",
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold,
          fontFamily = FontFamily.Serif,
          color = InkDark,
          textAlign = TextAlign.Center,
          modifier = Modifier.fillMaxWidth()
        )
      }
    }
  }
}

@Composable
private fun RuleItem(
  icon: String,
  title: String,
  desc: String
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 5.dp),
    horizontalArrangement = Arrangement.End,
    verticalAlignment = Alignment.Top
  ) {
    Column(
      modifier = Modifier
        .weight(1f)
        .padding(end = 8.dp),
      horizontalAlignment = Alignment.End
    ) {
      Text(
        text = title,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        color = InkDark,
        textAlign = TextAlign.Right
      )
      Text(
        text = desc,
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        color = Color(0xFF4A3E36),
        textAlign = TextAlign.Right,
        lineHeight = 16.sp
      )
    }

    Text(
      text = icon,
      fontSize = 18.sp,
      modifier = Modifier.padding(top = 1.dp)
    )
  }
}
