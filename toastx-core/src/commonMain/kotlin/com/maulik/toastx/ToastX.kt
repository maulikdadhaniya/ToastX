package com.maulik.toastx

object ToastX {
    fun show(
        message: String? = null,
        title: String? = null,
        type: ToastType = ToastType.Info,
        style: ToastStyle = ToastStyle.Elevated,
        showClose: Boolean = false,
        position: ToastPosition? = null,
        fullWidth: Boolean? = null,
        enterAnimation: ToastEnterAnimation? = null,
        durationMs: Long = 3_000L,
        autoDismiss: Boolean = true,
        onDismiss: (() -> Unit)? = null,
    ) {
        ToastManager.show(
            ToastConfig(
                title = title.asToastText(),
                message = message.asToastText(),
                type = type,
                style = style,
                showClose = showClose,
                position = position,
                fullWidth = fullWidth,
                enterAnimation = enterAnimation,
                durationMs = durationMs,
                autoDismiss = autoDismiss,
                onDismiss = onDismiss,
            ),
        )
    }

    fun success(
        message: String? = null,
        title: String? = null,
        style: ToastStyle = ToastStyle.Elevated,
        showClose: Boolean = false,
        position: ToastPosition? = null,
        fullWidth: Boolean? = null,
        enterAnimation: ToastEnterAnimation? = null,
        durationMs: Long = 3_000L,
        autoDismiss: Boolean = true,
        onDismiss: (() -> Unit)? = null,
    ) {
        ToastManager.show(
            ToastConfig(
                title = title.asToastText(),
                message = message.asToastText(),
                type = ToastType.Success,
                style = style,
                showClose = showClose,
                position = position,
                fullWidth = fullWidth,
                enterAnimation = enterAnimation,
                durationMs = durationMs,
                autoDismiss = autoDismiss,
                onDismiss = onDismiss,
            ),
        )
    }

    fun error(
        message: String? = null,
        title: String? = null,
        style: ToastStyle = ToastStyle.Elevated,
        showClose: Boolean = false,
        position: ToastPosition? = null,
        fullWidth: Boolean? = null,
        enterAnimation: ToastEnterAnimation? = null,
        durationMs: Long = 4_000L,
        autoDismiss: Boolean = true,
        onDismiss: (() -> Unit)? = null,
    ) {
        ToastManager.show(
            ToastConfig(
                title = title.asToastText(),
                message = message.asToastText(),
                type = ToastType.Error,
                style = style,
                showClose = showClose,
                position = position,
                fullWidth = fullWidth,
                enterAnimation = enterAnimation,
                durationMs = durationMs,
                autoDismiss = autoDismiss,
                onDismiss = onDismiss,
            ),
        )
    }

    fun warning(
        message: String? = null,
        title: String? = null,
        style: ToastStyle = ToastStyle.Elevated,
        showClose: Boolean = false,
        position: ToastPosition? = null,
        fullWidth: Boolean? = null,
        enterAnimation: ToastEnterAnimation? = null,
        durationMs: Long = 3_500L,
        autoDismiss: Boolean = true,
        onDismiss: (() -> Unit)? = null,
    ) {
        ToastManager.show(
            ToastConfig(
                title = title.asToastText(),
                message = message.asToastText(),
                type = ToastType.Warning,
                style = style,
                showClose = showClose,
                position = position,
                fullWidth = fullWidth,
                enterAnimation = enterAnimation,
                durationMs = durationMs,
                autoDismiss = autoDismiss,
                onDismiss = onDismiss,
            ),
        )
    }

    fun info(
        message: String? = null,
        title: String? = null,
        style: ToastStyle = ToastStyle.Elevated,
        showClose: Boolean = false,
        position: ToastPosition? = null,
        fullWidth: Boolean? = null,
        enterAnimation: ToastEnterAnimation? = null,
        durationMs: Long = 3_000L,
        autoDismiss: Boolean = true,
        onDismiss: (() -> Unit)? = null,
    ) {
        ToastManager.show(
            ToastConfig(
                title = title.asToastText(),
                message = message.asToastText(),
                type = ToastType.Info,
                style = style,
                showClose = showClose,
                position = position,
                fullWidth = fullWidth,
                enterAnimation = enterAnimation,
                durationMs = durationMs,
                autoDismiss = autoDismiss,
                onDismiss = onDismiss,
            ),
        )
    }

    fun custom(config: ToastConfig) {
        ToastManager.show(config)
    }

    fun dismiss() {
        ToastManager.dismiss()
    }

    private fun String?.asToastText(): String? =
        this?.trim()?.takeIf { it.isNotEmpty() }
}
