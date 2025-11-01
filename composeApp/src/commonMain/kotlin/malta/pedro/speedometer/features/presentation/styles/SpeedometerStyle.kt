package malta.pedro.speedometer.features.presentation.styles

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily

data class SpeedometerStyle(
    val needleColor: Color,
    val rimColor: Color,
    val faceColor: Color,
    val tickColor: Color,
    val majorTickWidthFraction: Float = 0.02f,
    val minorTickWidthFraction: Float = 0.01f,
    val needleWidthFraction: Float = 0.04f,
    val pointerWidthFraction: Float = 0.008f,
    val centralHubColor: Color = Color(0xFF222222),
    val centerAccentColor: Color = Color(0xFF555555),
    val fontFamily: FontFamily = FontFamily.Monospace,
    val majorFontSizeFraction: Float = 0.05f,
    val speedFontSizeFraction: Float = 0.08f,
    val unitFontSizeFraction: Float = 0.03f
)


val vwClassicStyle = SpeedometerStyle(
    needleColor = Color(0xFFb21807),
    rimColor = Color(0xFF2b2b2b),
    faceColor = Color(0xFFf5efe6),
    tickColor = Color.Black
)

// --- 1940s / 1950s VW Beetle classic ---
// Cream face, black ticks, red needle, thick rim
val vwClassic40sStyle = SpeedometerStyle(
    needleColor = Color(0xFFb21807),
    rimColor = Color(0xFF2b2b2b),
    faceColor = Color(0xFFf5efe6),
    tickColor = Color.Black,
    majorTickWidthFraction = 0.02f,
    minorTickWidthFraction = 0.01f,
    needleWidthFraction = 0.04f,
    pointerWidthFraction = 0.008f,
    centralHubColor = Color(0xFF222222),
    centerAccentColor = Color(0xFF555555),
    fontFamily = FontFamily.Monospace
)

// --- 1960s VW Microbus ---
// Two-tone orange/cream face, black needle, retro serif font
val vw60sStyle = SpeedometerStyle(
    needleColor = Color.Black,
    rimColor = Color(0xFF3a3a3a),
    faceColor = Color(0xFFffd699), // light orange
    tickColor = Color.Black,
    majorTickWidthFraction = 0.018f,
    minorTickWidthFraction = 0.008f,
    needleWidthFraction = 0.035f,
    pointerWidthFraction = 0.007f,
    centralHubColor = Color(0xFF111111),
    centerAccentColor = Color(0xFF888888),
    fontFamily = FontFamily.Serif
)

// --- 1970s VW Beetle ---
// Cool gray-blue face, blue needle, thinner ticks, subtle hub
val vw70sStyle = SpeedometerStyle(
    needleColor = Color(0xFF0055a4),
    rimColor = Color(0xFF3a3a3a),
    faceColor = Color(0xFFe6f0f5),
    tickColor = Color(0xFF222222),
    majorTickWidthFraction = 0.018f,
    minorTickWidthFraction = 0.008f,
    needleWidthFraction = 0.03f,
    pointerWidthFraction = 0.006f,
    centralHubColor = Color(0xFF111111),
    centerAccentColor = Color(0xFF888888),
    fontFamily = FontFamily.Serif
)

// --- 1980s VW Golf / Jetta ---
// Matte black face, neon green needle, high contrast
val vw80sStyle = SpeedometerStyle(
    needleColor = Color(0xFF39FF14),
    rimColor = Color(0xFF1c1c1c),
    faceColor = Color(0xFF0f0f0f),
    tickColor = Color(0xFF39FF14),
    majorTickWidthFraction = 0.015f,
    minorTickWidthFraction = 0.007f,
    needleWidthFraction = 0.025f,
    pointerWidthFraction = 0.005f,
    centralHubColor = Color(0xFF222222),
    centerAccentColor = Color(0xFF555555),
    fontFamily = FontFamily.Monospace
)

// --- 1990s VW New Beetle ---
// Light gray face, thin red needle, minimalistic style
val vw90sStyle = SpeedometerStyle(
    needleColor = Color(0xFFc21807),
    rimColor = Color(0xFF2b2b2b),
    faceColor = Color(0xFFe0e0e0),
    tickColor = Color(0xFF333333),
    majorTickWidthFraction = 0.012f,
    minorTickWidthFraction = 0.006f,
    needleWidthFraction = 0.02f,
    pointerWidthFraction = 0.004f,
    centralHubColor = Color(0xFF444444),
    centerAccentColor = Color(0xFF888888),
    fontFamily = FontFamily.SansSerif
)

// --- 1950s “Retro Two-Tone” ---
// Red & cream face, black circular counterweight needle
val vwRetro50sStyle = SpeedometerStyle(
    needleColor = Color.Black,
    rimColor = Color(0xFF2b2b2b),
    faceColor = Color(0xFFf5e6e0), // cream-pink tone
    tickColor = Color.Black,
    majorTickWidthFraction = 0.02f,
    minorTickWidthFraction = 0.01f,
    needleWidthFraction = 0.03f,
    pointerWidthFraction = 0.006f,
    centralHubColor = Color(0xFF222222),
    centerAccentColor = Color(0xFFff3333), // small red hub circle
    fontFamily = FontFamily.Cursive
)
