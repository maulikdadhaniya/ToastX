package com.maulik.toastx

/**
 * Visual layout families aligned with ToastX reference designs.
 *
 * - [Soft]: Pale fill, circular icon, title + message, optional close (soft status cards).
 * - [Minimal]: Compact single-line, white card, thin accent border.
 * - [Outline]: Two-line emphasis with strong outline and circular icon (status + description).
 * - [Elevated]: White card, rounded square icon tile, colored shadow.
 * - [OuterShadow]: Clean white card with stronger neutral ambient shadow outside the card.
 * - [BottomSheet]: Toast card styled like a mini bottom-sheet (top corners rounded, bottom edge flat when full-width).
 * - [Gradient]: Rich gradient background card.
 * - [AnimatedBorder]: Subtle moving gradient border around a clean card.
 * - [Glass]: Frosted glass bar on saturated background + optional pill CTA.
 */
sealed class ToastStyle {
    data object Soft : ToastStyle()
    data object Minimal : ToastStyle()
    data object Outline : ToastStyle()
    data object Elevated : ToastStyle()
    data object OuterShadow : ToastStyle()
    data object BottomSheet : ToastStyle()
    data object Gradient : ToastStyle()
    data object AnimatedBorder : ToastStyle()
    data object Glass : ToastStyle()
}
