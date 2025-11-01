package malta.pedro.speedometer.features.presentation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import malta.pedro.speedometer.features.presentation.styles.SpeedometerStyle
import malta.pedro.speedometer.features.presentation.styles.vw60sStyle
import malta.pedro.speedometer.features.presentation.styles.vw70sStyle
import malta.pedro.speedometer.features.presentation.styles.vw80sStyle
import malta.pedro.speedometer.features.presentation.styles.vw90sStyle
import malta.pedro.speedometer.features.presentation.styles.vwClassic40sStyle
import malta.pedro.speedometer.features.presentation.styles.vwClassicStyle
import malta.pedro.speedometer.features.presentation.styles.vwRetro50sStyle
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin


@Composable
fun VintageSpeedometer(
    modifier: Modifier = Modifier.size(240.dp),
    currentSpeed: Float,
    maxSpeed: Int = 140,
    style: SpeedometerStyle,
) {
    val displayed = animateFloatAsState(
        targetValue = currentSpeed.coerceIn(0f, maxSpeed.toFloat()),
        animationSpec = spring(
            stiffness = Spring.StiffnessLow,
            dampingRatio = 0.85f
        )
    )

    val textMeasurer = rememberTextMeasurer()

    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter
    ) {
        Canvas(modifier = Modifier.fillMaxWidth().height(400.dp)) {
            val w = size.width
            val h = size.height
            val radius = size.minDimension / 2f
            val center = Offset(w / 2f, h / 2f)

            // Rim / housing
            drawCircle(color = style.rimColor, radius = radius)
            drawCircle(color = Color(0xFF111111), radius = radius * 0.94f)

            // Outer ring (chrome)
            drawCircle(
                brush = Brush.radialGradient(listOf(Color(0xFFd6d6d6), Color(0xFF7f7f7f))),
                radius = radius * 0.88f,
                center = center
            )

            // Face
            drawCircle(color = style.faceColor, radius = radius * 0.84f, center = center)

            // Glass highlight
            drawArc(
                color = Color.White.copy(alpha = 0.08f),
                startAngle = -140f,
                sweepAngle = 60f,
                useCenter = false,
                topLeft = Offset(center.x - radius * 0.7f, center.y - radius * 0.9f),
                size = Size(radius * 1.4f, radius * 1.4f),
                style = Stroke(width = radius * 0.02f)
            )

            // Ticks and numbers
            val startAngle = 140f
            val sweep = 260f
            val majorTickCount = maxSpeed / 10 + 1

            for (i in 0..<majorTickCount) {
                val angle =
                    (startAngle + (sweep / (majorTickCount - 1)) * i).toDouble() * PI / 180.0
                val inner = Offset(
                    center.x + (radius * 0.6f) * cos(angle).toFloat(),
                    center.y + (radius * 0.6f) * sin(angle).toFloat()
                )
                val outer = Offset(
                    center.x + (radius * 0.78f) * cos(angle).toFloat(),
                    center.y + (radius * 0.78f) * sin(angle).toFloat()
                )
                drawLine(
                    color = style.tickColor,
                    start = inner,
                    end = outer,
                    strokeWidth = if (i % 2 == 0) radius * style.majorTickWidthFraction else radius * style.minorTickWidthFraction,
                    cap = StrokeCap.Round
                )

                if (i % 2 == 0) {
                    val label = ((i * (maxSpeed / (majorTickCount - 1)))).toString()
                    val textLayout = textMeasurer.measure(
                        text = AnnotatedString(label),
                        style = TextStyle(
                            color = style.tickColor,
                            fontSize = (radius * style.majorFontSizeFraction).sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            fontFamily = style.fontFamily
                        )
                    )

                    val tx =
                        center.x + (radius * 0.46f) * cos(angle).toFloat() - textLayout.size.width / 2
                    val ty =
                        center.y + (radius * 0.46f) * sin(angle).toFloat() - textLayout.size.height / 2
                    drawText(textLayout, topLeft = Offset(tx, ty))
                }
            }

            // Needle
            val percent = displayed.value / maxSpeed
            val needleAngle = startAngle + sweep * percent
            rotate(degrees = needleAngle, pivot = center) {
                drawLine(
                    color = style.needleColor,
                    start = center + Offset(-radius * 0.02f, 0f),
                    end = center + Offset(radius * 0.62f, 0f),
                    strokeWidth = radius * style.needleWidthFraction,
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = Color.Black,
                    start = center + Offset(-radius * 0.02f, 0f),
                    end = center + Offset(radius * 0.66f, 0f),
                    strokeWidth = radius * style.pointerWidthFraction,
                    cap = StrokeCap.Round
                )
            }

            // Central hub
            drawCircle(color = style.centralHubColor, radius = radius * 0.09f, center = center)
            drawCircle(color = style.centerAccentColor, radius = radius * 0.06f, center = center)

            // Odometer text
            val speedText = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = style.tickColor,
                        fontSize = (radius * style.speedFontSizeFraction).sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = style.fontFamily
                    )
                ) {
                    append(displayed.value.toInt().toString())
                }
                append(" ")
                withStyle(
                    style = SpanStyle(
                        color = style.tickColor,
                        fontSize = (radius * style.unitFontSizeFraction).sp,
                        fontWeight = FontWeight.Medium
                    )
                ) {
                    append("km/h")
                }
            }

            val textLayout = textMeasurer.measure(
                text = speedText,
                style = TextStyle(textAlign = TextAlign.Center)
            )

            drawText(
                textLayoutResult = textLayout,
                topLeft = Offset(
                    center.x - textLayout.size.width / 2,
                    center.y + radius * 0.6f - textLayout.size.height / 2
                )
            )
        }
    }
}

