package com.maulik.toastx.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.maulik.toastx.ToastAction
import com.maulik.toastx.ToastAppearance
import com.maulik.toastx.ToastConfig
import com.maulik.toastx.ToastManager
import com.maulik.toastx.ToastShapeOverride
import com.maulik.toastx.ToastType
import com.maulik.toastx.ToastStyle
import com.maulik.toastx.internal.ElevatedBell
import com.maulik.toastx.internal.ElevatedDoubleCheck
import com.maulik.toastx.internal.ElevatedNoEntry
import com.maulik.toastx.internal.MinimalInfoRing
import com.maulik.toastx.internal.ResolvedPalette
import com.maulik.toastx.internal.ToastGlyph
import com.maulik.toastx.internal.outerShapeForToast
import com.maulik.toastx.internal.resolveCardShape
import com.maulik.toastx.theme.ToastTheme
import com.maulik.toastx.theme.toastTheme
import com.maulik.toastx.theme.withColor

private fun Modifier.withOptionalToastClick(onClick: (() -> Unit)?): Modifier =
    if (onClick == null) {
        this
    } else {
        this.clickable(
            onClick = {
                try {
                    onClick()
                } catch (_: Throwable) {
                }
            },
        )
    }

private fun safeDismissToast() {
    try {
        ToastManager.dismiss()
    } catch (_: Throwable) {
    }
}

@Composable
internal fun ToastVariant(
    config: ToastConfig,
    palette: ResolvedPalette,
    modifier: Modifier = Modifier,
    fullWidth: Boolean = false,
) {
    when (config.style) {
        ToastStyle.Soft -> SoftToast(config, palette, modifier, borderWidth = 1.dp, fullWidth = fullWidth)
        ToastStyle.Minimal -> MinimalToast(config, palette, modifier, fullWidth = fullWidth)
        ToastStyle.Outline -> SoftToast(config, palette, modifier, borderWidth = 1.5.dp, fullWidth = fullWidth)
        ToastStyle.Elevated -> ElevatedToast(config, palette, modifier, fullWidth = fullWidth)
        ToastStyle.OuterShadow -> OuterShadowToast(config, palette, modifier, fullWidth)
        ToastStyle.BottomSheet -> BottomSheetToast(config, palette, modifier, fullWidth)
        ToastStyle.Gradient -> GradientToast(config, palette, modifier, fullWidth)
        ToastStyle.AnimatedBorder -> AnimatedBorderToast(config, palette, modifier, fullWidth)
        ToastStyle.Glass -> GlassToast(config, palette, modifier, fullWidth = fullWidth)
    }
}

