@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

package com.maulik.toastx

import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest

class ToastManagerTest {

    @AfterTest
    fun tearDown() {
        ToastManager.resetAfterTest()
    }

    @Test
    fun titleOnlyToastAccepted() =
        runTest {
            val d = StandardTestDispatcher(testScheduler)
            ToastManager.prepareTestCoroutineEnvironment(d)

            ToastManager.show(ToastConfig(title = "Headline only", message = null))
            assertEquals("Headline only", ToastManager.toast.value?.title)
            assertEquals(null, ToastManager.toast.value?.message)
        }

    @Test
    fun emptyTitleAndMessageRejected() =
        runTest {
            val d = StandardTestDispatcher(testScheduler)
            ToastManager.prepareTestCoroutineEnvironment(d)

            assertFailsWith<IllegalArgumentException> {
                ToastManager.show(ToastConfig(title = null, message = null))
            }
            assertFailsWith<IllegalArgumentException> {
                ToastManager.show(ToastConfig(title = "  ", message = "   "))
            }
        }

    @Test
    fun newToastReplacesPrevious() =
        runTest {
            val d = StandardTestDispatcher(testScheduler)
            ToastManager.prepareTestCoroutineEnvironment(d)

            ToastManager.show(ToastConfig(message = "first"))
            assertEquals("first", ToastManager.toast.value?.message)

            ToastManager.show(ToastConfig(message = "second"))
            assertEquals("second", ToastManager.toast.value?.message)
        }

    @Test
    fun dismissClearsAndRunsCallback() =
        runTest {
            val d = StandardTestDispatcher(testScheduler)
            ToastManager.prepareTestCoroutineEnvironment(d)

            var called = false
            ToastManager.show(
                ToastConfig(
                    message = "x",
                    onDismiss = { called = true },
                ),
            )
            ToastManager.dismiss()

            assertTrue(called)
            assertEquals(null, ToastManager.toast.value)
        }

    @Test
    fun autoDismissClearsAfterDuration() =
        runTest {
            val d = StandardTestDispatcher(testScheduler)
            ToastManager.prepareTestCoroutineEnvironment(d)

            ToastManager.show(
                ToastConfig(
                    message = "tick",
                    durationMs = 1_000L,
                    autoDismiss = true,
                ),
            )
            assertNotNull(ToastManager.toast.value)

            advanceTimeBy(1_000L)
            advanceUntilIdle()

            assertEquals(null, ToastManager.toast.value)
        }

    @Test
    fun newToastResetsAutoDismissTimer() =
        runTest {
            val d = StandardTestDispatcher(testScheduler)
            ToastManager.prepareTestCoroutineEnvironment(d)

            ToastManager.show(
                ToastConfig(
                    message = "a",
                    durationMs = 10_000L,
                    autoDismiss = true,
                ),
            )
            ToastManager.show(
                ToastConfig(
                    message = "b",
                    durationMs = 500L,
                    autoDismiss = true,
                ),
            )

            advanceTimeBy(500L)
            advanceUntilIdle()

            assertEquals(null, ToastManager.toast.value)
        }

    @Test
    fun dismissBecauseHostRemovedSkipsCallback() =
        runTest {
            val d = StandardTestDispatcher(testScheduler)
            ToastManager.prepareTestCoroutineEnvironment(d)

            var called = false
            ToastManager.show(
                ToastConfig(
                    message = "x",
                    onDismiss = { called = true },
                ),
            )
            ToastManager.dismissBecauseHostRemoved()

            assertEquals(null, ToastManager.toast.value)
            assertTrue(!called)
        }

    @Test
    fun onDismissThrowingDoesNotPreventClear() =
        runTest {
            val d = StandardTestDispatcher(testScheduler)
            ToastManager.prepareTestCoroutineEnvironment(d)

            ToastManager.show(
                ToastConfig(
                    message = "x",
                    onDismiss = { error("boom") },
                ),
            )
            ToastManager.dismiss()
            assertEquals(null, ToastManager.toast.value)
        }
}
