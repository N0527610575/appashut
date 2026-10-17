package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun DiceView(
  value: Int?,
  isRolling: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  size: Dp = 48.dp
) {
  val infiniteTransition = rememberInfiniteTransition(label = "diceRoll")

  val animatedRotation by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 360f,
    animationSpec = infiniteRepeatable(
      animation = tween(280, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "rollRotation"
  )

  val animatedJump by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = -16f,
    animationSpec = infiniteRepeatable(
      animation = tween(140, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "rollJump"
  )

  val rotation = if (isRolling) animatedRotation else 0f
  val offsetY = if (isRolling) animatedJump else 0f
  val displayValue = if (isRolling) (1..6).random() else (value ?: 1)

  Box(
    modifier = modifier
      .size(size)
      .offset(y = offsetY.dp)
      .rotate(rotation)
      .clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = ripple(bounded = false, radius = 32.dp),
        role = Role.Button,
        onClick = onClick
      )
      .testTag("die_button"),
    contentAlignment = Alignment.Center
  ) {
    // 3D Die Cube Canvas
    Canvas(
      modifier = Modifier
        .size(size - 6.dp)
        .shadow(6.dp, RoundedCornerShape(10.dp), ambientColor = Color(0x33203040))
        .clip(RoundedCornerShape(10.dp))
    ) {
      val w = this.size.width
      val h = this.size.height

      // 1. Die Body Gradient (Glossy White with slight bevel)
      drawRoundRect(
        brush = Brush.linearGradient(
          colors = listOf(Color(0xFFFFFFFF), Color(0xFFF6FAFC), Color(0xFFE2ECF2)),
          start = Offset(0f, 0f),
          end = Offset(w, h)
        ),
        size = Size(w, h),
        cornerRadius = CornerRadius(16f, 16f)
      )

      // 2. Inner border / Chamfer edge
      drawRoundRect(
        color = Color(0xFFC8D9E2),
        size = Size(w, h),
        cornerRadius = CornerRadius(16f, 16f),
        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.5f)
      )

      // 3. Top-left bevel highlight
      drawRoundRect(
        color = Color(0x88FFFFFF),
        topLeft = Offset(2f, 2f),
        size = Size(w - 4f, h * 0.35f),
        cornerRadius = CornerRadius(12f, 12f)
      )

      // 4. Pips (Turquoise / Sky-blue dots matching the authentic physical die)
      val pipRadius = w * 0.09f
      val pipCenterColor = Color(0xFF38BDF8)
      val pipOuterColor = Color(0xFF0284C7)

      fun drawPip(cx: Float, cy: Float, radius: Float = pipRadius) {
        // Subtle inset shadow
        drawCircle(
          color = Color(0x33002538),
          radius = radius * 1.15f,
          center = Offset(cx, cy + 1f)
        )
        // 3D Radial Pip
        drawCircle(
          brush = Brush.radialGradient(
            colors = listOf(pipCenterColor, pipOuterColor),
            center = Offset(cx - radius * 0.25f, cy - radius * 0.25f),
            radius = radius * 1.2f
          ),
          radius = radius,
          center = Offset(cx, cy)
        )
        // Specular white pip glint
        drawCircle(
          color = Color(0xAAFFFFFF),
          radius = radius * 0.3f,
          center = Offset(cx - radius * 0.35f, cy - radius * 0.35f)
        )
      }

      val left = w * 0.27f
      val center = w * 0.50f
      val right = w * 0.73f

      val top = h * 0.27f
      val middle = h * 0.50f
      val bottom = h * 0.73f

      when (displayValue) {
        1 -> {
          // Large turquoise pip in center for 1 (as seen in physical die)
          drawPip(center, middle, pipRadius * 1.35f)
        }
        2 -> {
          drawPip(left, top)
          drawPip(right, bottom)
        }
        3 -> {
          drawPip(left, top)
          drawPip(center, middle)
          drawPip(right, bottom)
        }
        4 -> {
          drawPip(left, top)
          drawPip(right, top)
          drawPip(left, bottom)
          drawPip(right, bottom)
        }
        5 -> {
          drawPip(left, top)
          drawPip(right, top)
          drawPip(center, middle)
          drawPip(left, bottom)
          drawPip(right, bottom)
        }
        6 -> {
          drawPip(left, top)
          drawPip(right, top)
          drawPip(left, middle)
          drawPip(right, middle)
          drawPip(left, bottom)
          drawPip(right, bottom)
        }
      }
    }
  }
}
