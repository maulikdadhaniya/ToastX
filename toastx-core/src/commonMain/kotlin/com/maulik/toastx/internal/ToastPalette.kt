package com.maulik.toastx.internal

import androidx.compose.ui.graphics.Color
import com.maulik.toastx.ToastAppearance
import com.maulik.toastx.ToastConfig
import com.maulik.toastx.ToastType
import com.maulik.toastx.theme.ToastTheme

internal data class ResolvedPalette(
    val accent: Color,
    val border: Color,
    val background: Color,
    val shadow: Color,
    val iconBackground: Color,
    val iconForeground: Color,
    val title: Color,
    val message: Color,
)

internal fun resolvePalette(
    config: ToastConfig,
    theme: ToastTheme,
): ResolvedPalette {
    val base =
        when (config.type) {
            ToastType.Success ->
                ResolvedPalette(
                    accent = theme.successAccent,
                    border = theme.successBorder,
                    background = theme.successBackground,
                    shadow = theme.successShadow,
                    iconBackground = theme.successAccent,
                    iconForeground = Color.White,
                    title = theme.onToastTitle,
                    message = theme.outlineMessage,
                )
            ToastType.Error ->
                ResolvedPalette(
                    accent = theme.errorAccent,
                    border = theme.errorBorder,
                    background = theme.errorBackground,
                    shadow = theme.errorShadow,
                    iconBackground = theme.errorAccent,
                    iconForeground = Color.White,
                    title = theme.onToastTitle,
                    message = theme.outlineMessage,
                )
            ToastType.Warning ->
                ResolvedPalette(
                    accent = theme.warningAccent,
                    border = theme.warningBorder,
                    background = theme.warningBackground,
                    shadow = theme.warningShadow,
                    iconBackground = theme.warningAccent,
                    iconForeground = Color.White,
                    title = theme.onToastTitle,
                    message = theme.outlineMessage,
                )
            ToastType.Info ->
                ResolvedPalette(
                    accent = theme.infoAccent,
                    border = theme.infoBorder,
                    background = theme.infoBackground,
                    shadow = theme.infoShadow,
                    iconBackground = theme.infoAccent,
                    iconForeground = Color.White,
                    title = theme.onToastTitle,
                    message = theme.outlineMessage,
                )
        }

    val a = config.appearance ?: return base
    return base.copy(
        accent = a.iconBackgroundColor ?: base.accent,
        border = a.borderColor ?: base.border,
        background = a.backgroundColor ?: base.background,
        shadow = a.shadowColor ?: base.shadow,
        iconBackground = a.iconBackgroundColor ?: base.iconBackground,
        iconForeground = a.iconForegroundColor ?: base.iconForeground,
        title = a.titleColor ?: base.title,
        message = a.messageColor ?: base.message,
    )
}

internal fun ToastAppearance.hostBackground(theme: ToastTheme): Color =
    hostBackgroundColor ?: theme.glassHostBlue
