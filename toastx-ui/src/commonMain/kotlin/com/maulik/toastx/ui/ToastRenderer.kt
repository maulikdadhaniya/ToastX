package com.maulik.toastx.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.maulik.toastx.ToastConfig
import com.maulik.toastx.ToastManager
import com.maulik.toastx.ToastPosition
import com.maulik.toastx.isBottomStripe
import com.maulik.toastx.internal.resolvePalette
import com.maulik.toastx.internal.swipeToDismissToast
import com.maulik.toastx.theme.toastTheme

@Composable
fun ToastRenderer(
    config: ToastConfig,
    hostPosition: ToastPosition = ToastPosition.TopCenter,
    modifier: Modifier = Modifier,
    swipeToDismiss: Boolean = true,
    maxWidth: Dp = 440.dp,
) {
    val theme = toastTheme()
    val palette = resolvePalette(config, theme)
    val slotPosition = config.position ?: hostPosition
    val useFullWidth = config.fullWidth ?: slotPosition.isBottomStripe

    Box(
        modifier
            .then(
                if (useFullWidth) {
                    Modifier.fillMaxWidth()
                } else {
                    Modifier.widthIn(max = maxWidth).fillMaxWidth()
                },
            )
            .swipeToDismissToast(swipeToDismiss) {
                try {
                    ToastManager.dismiss()
                } catch (_: Throwable) {
                }
            },
    ) {
        ToastVariant(config, palette, fullWidth = useFullWidth)
    }
}
