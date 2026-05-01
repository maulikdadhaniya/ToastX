package com.maulik.toastx

import androidx.compose.runtime.Composable

data class ToastConfig(
    val id: String = "",
    /** Optional headline. Use with [message], or alone if [message] is null (at least one should be non-blank). */
    val title: String? = null,
    /** Optional body. Use with [title], or alone if [title] is null (at least one should be non-blank). */
    val message: String? = null,
    val type: ToastType = ToastType.Info,
    val style: ToastStyle = ToastStyle.Elevated,
    val showIcon: Boolean = true,
    val showClose: Boolean = false,
    val durationMs: Long = 3_000L,
    val autoDismiss: Boolean = true,
    /** Overrides [ToastHost] alignment for this toast only. */
    val position: ToastPosition? = null,
    /**
     * Layout width: `null` = full width when [position] (or host default) is a bottom stripe ([ToastPosition.isBottomStripe]),
     * capped width at [ToastHost.maxToastWidth] for top positions.
     */
    val fullWidth: Boolean? = null,
    /** Overrides [ToastHost] default enter animation for this toast only. */
    val enterAnimation: ToastEnterAnimation? = null,
    val action: ToastAction? = null,
    val appearance: ToastAppearance? = null,
    /**
     * Optional custom icon slot. Use this for Image, animated vector, Lottie wrapper, or any composable.
     * If null, ToastX renders built-in icons.
     */
    val iconContent: (@Composable (ToastType) -> Unit)? = null,
    val onToastClick: (() -> Unit)? = null,
    val onDismiss: (() -> Unit)? = null,
)
