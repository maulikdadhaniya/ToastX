package com.maulik.toastx

import kotlinx.coroutines.CoroutineDispatcher

/** UI / composition-safe dispatcher for [ToastManager] state and callbacks. */
internal expect fun toastManagerDispatcher(): CoroutineDispatcher
