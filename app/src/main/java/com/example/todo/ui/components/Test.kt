package com.example.todo.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

@Composable
fun Ring(fraction: Float, modifier: Modifier = Modifier) {
    val startAngle = 30f
    val sweepAngle = 300f
    Canvas(
        modifier = modifier
            .drawBehind {
                val brush = Brush.radialGradient(
                    colorStops = arrayOf(
                        0f to Color.Transparent,
                        0.5f to Color.Transparent,
                        0.7f to Color(0xffd6deee),
                        0.9f to Color.Transparent,
                        1f to Color.Transparent
                    ),
                    center = center + Offset(10f, 10f)
                )
                drawCircle(brush)
            }
            .padding(16.dp)
            .rotate(90f)
    ) {
        val brush = Brush.sweepGradient(colors = listOf(Color.Red, Color.Yellow, Color.Green))
        drawArc(
            Color(0xfff5f6fb),
            0f,
            360f,
            useCenter = false,
            style = Stroke(width = 40f, cap = StrokeCap.Round)
        )
        drawArc(
            brush,
            startAngle,
            sweepAngle,
            useCenter = false,
            style = Stroke(width = 10f, cap = StrokeCap.Round)
        )

        val angle = (startAngle + sweepAngle * fraction).toDouble()
        val radius = (size.height / 2)
        val x = radius + (radius * cos(Math.toRadians(angle))).toFloat()
        val y = radius + (radius * sin(Math.toRadians(angle))).toFloat()

        val color = if (fraction > 0.5)
            getColorBetween(Color.Yellow, Color.Green, fraction = (1f - fraction) * 2)
        else getColorBetween(Color.Red, Color.Yellow, fraction = fraction * 2)

        drawCircle(
            Color.White,
            radius = 16f,
            center = Offset(x, y)
        )
        drawCircle(
            color,
            radius = 12f,
            center = Offset(x, y)
        )
    }
}

private fun getColorBetween(color1: Color, color2: Color, fraction: Float): Color {
    val (red1, green1, blue1) = color1
    val (red2, green2, blue2) = color2
    val red = min(red1, red2) + abs(red1 - red2) * fraction
    val green = min(green1, green2) + abs(green1 - green2) * fraction
    val blue = min(blue1, blue2) + abs(blue1 - blue2) * fraction
    return Color(red, green, blue)
}

@Preview
@Composable
private fun RingPreview() {

    Ring(
        modifier = Modifier
            .size(100.dp)
            .background(Color.White),
        fraction = 0.6f
    )
}