@Composable
private fun SoftToast(
    config: ToastConfig,
    palette: ResolvedPalette,
    modifier: Modifier = Modifier,
    borderWidth: Dp,
    fullWidth: Boolean = false,
) {
    val theme = toastTheme()
    val shape = outerShapeForToast(config.appearance, fullWidth, 18.dp)
    val bg = config.appearance?.backgroundColor ?: palette.background
    val border = config.appearance?.borderColor ?: palette.border
    val pad = config.appearance?.contentPadding ?: androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp, vertical = 14.dp)

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(shape)
                .background(bg)
                .border(borderWidth, border, shape)
                .padding(pad)
                .withOptionalToastClick(config.onToastClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        if (config.showIcon) {
            Box(
                modifier =
                    Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(palette.iconBackground),
                contentAlignment = Alignment.Center,
            ) {
                config.iconContent?.invoke(config.type) ?: ToastGlyph(config.type, palette.iconBackground, Modifier.size(28.dp))
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            config.title?.let {
                Text(
                    it,
                    style = theme.withColor(theme.typography.title, palette.title),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            config.message?.let {
                if (config.title != null) Spacer(Modifier.height(4.dp))
                Text(
                    it,
                    style = theme.withColor(theme.typography.message, palette.message),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
        if (config.showClose) {
            DismissChip(palette.accent, onClick = { safeDismissToast() })
        }
    }
}

@Composable
private fun MinimalToast(
    config: ToastConfig,
    palette: ResolvedPalette,
    modifier: Modifier = Modifier,
    fullWidth: Boolean = false,
) {
    val theme = toastTheme()
    val shape = outerShapeForToast(config.appearance, fullWidth, 12.dp)
    val borderW = config.appearance?.borderWidth ?: 1.dp
    val bg = config.appearance?.backgroundColor ?: palette.background
    val border = config.appearance?.borderColor ?: palette.border
    val pad = config.appearance?.contentPadding ?: androidx.compose.foundation.layout.PaddingValues(horizontal = 14.dp, vertical = 12.dp)
    val line =
        buildString {
            config.title?.let { append(it) }
            if (config.title != null && config.message != null) append(" ")
            config.message?.let { append(it) }
        }

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(shape)
                .background(bg)
                .border(borderW, border, shape)
                .padding(pad)
                .then(
                    Modifier.withOptionalToastClick(config.onToastClick),
                ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        if (config.showIcon) {
            config.iconContent?.invoke(config.type) ?: MinimalInfoRing(palette.accent, Modifier.size(22.dp))
        }
        Text(
            line,
            modifier = Modifier.weight(1f),
            style = theme.withColor(theme.typography.minimalLine, palette.title),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
        if (config.showClose) {
            DismissChip(palette.accent, onClick = { safeDismissToast() })
        }
    }
}

@Composable
private fun ElevatedToast(
    config: ToastConfig,
    palette: ResolvedPalette,
    modifier: Modifier = Modifier,
    fullWidth: Boolean = false,
) {
    val shape =
        outerShapeForToast(
            config.appearance,
            fullWidth,
            defaultCorner = 14.dp,
            fullWidthCorner = 18.dp,
        )
    val shadow = config.appearance?.shadowColor ?: palette.shadow
    val elev = config.appearance?.elevation ?: 14.dp
    val pad = config.appearance?.contentPadding ?: androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp, vertical = 14.dp)
    val spotAlpha = 0.4f
    val ambientAlpha = 0.35f

    val theme = toastTheme()
    val cardBg = config.appearance?.backgroundColor ?: palette.background
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .shadow(
                    elev,
                    shape,
                    clip = false,
                    spotColor = shadow.copy(alpha = spotAlpha),
                    ambientColor = shadow.copy(alpha = ambientAlpha),
                )
                .clip(shape)
                .background(cardBg)
                .padding(pad)
                .then(
                    Modifier.withOptionalToastClick(config.onToastClick),
                ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        if (config.showIcon) {
            val tileShape = RoundedCornerShape(8.dp)
            Box(
                modifier =
                    Modifier
                        .size(48.dp)
                        .clip(tileShape)
                        .background(palette.iconBackground),
                contentAlignment = Alignment.Center,
            ) {
                config.iconContent?.invoke(config.type) ?: ElevatedInnerGlyph(config.type, palette.iconBackground)
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            config.title?.let {
                Text(
                    it,
                    style = theme.withColor(theme.typography.title, palette.title),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            config.message?.let {
                if (config.title != null) Spacer(Modifier.height(4.dp))
                Text(
                    it,
                    style = theme.withColor(theme.typography.message, palette.message),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
        if (config.showClose) {
            DismissChip(palette.message.copy(alpha = 0.9f), onClick = { safeDismissToast() })
        }
    }
}

@Composable
private fun ElevatedInnerGlyph(
    type: ToastType,
    bg: Color,
) {
    when (type) {
        ToastType.Success -> ElevatedDoubleCheck(bg, Modifier.size(24.dp))
        ToastType.Info -> ElevatedBell(bg, Modifier.size(24.dp))
        ToastType.Warning -> ToastGlyph(ToastType.Warning, bg, Modifier.size(28.dp))
        ToastType.Error -> ElevatedNoEntry(bg, Modifier.size(24.dp))
    }
}

@Composable
private fun OuterShadowToast(
    config: ToastConfig,
    palette: ResolvedPalette,
    modifier: Modifier = Modifier,
    fullWidth: Boolean = false,
) {
    val shape = outerShapeForToast(config.appearance, fullWidth, defaultCorner = 16.dp)
    val pad = config.appearance?.contentPadding ?: androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp, vertical = 14.dp)
    val bg = config.appearance?.backgroundColor ?: palette.background
    val borderCol = config.appearance?.borderColor ?: palette.border
    val elev = config.appearance?.elevation ?: 30.dp
    val shadowTint = config.appearance?.shadowColor ?: palette.shadow
    val theme = toastTheme()
    val dismissTint = palette.message.copy(alpha = 0.9f)
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .shadow(
                    elevation = elev,
                    shape = shape,
                    clip = false,
                    spotColor = shadowTint.copy(alpha = 0.48f),
                    ambientColor = Color.Black.copy(alpha = 0.26f),
                )
                .clip(shape)
                .background(bg)
                .border(1.dp, borderCol, shape)
                .padding(pad)
                .withOptionalToastClick(config.onToastClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        if (config.showIcon) {
            Box(
                modifier =
                    Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(palette.iconBackground.copy(alpha = 0.24f)),
                contentAlignment = Alignment.Center,
            ) {
                config.iconContent?.invoke(config.type) ?: ToastGlyph(config.type, palette.iconBackground, Modifier.size(26.dp))
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            config.title?.let {
                Text(
                    it,
                    style = theme.withColor(theme.typography.title, palette.title),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            config.message?.let {
                if (config.title != null) Spacer(Modifier.height(3.dp))
                Text(
                    it,
                    style = theme.withColor(theme.typography.message, palette.message),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
        if (config.showClose) {
            DismissChip(dismissTint, onClick = { safeDismissToast() })
        }
    }
}

@Composable
private fun BottomSheetToast(
    config: ToastConfig,
    palette: ResolvedPalette,
    modifier: Modifier = Modifier,
    fullWidth: Boolean = false,
) {
    val shape =
        if (config.appearance?.shape != null) {
            resolveCardShape(config.appearance, 18.dp)
        } else if (fullWidth) {
            RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        } else {
            RoundedCornerShape(18.dp)
        }
    val pad = config.appearance?.contentPadding ?: androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp, vertical = 14.dp)
    val theme = toastTheme()
    val sheetBg = config.appearance?.backgroundColor ?: palette.background
    val sheetBorder = config.appearance?.borderColor ?: palette.border
    val dismissTint = palette.message.copy(alpha = 0.9f)

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .shadow(
                    elevation = config.appearance?.elevation ?: 20.dp,
                    shape = shape,
                    clip = false,
                    spotColor = Color.Black.copy(alpha = 0.22f),
                    ambientColor = Color.Black.copy(alpha = 0.16f),
                )
                .clip(shape)
                .background(sheetBg)
                .border(1.dp, sheetBorder, shape)
                .padding(pad)
                .withOptionalToastClick(config.onToastClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        if (config.showIcon) {
            Box(
                modifier =
                    Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(palette.iconBackground.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center,
            ) {
                config.iconContent?.invoke(config.type) ?: ToastGlyph(config.type, palette.iconBackground, Modifier.size(26.dp))
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            config.title?.let {
                Text(
                    it,
                    style = theme.withColor(theme.typography.title, palette.title),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            config.message?.let {
                if (config.title != null) Spacer(Modifier.height(3.dp))
                Text(
                    it,
                    style = theme.withColor(theme.typography.message, palette.message),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
        if (config.showClose) {
            DismissChip(dismissTint, onClick = { safeDismissToast() })
        }
    }
}

@Composable
private fun GradientToast(
    config: ToastConfig,
    palette: ResolvedPalette,
    modifier: Modifier = Modifier,
    fullWidth: Boolean = false,
) {
    val shape = outerShapeForToast(config.appearance, fullWidth, defaultCorner = 18.dp)
    val gradient =
        Brush.linearGradient(
            colors =
                listOf(
                    palette.accent.copy(alpha = 0.92f),
                    palette.accent.copy(alpha = 0.72f),
                    palette.shadow.copy(alpha = 0.80f),
                ),
            start = Offset(0f, 0f),
            end = Offset(900f, 600f),
        )
    val pad = config.appearance?.contentPadding ?: androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp, vertical = 14.dp)
    val theme = toastTheme()
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .shadow(
                    14.dp,
                    shape,
                    clip = false,
                    spotColor = palette.shadow.copy(alpha = 0.45f),
                    ambientColor = palette.shadow.copy(alpha = 0.28f),
                )
                .clip(shape)
                .then(
                    config.appearance?.backgroundColor?.let { custom ->
                        Modifier.background(custom)
                    } ?: Modifier.background(gradient),
                )
                .padding(pad)
                .withOptionalToastClick(config.onToastClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        if (config.showIcon) {
            Box(
                modifier =
                    Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(theme.gradientIconScrim),
                contentAlignment = Alignment.Center,
            ) {
                config.iconContent?.invoke(config.type)
                    ?: ToastGlyph(config.type, theme.gradientIconForeground, Modifier.size(26.dp))
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            config.title?.let {
                Text(
                    it,
                    style = theme.withColor(theme.typography.gradientTitle, theme.gradientContentTitle),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            config.message?.let {
                if (config.title != null) Spacer(Modifier.height(3.dp))
                Text(
                    it,
                    style = theme.withColor(theme.typography.gradientMessage, theme.gradientContentMessage),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
        if (config.showClose) {
            DismissChip(theme.gradientCloseTint, onClick = { safeDismissToast() })
        }
    }
}

private fun animatedBorderInnerShape(
    appearance: ToastAppearance?,
    fullWidth: Boolean,
    borderWidth: Dp,
): Shape {
    val defaultOuter = 16.dp
    val fullCorner = 20.dp
    return when (val s = appearance?.shape) {
        is ToastShapeOverride.Rounded ->
            RoundedCornerShape((s.cornerRadius - borderWidth).coerceAtLeast(1.dp))
        ToastShapeOverride.Pill -> RoundedCornerShape(percent = 50)
        null -> {
            val outer = if (fullWidth) fullCorner else defaultOuter
            RoundedCornerShape((outer - borderWidth).coerceAtLeast(4.dp))
        }
    }
}

@Composable
private fun AnimatedBorderToast(
    config: ToastConfig,
    palette: ResolvedPalette,
    modifier: Modifier = Modifier,
    fullWidth: Boolean = false,
) {
    val shape = outerShapeForToast(config.appearance, fullWidth, defaultCorner = 16.dp)
    val pad = config.appearance?.contentPadding ?: androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp, vertical = 14.dp)
    val borderWidth = 2.5.dp
    val innerShape = animatedBorderInnerShape(config.appearance, fullWidth, borderWidth)
    val animatedBorderBg = config.appearance?.backgroundColor ?: palette.background
    val theme = toastTheme()

    val transition = rememberInfiniteTransition(label = "toastBorder")
    // One-way loop: translate the brush exactly one gradient period (start color == end color) so Restart has no seam.
    val shift by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec =
            infiniteRepeatable(
                animation = tween(durationMillis = 3200, easing = LinearEasing),
                repeatMode = RepeatMode.Restart,
            ),
        label = "borderShift",
    )
    val density = LocalDensity.current

    BoxWithConstraints(
        modifier =
            modifier
                .fillMaxWidth()
                .shadow(
                    10.dp,
                    shape,
                    clip = false,
                    spotColor = palette.shadow.copy(alpha = 0.2f),
                    ambientColor = palette.shadow.copy(alpha = 0.18f),
                ),
    ) {
        val wPx = with(density) { maxWidth.toPx() }.coerceAtLeast(1f)
        val hPx =
            with(density) {
                if (constraints.hasBoundedHeight) {
                    maxHeight.toPx().coerceAtLeast(1f)
                } else {
                    wPx * 0.45f
                }
            }
        val travel = (wPx + hPx) * 1.35f
        val y0 = -hPx * 0.28f
        val y1 = hPx * 1.35f
        val gx = travel * 1.18f
        val gy = y1 - y0
        val solid = palette.accent
        val soft = palette.shadow.copy(alpha = 0.5f)
        val movingBrush =
            Brush.linearGradient(
                colors = listOf(solid, soft, solid, soft, solid),
                start = Offset(-travel + shift * gx, y0 + shift * gy),
                end = Offset(-travel + shift * gx + gx, y0 + shift * gy + gy),
            )

        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(shape)
                    .background(movingBrush),
        ) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(borderWidth)
                        .clip(innerShape)
                        .background(animatedBorderBg)
                        .padding(pad)
                        .withOptionalToastClick(config.onToastClick),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                if (config.showIcon) {
                    Box(
                        modifier =
                            Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(palette.iconBackground.copy(alpha = 0.14f)),
                        contentAlignment = Alignment.Center,
                    ) {
                        config.iconContent?.invoke(config.type)
                            ?: ToastGlyph(config.type, palette.iconBackground, Modifier.size(26.dp))
                    }
                }
                Column(modifier = Modifier.weight(1f)) {
                    config.title?.let {
                        Text(
                            it,
                            style = theme.withColor(theme.typography.title, palette.title),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                    config.message?.let {
                        if (config.title != null) Spacer(Modifier.height(3.dp))
                        Text(
                            it,
                            style = theme.withColor(theme.typography.message, palette.message),
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
                if (config.showClose) {
                    DismissChip(palette.accent, onClick = { safeDismissToast() })
                }
            }
        }
    }
}

@Composable
private fun GlassToast(
    config: ToastConfig,
    palette: ResolvedPalette,
    modifier: Modifier = Modifier,
    fullWidth: Boolean = false,
) {
    val theme = toastTheme()
    val appearance = config.appearance
    val fill = appearance?.glassScrimColor ?: theme.glassFill
    val stroke = appearance?.borderColor ?: theme.glassBorder
    val actionBg = appearance?.actionBackgroundColor ?: theme.glassActionFill
    val onGlass = appearance?.titleColor ?: theme.onGlass
    val shape =
        if (appearance?.shape != null) {
            resolveCardShape(appearance, 28.dp)
        } else if (fullWidth) {
            RoundedCornerShape(24.dp)
        } else {
            RoundedCornerShape(28.dp)
        }

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .shadow(22.dp, shape, spotColor = Color.Black.copy(0.14f), ambientColor = Color.Black.copy(0.10f))
                .clip(shape)
                .background(fill)
                .border(1.dp, stroke, shape)
                .then(
                    appearance?.contentPadding?.let { Modifier.padding(it) }
                        ?: Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
                )
                .then(
                    Modifier.withOptionalToastClick(config.onToastClick),
                ),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (config.showIcon) {
                Box(
                    modifier =
                        Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(palette.accent.copy(alpha = 0.22f)),
                    contentAlignment = Alignment.Center,
                ) {
                    config.iconContent?.invoke(config.type)
                        ?: ToastGlyph(config.type, palette.iconBackground, Modifier.size(26.dp))
                }
                Spacer(Modifier.width(14.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                config.title?.let {
                    Text(
                        it,
                        style = theme.withColor(theme.typography.glassTitle, onGlass),
                    )
                }
                config.message?.let {
                    if (config.title != null) Spacer(Modifier.height(6.dp))
                    Text(
                        it,
                        style = theme.withColor(theme.typography.glassMessage, onGlass.copy(alpha = 0.92f)),
                        maxLines = 3,
                    )
                }
            }
            config.action?.let { action ->
                Spacer(Modifier.width(12.dp))
                GlassCta(theme, action, actionBg, appearance?.actionContentColor ?: onGlass)
            }
        }
    }
}

@Composable
private fun GlassCta(
    theme: ToastTheme,
    action: ToastAction,
    container: Color,
    content: Color,
) {
    Button(
        onClick = {
            try {
                action.onClick()
            } catch (_: Throwable) {
            }
            safeDismissToast()
        },
        shape = RoundedCornerShape(50),
        colors =
            ButtonDefaults.buttonColors(
                containerColor = container,
                contentColor = content,
            ),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 20.dp, vertical = 8.dp),
    ) {
        Text(
            action.label,
            style = theme.withColor(theme.typography.glassAction, content),
        )
    }
}

@Composable
private fun DismissChip(
    tint: Color,
    onClick: () -> Unit,
) {
    val theme = toastTheme()
    Box(
        modifier =
            Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            "✕",
            style = theme.withColor(theme.typography.dismissGlyph, tint),
        )
    }
}