@Composable
fun VintageSpeedometerOld(
    modifier: Modifier = Modifier.size(240.dp),
    currentSpeed: Float,
    maxSpeed: Int = 120,
    needleColor: Color = Color(0xFFb21807), // vintage red
    rimColor: Color = Color(0xFF2b2b2b),
    faceColor: Color = Color(0xFFf5efe6), // cream face
) {
    val displayed = animateFloatAsState(
        targetValue = currentSpeed.coerceIn(0f, maxSpeed.toFloat()),
        animationSpec = tween(
            durationMillis = 1000,
            easing = FastOutSlowInEasing,
        )
        /*
            spring(
                stiffness = Spring.StiffnessMediumLow,
                dampingRatio = 0.75f  // slight overshoot
            )
            spring(
                stiffness = Spring.StiffnessLow,      // slower spring
                dampingRatio = 0.85f                  // critically damped
            )
         */
    )
    val textMeasurer = rememberTextMeasurer()

    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter
    ) {
        Canvas(modifier = Modifier.fillMaxWidth().height(400.dp)) {
            val w = size.width
            val h = size.height
            val radius = size.minDimension / 2f
            val center = Offset(w / 2f, h / 2f)

            // Rim / housing
            drawCircle(color = rimColor, radius = radius)
            drawCircle(color = Color(0xFF111111), radius = radius * 0.94f)

            // Outer ring (chrome)
            drawCircle(
                brush = Brush.radialGradient(listOf(Color(0xFFd6d6d6), Color(0xFF7f7f7f))),
                radius = radius * 0.88f,
                center = center
            )

            // Face
            drawCircle(color = faceColor, radius = radius * 0.84f, center = center)

            // Glass highlight (soft)
            drawArc(
                color = Color.White.copy(alpha = 0.08f),
                startAngle = -140f,
                sweepAngle = 60f,
                useCenter = false,
                topLeft = Offset(center.x - radius * 0.7f, center.y - radius * 0.9f),
                size = androidx.compose.ui.geometry.Size(radius * 1.4f, radius * 1.4f),
                style = Stroke(width = radius * 0.02f)
            )

            // Ticks and numbers
            val startAngle = 140f
            val sweep = 260f
            val majorTickCount = maxSpeed / 10 + 1

            for (i in 0..<majorTickCount) {
                val angle =
                    (startAngle + (sweep / (majorTickCount - 1)) * i).toDouble() * PI / 180.0
                val inner = Offset(
                    center.x + (radius * 0.6f) * cos(angle).toFloat(),
                    center.y + (radius * 0.6f) * sin(angle).toFloat()
                )
                val outer = Offset(
                    center.x + (radius * 0.78f) * cos(angle).toFloat(),
                    center.y + (radius * 0.78f) * sin(angle).toFloat()
                )
                drawLine(
                    color = Color.Black,
                    start = inner,
                    end = outer,
                    strokeWidth = if (i % 2 == 0) radius * 0.02f else radius * 0.01f,
                    cap = StrokeCap.Round
                )

                // numbers every other tick (major)
                if (i % 2 == 0) {
                    val label = ((i * (maxSpeed / (majorTickCount - 1)))).toString()
                    val textLayout = textMeasurer.measure(
                        text = AnnotatedString(label),
                        style = TextStyle(
                            color = Color(0xFF1E1E1E),
                            fontSize = (radius * 0.05f).sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    )

                    val tx =
                        center.x + (radius * 0.46f) * cos(angle).toFloat() - textLayout.size.width / 2
                    val ty =
                        center.y + (radius * 0.46f) * sin(angle).toFloat() - textLayout.size.height / 2
                    drawText(textLayout, topLeft = Offset(tx, ty))
                }
            }

            // Needle
            val percent = displayed.value / maxSpeed
            val needleAngle = startAngle + sweep * percent
            rotate(degrees = needleAngle, pivot = center) {
                // needle body
                drawLine(
                    color = needleColor,
                    start = center + Offset(-radius * 0.02f, 0f),
                    end = center + Offset(radius * 0.62f, 0f),
                    strokeWidth = radius * 0.04f,
                    cap = StrokeCap.Round
                )
                // thin pointer
                drawLine(
                    color = Color.Black,
                    start = center + Offset(-radius * 0.02f, 0f),
                    end = center + Offset(radius * 0.66f, 0f),
                    strokeWidth = radius * 0.008f,
                    cap = StrokeCap.Round
                )
            }

            // Central hub
            drawCircle(color = Color(0xFF222222), radius = radius * 0.09f, center = center)
            drawCircle(color = Color(0xFF555555), radius = radius * 0.06f, center = center)

            // Small odometer-style label (cross-platform, with km/h)
            val speedText = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = Color.Black,
                        fontSize = (radius * 0.08f).sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                ) {
                    append(displayed.value.toInt().toString())
                }
                append(" ") // small gap
                withStyle(
                    style = SpanStyle(
                        color = Color.Black,
                        fontSize = (radius * 0.03f).sp, // smaller
                        fontWeight = FontWeight.Medium
                    )
                ) {
                    append("km/h")
                }
            }

            val textLayout = textMeasurer.measure(
                text = speedText,
                style = TextStyle(textAlign = TextAlign.Center)
            )

            drawText(
                textLayoutResult = textLayout,
                topLeft = Offset(
                    center.x - textLayout.size.width / 2,
                    center.y + radius * 0.6f - textLayout.size.height / 2
                )
            )
        }
    }
}

// Sample usage in a desktop/Android composable entry point
@Preview
@Composable
fun SpeedometerPreview() {
    val styles = listOf(
        vw70sStyle, vwClassicStyle, vw60sStyle, vw80sStyle, vw90sStyle,
        vwClassic40sStyle, vwRetro50sStyle
    )
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        styles.forEach { style ->
            VintageSpeedometer(
                modifier = Modifier.size(400.dp),
                currentSpeed = 22.7F,
                maxSpeed = 140,
                style = style
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}
