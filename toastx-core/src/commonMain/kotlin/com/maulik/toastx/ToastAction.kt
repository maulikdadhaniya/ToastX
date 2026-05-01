package com.maulik.toastx

data class ToastAction(
    val label: String,
    val onClick: () -> Unit,
)
