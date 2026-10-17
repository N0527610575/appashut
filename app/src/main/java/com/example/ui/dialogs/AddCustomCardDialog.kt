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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.CardType
import com.example.ui.theme.AccentSky
import com.example.ui.theme.InkDark

@Composable
fun AddCustomCardDialog(
  onDismiss: () -> Unit,
  onSaveCard: (String, String, CardType) -> Unit
) {
  var title by remember { mutableStateOf("") }
  var text by remember { mutableStateOf("") }
  var cardType by remember { mutableStateOf(CardType.INDIVIDUAL) }

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth(0.92f)
        .padding(vertical = 24.dp)
        .clip(RoundedCornerShape(26.dp))
        .background(Color(0xFFFFFDF9))
        .border(1.dp, Color(0x55B4A087), RoundedCornerShape(26.dp))
        .padding(20.dp)
        .testTag("add_custom_card_dialog")
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.End
      ) {
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
            Icon(imageVector = Icons.Default.Close, contentDescription = "סגור", tint = InkDark)
          }

          Text(
            text = "הוספת קלף אישי",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif,
            color = InkDark
          )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Type selection
        Text(text = "סוג המשימה:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = InkDark)
        Spacer(modifier = Modifier.height(6.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          FilterChip(
            selected = cardType == CardType.SHARED,
            onClick = { cardType = CardType.SHARED },
            label = { Text("משותפת 🤝", fontSize = 12.sp) },
            modifier = Modifier.weight(1f)
          )
          FilterChip(
            selected = cardType == CardType.INDIVIDUAL,
            onClick = { cardType = CardType.INDIVIDUAL },
            label = { Text("אישית ✨", fontSize = 12.sp) },
            modifier = Modifier.weight(1f)
          )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Title
        Text(text = "כותרת הקלף:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = InkDark)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          placeholder = { Text("למשל: זיכרון מתוק שלנו", fontSize = 12.sp) },
          singleLine = true,
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(14.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = AccentSky
          )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Text
        Text(text = "תוכן המשימה או השאלה:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = InkDark)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
          value = text,
          onValueChange = { text = it },
          placeholder = { Text("כתבו כאן את המשימה הזוגית...", fontSize = 12.sp) },
          minLines = 3,
          maxLines = 5,
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(14.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = AccentSky
          )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
          onClick = {
            if (text.isNotBlank()) {
              onSaveCard(title, text, cardType)
              onDismiss()
            }
          },
          enabled = text.isNotBlank(),
          colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF2C2520)
          ),
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(text = "הוספה לחפיסת הקלפים ✨", fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}
