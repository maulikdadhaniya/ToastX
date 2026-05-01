package com.maulik.toastx

import kotlin.random.Random
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

object ToastManager {
    private var supervisorJob = SupervisorJob()
    private var dispatcher: CoroutineDispatcher = toastManagerDispatcher()
    private var scope = CoroutineScope(supervisorJob + dispatcher)

    private val _toast = MutableStateFlow<ToastConfig?>(null)
    val toast: StateFlow<ToastConfig?> = _toast.asStateFlow()

    private var dismissJob: Job? = null

    /**
     * Test-only: routes scheduled work onto [dispatcher] (e.g. [kotlinx.coroutines.test.StandardTestDispatcher])
     * and clears any active toast. Call [resetAfterTest] when finished.
     */
    internal fun prepareTestCoroutineEnvironment(dispatcher: CoroutineDispatcher) {
        dismissJob?.cancel()
        dismissJob = null
        _toast.value = null
        supervisorJob.cancel()
        supervisorJob = SupervisorJob()
        this.dispatcher = dispatcher
        scope = CoroutineScope(supervisorJob + dispatcher)
    }

    internal fun resetAfterTest() {
        dismissJob?.cancel()
        dismissJob = null
        _toast.value = null
        supervisorJob.cancel()
        supervisorJob = SupervisorJob()
        dispatcher = toastManagerDispatcher()
        scope = CoroutineScope(supervisorJob + dispatcher)
    }

    fun show(config: ToastConfig) {
        val hasTitle = !config.title.isNullOrBlank()
        val hasMessage = !config.message.isNullOrBlank()
        require(hasTitle || hasMessage) {
            "ToastConfig requires a non-blank title and/or message"
        }

        dismissJob?.cancel()
        dismissJob = null

        val withId =
            if (config.id.isEmpty()) {
                config.copy(id = newToastId())
            } else {
                config
            }
        _toast.value = withId

        if (withId.autoDismiss && withId.durationMs > 0L) {
            dismissJob =
                scope.launch {
                    delay(withId.durationMs)
                    dismiss()
                }
        }
    }

    fun dismiss() {
        dismissJob?.cancel()
        dismissJob = null
        val current = _toast.value
        _toast.value = null
        if (current != null) {
            try {
                current.onDismiss?.invoke()
            } catch (_: Throwable) {
            }
        }
    }

    /**
     * Clears the current toast because [ToastHost] was removed. Does not invoke [ToastConfig.onDismiss]
     * so UI teardown callbacks cannot run against a disposed hierarchy.
     */
    fun dismissBecauseHostRemoved() {
        dismissJob?.cancel()
        dismissJob = null
        _toast.value = null
    }

    private fun newToastId(): String =
        buildString {
            append(Random.nextLong().toString(16))
            append('-')
            append(Random.nextLong().toString(16))
        }
}
