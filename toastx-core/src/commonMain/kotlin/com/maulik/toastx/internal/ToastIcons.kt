package com.maulik.toastx.internal

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.maulik.toastx.ToastType

@Composable
internal fun ToastGlyph(
    type: ToastType,
    tint: Color,
    modifier: Modifier = Modifier.size(22.dp),
) {
    when (type) {
        ToastType.Success ->
            Canvas(modifier) {
                val stroke = size.minDimension * 0.12f
                drawCircle(tint, size.minDimension / 2f, center)
                val inset = size.minDimension * 0.28f
                drawLine(
                    color = Color.White,
                    start = Offset(inset, size.height * 0.52f),
                    end = Offset(size.width * 0.42f, size.height * 0.68f),
                    strokeWidth = stroke,
                    cap = StrokeCap.Round,
                )
                drawLine(
                    color = Color.White,
                    start = Offset(size.width * 0.42f, size.height * 0.68f),
                    end = Offset(size.width - inset, size.height * 0.35f),
                    strokeWidth = stroke,
                    cap = StrokeCap.Round,
                )
            }
        ToastType.Error ->
            Canvas(modifier) {
                val stroke = size.minDimension * 0.12f
                drawCircle(tint, size.minDimension / 2f, center)
                val pad = size.minDimension * 0.3f
                drawLine(
                    Color.White,
                    Offset(pad, pad),
                    Offset(size.width - pad, size.height - pad),
                    stroke,
                    cap = StrokeCap.Round,
                )
                drawLine(
                    Color.White,
                    Offset(size.width - pad, pad),
                    Offset(pad, size.height - pad),
                    stroke,
                    cap = StrokeCap.Round,
                )
            }
        ToastType.Warning ->
            Canvas(modifier) {
                val stroke = size.minDimension * 0.12f
                drawCircle(tint, size.minDimension / 2f, center)
                val w = size.width * 0.1f
                drawLine(
                    Color.White,
                    Offset(center.x, size.height * 0.32f),
                    Offset(center.x, size.height * 0.55f),
                    w,
                    cap = StrokeCap.Round,
                )
                drawCircle(Color.White, w * 0.55f, Offset(center.x, size.height * 0.72f))
            }
        ToastType.Info ->
            Canvas(modifier) {
                val stroke = size.minDimension * 0.12f
                drawCircle(tint, size.minDimension / 2f, center)
                val w = size.width * 0.1f
                drawLine(
                    Color.White,
                    Offset(center.x, size.height * 0.34f),
                    Offset(center.x, size.height * 0.52f),
                    w,
                    cap = StrokeCap.Round,
                )
                drawLine(
                    Color.White,
                    Offset(center.x, size.height * 0.58f),
                    Offset(center.x, size.height * 0.72f),
                    w * 0.65f,
                    cap = StrokeCap.Round,
                )
            }
    }
}

/** Small “info” ring + i for minimal single-line toasts. */
@Composable
internal fun MinimalInfoRing(
    tint: Color,
    modifier: Modifier = Modifier.size(20.dp),
) {
    Canvas(modifier) {
        val stroke = size.minDimension * 0.08f
        drawCircle(tint, size.minDimension / 2f - stroke, center, style = Stroke(stroke))
        val w = size.width * 0.08f
        drawLine(
            tint,
            Offset(center.x, size.height * 0.32f),
            Offset(center.x, size.height * 0.48f),
            w,
            cap = StrokeCap.Round,
        )
        drawCircle(tint, w * 0.45f, Offset(center.x, size.height * 0.66f))
    }
}

@Composable
internal fun ElevatedDoubleCheck(
    @Suppress("UNUSED_PARAMETER") tint: Color,
    modifier: Modifier = Modifier.size(22.dp),
) {
    Canvas(modifier) {
        val stroke = size.minDimension * 0.12f
        val p1 =
            Path().apply {
                moveTo(size.width * 0.22f, size.height * 0.52f)
                lineTo(size.width * 0.45f, size.height * 0.72f)
                lineTo(size.width * 0.82f, size.height * 0.28f)
            }
        drawPath(
            p1,
            Color.White,
            style = Stroke(stroke, cap = StrokeCap.Round, join = StrokeJoin.Round),
        )
    }
}

@Composable
internal fun ElevatedBell(
    @Suppress("UNUSED_PARAMETER") tint: Color,
    modifier: Modifier = Modifier.size(22.dp),
) {
    Canvas(modifier) {
        val stroke = size.minDimension * 0.1f
        val w = size.width
        val h = size.height
        val path =
            Path().apply {
                moveTo(w * 0.35f, h * 0.42f)
                quadraticTo(w * 0.35f, h * 0.18f, w * 0.5f, h * 0.18f)
                quadraticTo(w * 0.65f, h * 0.18f, w * 0.65f, h * 0.42f)
                lineTo(w * 0.72f, h * 0.62f)
                lineTo(w * 0.28f, h * 0.62f)
                close()
            }
        drawPath(path, Color.White, style = Stroke(stroke, join = androidx.compose.ui.graphics.StrokeJoin.Round))
        drawLine(Color.White, Offset(w * 0.42f, h * 0.72f), Offset(w * 0.58f, h * 0.72f), stroke, cap = StrokeCap.Round)
    }
}

@Composable
internal fun ElevatedNoEntry(
    tint: Color,
    modifier: Modifier = Modifier.size(22.dp),
) {
    Canvas(modifier) {
        val stroke = size.minDimension * 0.1f
        drawCircle(tint, size.minDimension / 2f - stroke, center, style = Stroke(stroke))
        drawLine(
            Color.White,
            Offset(size.width * 0.28f, size.height * 0.72f),
            Offset(size.width * 0.72f, size.height * 0.28f),
            stroke * 1.1f,
            cap = StrokeCap.Round,
        )
    }
}
