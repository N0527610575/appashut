package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.model.GemColor

@Composable
fun GlassGemView(
  gemColor: GemColor,
  modifier: Modifier = Modifier,
  size: Dp = 22.dp
) {
  Box(modifier = modifier.size(size)) {
    Canvas(modifier = Modifier.matchParentSize()) {
      val w = this.size.width
      val h = this.size.height

      // 1. Drop shadow
      drawOval(
        color = Color(0x33101820),
        topLeft = Offset(w * 0.04f, h * 0.12f),
        size = Size(w * 0.92f, h * 0.88f)
      )

      // 2. Base 3D Radial Sphere Body
      drawOval(
        brush = Brush.radialGradient(
          colors = listOf(gemColor.light, gemColor.primary, gemColor.dark),
          center = Offset(w * 0.35f, h * 0.35f),
          radius = w * 0.75f
        ),
        topLeft = Offset(0f, 0f),
        size = Size(w, h)
      )

      // 3. Inner shadow rim
      drawOval(
        brush = Brush.verticalGradient(
          colors = listOf(Color(0x66FFFFFF), Color.Transparent, Color(0x44000000)),
          startY = 0f,
          endY = h
        ),
        topLeft = Offset(0f, 0f),
        size = Size(w, h)
      )

      // 4. Glossy Specular Light Reflection (Top-Left)
      drawOval(
        brush = Brush.verticalGradient(
          colors = listOf(Color(0xEEFFFFFF), Color(0x33FFFFFF)),
          startY = h * 0.12f,
          endY = h * 0.42f
        ),
        topLeft = Offset(w * 0.18f, h * 0.14f),
        size = Size(w * 0.44f, h * 0.28f)
      )

      // 5. Subtle Secondary Caustic Glow (Bottom-Right)
      drawOval(
        color = Color(0x55FFFFFF),
        topLeft = Offset(w * 0.58f, h * 0.66f),
        size = Size(w * 0.26f, h * 0.18f)
      )
    }
  }
}
