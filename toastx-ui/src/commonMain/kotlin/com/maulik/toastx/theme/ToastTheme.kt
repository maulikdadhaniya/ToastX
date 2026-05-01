package com.maulik.toastx.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Default colors tuned to the ToastX reference mocks (soft pastels, tinted shadows, glass on deep blue).
 */
data class ToastTheme(
    val successAccent: Color = Color(0xFF2E7D32),
    val successBorder: Color = Color(0xFFA5D6A7),
    val successBackground: Color = Color(0xFFE8F5E9),
    val successShadow: Color = Color(0xFF81C784),

    val errorAccent: Color = Color(0xFFC62828),
    val errorBorder: Color = Color(0xFFEF9A9A),
    val errorBackground: Color = Color(0xFFFFEBEE),
    val errorShadow: Color = Color(0xFFE57373),

    val warningAccent: Color = Color(0xFFE65100),
    val warningBorder: Color = Color(0xFFFFB74D),
    val warningBackground: Color = Color(0xFFFFF8E1),
    val warningShadow: Color = Color(0xFFFFB74D),

    val infoAccent: Color = Color(0xFF1565C0),
    val infoBorder: Color = Color(0xFF90CAF9),
    val infoBackground: Color = Color(0xFFE3F2FD),
    val infoShadow: Color = Color(0xFF64B5F6),

    val glassHostBlue: Color = Color(0xFF2B59A2),
    /** Light-theme glass: reads on white/light app surfaces (opaque frost + dark ink). */
    val glassFill: Color = Color(0xFFF2F4F7),
    val glassBorder: Color = Color(0x29000000),
    val glassActionFill: Color = Color(0x14000000),
    val onGlass: Color = Color(0xFF1A1D21),

    val onDark: Color = Color.White,
    /** Title on standard toast surfaces (elevated / outer shadow, soft, etc.). */
    val onToastTitle: Color = Color(0xFF1A1A1A),
    val outlineMessage: Color = Color(0xFF616161),

    val typography: ToastTypography = ToastTypography.Default,

    /** Text on [ToastStyle.Gradient] fills (high chroma); separate from [onToastTitle] for contrast. */
    val gradientContentTitle: Color = Color.White,
    val gradientContentMessage: Color = Color.White.copy(alpha = 0.92f),
    val gradientIconScrim: Color = Color.White.copy(alpha = 0.24f),
    val gradientCloseTint: Color = Color.White.copy(alpha = 0.86f),
    /** Icon tint on gradient when using default glyphs. */
    val gradientIconForeground: Color = Color.White,
)

val LocalToastTheme = staticCompositionLocalOf { ToastTheme() }

object ToastThemeDefaults {
    val light: ToastTheme get() = ToastTheme()
    val dark: ToastTheme
        get() =
            ToastTheme(
                successAccent = Color(0xFF66BB6A),
                successBorder = Color(0xFF2E7D32),
                successBackground = Color(0xFF16291B),
                successShadow = Color(0xFF2E7D32),
                errorAccent = Color(0xFFEF5350),
                errorBorder = Color(0xFFB71C1C),
                errorBackground = Color(0xFF2A1717),
                errorShadow = Color(0xFFB71C1C),
                warningAccent = Color(0xFFFFB74D),
                warningBorder = Color(0xFFF57C00),
                warningBackground = Color(0xFF2C2416),
                warningShadow = Color(0xFFF57C00),
                infoAccent = Color(0xFF64B5F6),
                infoBorder = Color(0xFF1565C0),
                infoBackground = Color(0xFF162535),
                infoShadow = Color(0xFF1565C0),
                glassHostBlue = Color(0xFF1E3A6B),
                /** Dark-theme glass: translucent panel on dark surfaces. */
                glassFill = Color.White.copy(alpha = 0.14f),
                glassBorder = Color.White.copy(alpha = 0.32f),
                glassActionFill = Color.White.copy(alpha = 0.26f),
                onGlass = Color.White,
                onDark = Color.White,
                onToastTitle = Color(0xFFE8EEF4),
                outlineMessage = Color(0xFFB0BEC5),
                gradientContentMessage = Color.White.copy(alpha = 0.90f),
            )
}

@Composable
@ReadOnlyComposable
fun toastTheme(): ToastTheme = LocalToastTheme.current
