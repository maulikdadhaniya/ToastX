package com.maulik.toastx.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Typography tokens for all toast styles. Set [defaultFontFamily] to apply one font family app-wide;
 * individual [TextStyle]s can still set weights and sizes.
 */
data class ToastTypography(
    val defaultFontFamily: FontFamily? = null,
    val title: TextStyle =
        TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            lineHeight = 20.sp,
        ),
    val message: TextStyle =
        TextStyle(
            fontSize = 13.sp,
            lineHeight = 18.sp,
        ),
    val minimalLine: TextStyle =
        TextStyle(
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            lineHeight = 18.sp,
        ),
    val glassTitle: TextStyle =
        TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            lineHeight = 21.sp,
        ),
    val glassMessage: TextStyle =
        TextStyle(
            fontSize = 14.sp,
            lineHeight = 20.sp,
        ),
    val glassAction: TextStyle =
        TextStyle(
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            lineHeight = 20.sp,
        ),
    val dismissGlyph: TextStyle =
        TextStyle(
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            lineHeight = 18.sp,
        ),
    val gradientTitle: TextStyle =
        TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            lineHeight = 20.sp,
        ),
    val gradientMessage: TextStyle =
        TextStyle(
            fontSize = 13.sp,
            lineHeight = 18.sp,
        ),
) {
    fun role(style: TextStyle): TextStyle =
        if (defaultFontFamily != null) {
            style.copy(fontFamily = defaultFontFamily)
        } else {
            style
        }

    companion object {
        val Default = ToastTypography()
    }
}

/** Merges [role] with [ToastTypography.defaultFontFamily] and applies [color]. */
fun ToastTheme.withColor(
    role: TextStyle,
    color: Color,
): TextStyle = typography.role(role).copy(color = color)
