package com.maulik.toastx

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

/** Optional per-toast overrides; null fields fall back to [ToastTheme] + state palette. */
data class ToastAppearance(
    val backgroundColor: Color? = null,
    val borderColor: Color? = null,
    val borderWidth: Dp? = null,
    val iconBackgroundColor: Color? = null,
    val iconForegroundColor: Color? = null,
    val titleColor: Color? = null,
    val messageColor: Color? = null,
    val actionBackgroundColor: Color? = null,
    val actionContentColor: Color? = null,
    val shadowColor: Color? = null,
    val glassScrimColor: Color? = null,
    val hostBackgroundColor: Color? = null,
    val shape: ToastShapeOverride? = null,
    val contentPadding: PaddingValues? = null,
    val elevation: Dp? = null,
)

sealed class ToastShapeOverride {
    data class Rounded(val cornerRadius: Dp) : ToastShapeOverride()
    data object Pill : ToastShapeOverride()
}
