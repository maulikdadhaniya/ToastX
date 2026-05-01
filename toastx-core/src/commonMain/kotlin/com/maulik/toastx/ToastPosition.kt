package com.maulik.toastx

import androidx.compose.ui.Alignment

enum class ToastPosition(val alignment: Alignment) {
    TopStart(Alignment.TopStart),
    TopCenter(Alignment.TopCenter),
    TopEnd(Alignment.TopEnd),
    BottomStart(Alignment.BottomStart),
    BottomCenter(Alignment.BottomCenter),
    BottomEnd(Alignment.BottomEnd),
}

/** Bottom row positions span the full width of the toast slot (edge-to-edge within host padding). */
val ToastPosition.isBottomStripe: Boolean
    get() =
        when (this) {
            ToastPosition.BottomStart,
            ToastPosition.BottomCenter,
            ToastPosition.BottomEnd,
            -> true
            else -> false
        }
