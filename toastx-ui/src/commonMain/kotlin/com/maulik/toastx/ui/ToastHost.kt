package com.maulik.toastx.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.maulik.toastx.ToastConfig
import com.maulik.toastx.ToastEnterAnimation
import com.maulik.toastx.ToastManager
import com.maulik.toastx.ToastPosition
import com.maulik.toastx.ToastStyle
import com.maulik.toastx.theme.LocalToastTheme
import com.maulik.toastx.theme.ToastTheme
import com.maulik.toastx.theme.ToastThemeDefaults

private val ToastMotionEasing = CubicBezierEasing(0.25f, 0.08f, 0.25f, 1f)

private fun AnimatedContentTransitionScope<ToastConfig?>.toastMotion(
    defaultEnterAnimation: ToastEnterAnimation,
): ContentTransform {
    val anim =
        targetState?.enterAnimation
            ?: initialState?.enterAnimation
            ?: defaultEnterAnimation
    val fadeInSpec = tween<Float>(durationMillis = 120, easing = ToastMotionEasing)
    val fadeOutSpec = tween<Float>(durationMillis = 120, easing = ToastMotionEasing)
    return when (anim) {
        ToastEnterAnimation.FromBottom -> {
            (
                fadeIn(fadeInSpec) +
                    slideInVertically { fullHeight -> fullHeight }
            ) togetherWith (
                fadeOut(fadeOutSpec) +
                    slideOutVertically { fullHeight -> fullHeight }
            )
        }
        ToastEnterAnimation.FromTop -> {
            (
                fadeIn(fadeInSpec) +
                    slideInVertically { fullHeight -> -fullHeight }
            ) togetherWith (
                fadeOut(fadeOutSpec) +
                    slideOutVertically { fullHeight -> -fullHeight }
            )
        }
    }
}

@Composable
fun ToastHost(
    modifier: Modifier = Modifier,
    lightTheme: ToastTheme = ToastThemeDefaults.light,
    darkTheme: ToastTheme = ToastThemeDefaults.dark,
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    position: ToastPosition = ToastPosition.TopCenter,
    defaultEnterAnimation: ToastEnterAnimation = ToastEnterAnimation.FromBottom,
    contentPadding: PaddingValues = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
    swipeToDismiss: Boolean = true,
    maxToastWidth: Dp = 440.dp,
    content: @Composable () -> Unit,
) {
    DisposableEffect(Unit) {
        onDispose {
            ToastManager.dismissBecauseHostRemoved()
        }
    }
    val resolvedTheme = if (useDarkTheme) darkTheme else lightTheme
    CompositionLocalProvider(LocalToastTheme provides resolvedTheme) {
        Box(modifier.fillMaxSize()) {
            content()
            val toast by ToastManager.toast.collectAsState()
            val align = toast?.position ?: position
            val overlayPadding =
                if (toast?.style == ToastStyle.BottomSheet && align.name.startsWith("Bottom")) {
                    PaddingValues(start = 20.dp, top = 20.dp, end = 20.dp, bottom = 0.dp)
                } else {
                    contentPadding
                }
            Box(
                Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.safeDrawing)
                    .padding(overlayPadding),
                contentAlignment = align.alignment,
            ) {
                AnimatedContent(
                    targetState = toast,
                    transitionSpec = { toastMotion(defaultEnterAnimation) },
                    label = "toastx",
                ) { current ->
                    current?.let { cfg ->
                        ToastRenderer(
                            config = cfg,
                            hostPosition = position,
                            swipeToDismiss = swipeToDismiss,
                            maxWidth = maxToastWidth,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }
        }
    }
}
