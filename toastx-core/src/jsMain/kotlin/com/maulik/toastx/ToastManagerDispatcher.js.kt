package com.maulik.toastx

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

internal actual fun toastManagerDispatcher(): CoroutineDispatcher = Dispatchers.Default
