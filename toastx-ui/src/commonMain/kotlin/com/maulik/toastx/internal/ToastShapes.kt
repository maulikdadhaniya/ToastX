package com.maulik.toastx.internal

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.maulik.toastx.ToastAppearance
import com.maulik.toastx.ToastShapeOverride

internal fun resolveCardShape(
    appearance: ToastAppearance?,
    defaultCorner: Dp,
): Shape =
    when (val s = appearance?.shape) {
        is ToastShapeOverride.Rounded -> RoundedCornerShape(s.cornerRadius)
        ToastShapeOverride.Pill -> RoundedCornerShape(percent = 50)
        null -> RoundedCornerShape(defaultCorner)
    }

/** Full-width toast shape helper. */
internal fun outerShapeForToast(
    appearance: ToastAppearance?,
    fullWidth: Boolean,
    defaultCorner: Dp,
    fullWidthCorner: Dp = 20.dp,
): Shape {
    if (appearance?.shape != null) return resolveCardShape(appearance, defaultCorner)
    if (fullWidth) return RoundedCornerShape(fullWidthCorner)
    return RoundedCornerShape(defaultCorner)
}
