package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.model.GameSettings
import com.example.model.SeatingMode
import com.example.ui.theme.AccentSky
import com.example.ui.theme.InkDark
import com.example.ui.theme.InkMuted

@Composable
fun SettingsDialog(
  settings: GameSettings,
  onDismiss: () -> Unit,
  onSaveNames: (String, String) -> Unit,
  onSaveSettings: (Boolean, Boolean, Boolean, SeatingMode) -> Unit,
  onOpenAddCard: () -> Unit,
  onResetGame: () -> Unit
) {
  var p1Name by remember { mutableStateOf(settings.player1Name) }
  var p2Name by remember { mutableStateOf(settings.player2Name) }
  var withDice by remember { mutableStateOf(settings.withDice) }
  var withStones by remember { mutableStateOf(settings.withStones) }
  var soundEnabled by remember { mutableStateOf(settings.soundEnabled) }
  var seatingMode by remember { mutableStateOf(settings.seatingMode) }

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
        .testTag("settings_dialog")
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
            onClick = {
              onSaveNames(p1Name, p2Name)
              onSaveSettings(withDice, withStones, soundEnabled, seatingMode)
              onDismiss()
            },
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

          Text(
            text = "הגדרות המשחק",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif,
            color = InkDark
          )
        }

        Spacer(modifier = Modifier.height(14.dp))
        HorizontalDivider(color = Color(0x33B4A087))
        Spacer(modifier = Modifier.height(14.dp))

        // Auto-save banner
        Surface(
          shape = RoundedCornerShape(16.dp),
          color = Color(0xDDF0FDF4),
          border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBBF7D0)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
          ) {
            Column(horizontalAlignment = Alignment.End, modifier = Modifier.weight(1f)) {
              Text(
                text = "שמירה אוטומטית פעילה",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF166534)
              )
              Text(
                text = "השמות, האבנים, אופן הישיבה והקלפים נשמרים במכשיר",
                fontSize = 10.sp,
                color = Color(0xFF15803D)
              )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "✓", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF166534))
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Seating Mode (New requested feature: Sit across from each other vs side-by-side!)
        Text(
          text = "אופן הישיבה סביב המכשיר:",
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          color = InkDark
        )

        Spacer(modifier = Modifier.height(6.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          // Face to Face Option
          Surface(
            shape = RoundedCornerShape(14.dp),
            color = if (seatingMode == SeatingMode.FACE_TO_FACE) Color(0xFFE0F2FE) else Color(0xDFFFFFFF),
            border = androidx.compose.foundation.BorderStroke(
              width = if (seatingMode == SeatingMode.FACE_TO_FACE) 2.dp else 1.dp,
              color = if (seatingMode == SeatingMode.FACE_TO_FACE) AccentSky else Color(0x33D1D5DB)
            ),
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(14.dp))
              .clickable {
                seatingMode = SeatingMode.FACE_TO_FACE
                onSaveSettings(withDice, withStones, soundEnabled, SeatingMode.FACE_TO_FACE)
              }
              .padding(8.dp)
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text(text = "🔄 זה מול זו", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = InkDark)
              Text(
                text = "משני צידי השולחן (שחקן 2 הפוך ב-180°)",
                fontSize = 9.5.sp,
                color = InkMuted,
                textAlign = TextAlign.Center,
                lineHeight = 12.sp
              )
            }
          }

          // Side by Side Option
          Surface(
            shape = RoundedCornerShape(14.dp),
            color = if (seatingMode == SeatingMode.SIDE_BY_SIDE) Color(0xFFE0F2FE) else Color(0xDFFFFFFF),
            border = androidx.compose.foundation.BorderStroke(
              width = if (seatingMode == SeatingMode.SIDE_BY_SIDE) 2.dp else 1.dp,
              color = if (seatingMode == SeatingMode.SIDE_BY_SIDE) AccentSky else Color(0x33D1D5DB)
            ),
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(14.dp))
              .clickable {
                seatingMode = SeatingMode.SIDE_BY_SIDE
                onSaveSettings(withDice, withStones, soundEnabled, SeatingMode.SIDE_BY_SIDE)
              }
              .padding(8.dp)
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text(text = "👥 זה לצד זו", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = InkDark)
              Text(
                text = "שני השחקנים יושבים יחד באותו כיוון",
                fontSize = 9.5.sp,
                color = InkMuted,
                textAlign = TextAlign.Center,
                lineHeight = 12.sp
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Player Names
        Text(
          text = "שמות השחקנים",
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          color = InkDark
        )

        Spacer(modifier = Modifier.height(6.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          OutlinedTextField(
            value = p2Name,
            onValueChange = {
              p2Name = it
              onSaveNames(p1Name, it)
            },
            label = {
              Text(
                if (seatingMode == SeatingMode.FACE_TO_FACE) "שחקן 2 (ממול)" else "שחקן 2 (שמאל)",
                fontSize = 11.sp
              )
            },
            singleLine = true,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = AccentSky,
              focusedLabelColor = AccentSky
            )
          )

          OutlinedTextField(
            value = p1Name,
            onValueChange = {
              p1Name = it
              onSaveNames(it, p2Name)
            },
            label = {
              Text(
                if (seatingMode == SeatingMode.FACE_TO_FACE) "שחקן 1 (אצלי)" else "שחקן 1 (ימין)",
                fontSize = 11.sp
              )
            },
            singleLine = true,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = AccentSky,
              focusedLabelColor = AccentSky
            )
          )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Toggles Container
        Surface(
          shape = RoundedCornerShape(18.dp),
          color = Color(0xDFFFFFFF),
          border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x33D1D5DB)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            // Dice toggle
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Switch(
                checked = withDice,
                onCheckedChange = {
                  withDice = it
                  onSaveSettings(it, withStones, soundEnabled, seatingMode)
                },
                colors = SwitchDefaults.colors(
                  checkedThumbColor = Color.White,
                  checkedTrackColor = AccentSky
                )
              )
              Column(horizontalAlignment = Alignment.End) {
                Text(
                  text = "משחק עם קוביות",
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold,
                  color = InkDark
                )
                Text(
                  text = "המספר הקטן יותר מרים קלף",
                  fontSize = 11.sp,
                  color = InkMuted
                )
              }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0x1F000000))

            // Stones toggle
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Switch(
                checked = withStones,
                onCheckedChange = {
                  withStones = it
                  onSaveSettings(withDice, it, soundEnabled, seatingMode)
                },
                colors = SwitchDefaults.colors(
                  checkedThumbColor = Color.White,
                  checkedTrackColor = AccentSky
                )
              )
              Column(horizontalAlignment = Alignment.End) {
                Text(
                  text = "משחק עם אבנים טובות וקופה",
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold,
                  color = InkDark
                )
                Text(
                  text = "ספירת אבנים, פרגון ויעד 10 אבנים",
                  fontSize = 11.sp,
                  color = InkMuted
                )
              }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0x1F000000))

            // Sound toggle
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Switch(
                checked = soundEnabled,
                onCheckedChange = {
                  soundEnabled = it
                  onSaveSettings(withDice, withStones, it, seatingMode)
                },
                colors = SwitchDefaults.colors(
                  checkedThumbColor = Color.White,
                  checkedTrackColor = AccentSky
                )
              )
              Column(horizontalAlignment = Alignment.End) {
                Text(
                  text = "צלילים ואפקטים קוליים",
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold,
                  color = InkDark
                )
                Text(
                  text = "נקישת גולות זכוכית, רשרוש קלפים וקוביות",
                  fontSize = 11.sp,
                  color = InkMuted
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Custom Card Button
        Button(
          onClick = {
            onSaveNames(p1Name, p2Name)
            onSaveSettings(withDice, withStones, soundEnabled, seatingMode)
            onOpenAddCard()
          },
          colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF2C2520)
          ),
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = Color.White)
          Spacer(modifier = Modifier.width(6.dp))
          Text(text = "הוספת קלף אישי לחפיסה ✨", fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Reset Game Button
        OutlinedButton(
          onClick = {
            onResetGame()
            onDismiss()
          },
          colors = ButtonDefaults.outlinedButtonColors(
            contentColor = Color(0xFFB91C1C)
          ),
          border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFECACA)),
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Icon(imageVector = Icons.Default.Refresh, contentDescription = null, tint = Color(0xFFB91C1C))
          Spacer(modifier = Modifier.width(6.dp))
          Text(text = "איפוס משחק והחזרת 20 האבנים לקופה", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}
