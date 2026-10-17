package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Authentic recreation of the hand-drawn "פשוט להיפגש" logo:
 * - Warm kraft/sand paper background
 * - Top text: "אז קדימה, תהנו לכם.."
 * - "פשוט" and "להיפגש" in cyan/blue crayon
 * - Spiral winding into a heart in crimson/pink crayon
 * - Turquoise dot inside the heart
 */
@Composable
fun PashutLogoView(
  modifier: Modifier = Modifier,
  size: Dp = 190.dp,
  showTopSubtitle: Boolean = true
) {
  Box(
    modifier = modifier
      .size(size)
      .clip(CircleShape)
      .background(
        Brush.radialGradient(
          colors = listOf(
            Color(0xFFE9BD8A), // Center warm parchment
            Color(0xFFE1B078),
            Color(0xFFD69F65)  // Edge darker kraft
          ),
          center = Offset.Unspecified,
          radius = 500f
        )
      )
      .border(2.5.dp, Color(0x66FFFFFF), CircleShape),
    contentAlignment = Alignment.Center
  ) {
    // 1. Hand-drawn Spiral, Heart & Inner Dot Canvas
    Canvas(modifier = Modifier.fillMaxSize()) {
      val w = this.size.width
      val h = this.size.height

      // Subtle texture rings
      drawCircle(
        color = Color(0x187C4D1D),
        radius = w * 0.45f,
        style = Stroke(width = 1.5f)
      )

      val spiralStrokeWidth = (w * 0.038f).coerceAtLeast(4f)
      val spiralColor = Color(0xFFC73663) // Crimson/Pink crayon color from the drawing

      // Spiral Path winding inward towards center heart
      val spiralPath = Path().apply {
        // Outer loop start near bottom right
        moveTo(w * 0.88f, h * 0.58f)
        cubicTo(
          w * 0.86f, h * 0.86f,
          w * 0.60f, h * 0.94f,
          w * 0.44f, h * 0.92f
        )
        cubicTo(
          w * 0.22f, h * 0.90f,
          w * 0.12f, h * 0.72f,
          w * 0.14f, h * 0.52f
        )
        cubicTo(
          w * 0.16f, h * 0.32f,
          w * 0.34f, h * 0.22f,
          w * 0.56f, h * 0.22f
        )
        cubicTo(
          w * 0.80f, h * 0.24f,
          w * 0.88f, h * 0.44f,
          w * 0.82f, h * 0.62f
        )
        cubicTo(
          w * 0.76f, h * 0.78f,
          w * 0.58f, h * 0.84f,
          w * 0.42f, h * 0.80f
        )
        cubicTo(
          w * 0.28f, h * 0.74f,
          w * 0.26f, h * 0.56f,
          w * 0.34f, h * 0.46f
        )
        cubicTo(
          w * 0.42f, h * 0.36f,
          w * 0.64f, h * 0.38f,
          w * 0.70f, h * 0.48f
        )
        cubicTo(
          w * 0.74f, h * 0.58f,
          w * 0.66f, h * 0.70f,
          w * 0.54f, h * 0.70f
        )
        // Transition into the central heart base
        lineTo(w * 0.50f, h * 0.72f)
      }

      // Draw the main spiral line
      drawPath(
        path = spiralPath,
        color = spiralColor,
        style = Stroke(
          width = spiralStrokeWidth,
          cap = StrokeCap.Round,
          join = StrokeJoin.Round
        )
      )

      // Heart Path in the center
      val heartCenter = Offset(w * 0.49f, h * 0.66f)
      val heartScale = w * 0.0036f

      val heartPath = Path().apply {
        // Crayon Heart coordinates
        moveTo(heartCenter.x, heartCenter.y + 12f * heartScale)
        cubicTo(
          heartCenter.x - 18f * heartScale, heartCenter.y - 2f * heartScale,
          heartCenter.x - 18f * heartScale, heartCenter.y - 18f * heartScale,
          heartCenter.x - 5f * heartScale, heartCenter.y - 18f * heartScale
        )
        cubicTo(
          heartCenter.x, heartCenter.y - 18f * heartScale,
          heartCenter.x, heartCenter.y - 12f * heartScale,
          heartCenter.x, heartCenter.y - 10f * heartScale
        )
        cubicTo(
          heartCenter.x, heartCenter.y - 12f * heartScale,
          heartCenter.x, heartCenter.y - 18f * heartScale,
          heartCenter.x + 5f * heartScale, heartCenter.y - 18f * heartScale
        )
        cubicTo(
          heartCenter.x + 18f * heartScale, heartCenter.y - 18f * heartScale,
          heartCenter.x + 18f * heartScale, heartCenter.y - 2f * heartScale,
          heartCenter.x, heartCenter.y + 12f * heartScale
        )
        close()
      }

      // Heart outline
      drawPath(
        path = heartPath,
        color = Color(0xFFC73663),
        style = Stroke(
          width = spiralStrokeWidth * 1.1f,
          cap = StrokeCap.Round,
          join = StrokeJoin.Round
        )
      )

      // Turquoise center dot inside heart ("הנקודה הכחולה")
      drawCircle(
        color = Color(0xFF00A3E0),
        radius = w * 0.038f,
        center = Offset(heartCenter.x - 1f, heartCenter.y - 3f * heartScale)
      )
      // Highlight on dot
      drawCircle(
        color = Color(0x88FFFFFF),
        radius = w * 0.015f,
        center = Offset(heartCenter.x - 3f, heartCenter.y - 5f * heartScale)
      )
    }

    // 2. Hand-Drawn Words & Subtitle Overlay
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 14.dp, vertical = 10.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      // Top: "אז קדימה, תהנו לכם.." in authentic crayon red
      if (showTopSubtitle) {
        Text(
          text = "..אז קדימה, תהנו לכם",
          fontSize = (size.value * 0.068f).sp,
          fontWeight = FontWeight.Bold,
          fontFamily = FontFamily.Serif,
          color = Color(0xFFC62828),
          textAlign = TextAlign.Center,
          modifier = Modifier.padding(top = 2.dp)
        )
      } else {
        Spacer(modifier = Modifier.height(8.dp))
      }

      // Main curved blue crayon words "פשוט" and "להיפגש"
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 4.dp)
          .offset(y = (-size.value * 0.05f).dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // "להיפגש" (Left side in LTR, which is Left in RTL too)
        Text(
          text = "להיפגש",
          fontSize = (size.value * 0.13f).sp,
          fontWeight = FontWeight.Bold,
          fontFamily = FontFamily.Serif,
          color = Color(0xFF0088CC),
          letterSpacing = 1.sp,
          textAlign = TextAlign.Center,
          modifier = Modifier
            .rotate(-16f)
            .offset(x = 4.dp, y = (-2).dp)
        )

        // "פשוט" (Right side in LTR)
        Text(
          text = "פשוט",
          fontSize = (size.value * 0.14f).sp,
          fontWeight = FontWeight.Bold,
          fontFamily = FontFamily.Serif,
          color = Color(0xFF0088CC),
          letterSpacing = 1.sp,
          textAlign = TextAlign.Center,
          modifier = Modifier
            .rotate(18f)
            .offset(x = (-4).dp, y = (-2).dp)
        )
      }

      // Bottom Spacer for balanced breathing room
      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}
