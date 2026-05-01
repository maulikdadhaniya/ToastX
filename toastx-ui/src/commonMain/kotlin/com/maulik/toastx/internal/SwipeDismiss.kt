package com.maulik.toastx.internal

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import kotlin.math.absoluteValue
import kotlin.math.roundToInt
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun Modifier.swipeToDismissToast(
    enabled: Boolean,
    onDismiss: () -> Unit,
): Modifier =
    composed {
        if (!enabled) return@composed this
        val offsetState = remember { mutableFloatStateOf(0f) }
        val scope = rememberCoroutineScope()
        var swipeAnimationJob by remember { mutableStateOf<Job?>(null) }
        this
            .offset { IntOffset(offsetState.floatValue.roundToInt(), 0) }
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragStart = {
                        swipeAnimationJob?.cancel()
                        swipeAnimationJob = null
                    },
                    onHorizontalDrag = { _, dragAmount ->
                        offsetState.floatValue += dragAmount
                    },
                    onDragEnd = {
                        val threshold = (size.width * 0.22f).coerceAtLeast(1f)
                        val current = offsetState.floatValue
                        val width = size.width.toFloat()
                        swipeAnimationJob =
                            scope.launch {
                                try {
                                    if (current.absoluteValue > threshold) {
                                        val target =
                                            if (current > 0) width * 1.35f else -width * 1.35f
                                        val anim = Animatable(current)
                                        anim.animateTo(
                                            targetValue = target,
                                            animationSpec =
                                                tween(
                                                    durationMillis = 220,
                                                    easing = FastOutSlowInEasing,
                                                ),
                                        ) {
                                            offsetState.floatValue = value
                                        }
                                        try {
                                            onDismiss()
                                        } catch (_: Throwable) {
                                        }
                                    } else {
                                        val anim = Animatable(current)
                                        anim.animateTo(
                                            targetValue = 0f,
                                            animationSpec =
                                                spring(
                                                    dampingRatio = Spring.DampingRatioNoBouncy,
                                                    stiffness = Spring.StiffnessMediumLow,
                                                ),
                                        ) {
                                            offsetState.floatValue = value
                                        }
                                    }
                                } catch (_: CancellationException) {
                                }
                            }
                    },
                    onDragCancel = {
                        swipeAnimationJob?.cancel()
                        val current = offsetState.floatValue
                        swipeAnimationJob =
                            scope.launch {
                                try {
                                    val anim = Animatable(current)
                                    anim.animateTo(
                                        targetValue = 0f,
                                        animationSpec =
                                            spring(
                                                dampingRatio = Spring.DampingRatioNoBouncy,
                                                stiffness = Spring.StiffnessMediumLow,
                                            ),
                                    ) {
                                        offsetState.floatValue = value
                                    }
                                } catch (_: CancellationException) {
                                }
                            }
                    },
                )
            }
    }
