package com.maulik.toastx

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maulik.toastx.theme.LocalToastTheme
import com.maulik.toastx.theme.ToastTheme
import com.maulik.toastx.theme.ToastThemeDefaults
import com.maulik.toastx.ui.ToastRenderer
import io.github.alexzhirkevich.compottie.Compottie
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import toastx.composeapp.generated.resources.Res

private const val DemoSuccessTitle = "Success"
private const val DemoSuccessMessage = "User Login Sucessfully"
private const val DemoErrorTitle = "Error"
private const val DemoErrorMessage = "User Registration failed."
private const val DemoWarningTitle = "Warning"
private const val DemoWarningMessage = "Your password need to change"
private const val DemoInfoTitle = "Info"
private const val DemoInfoMessage = "Please Contact Administator"

/** Per-type Lottie assets under composeResources/files. */
private fun lottieAssetForType(type: ToastType): String =
    when (type) {
        ToastType.Success -> "files/lottie_success.json"
        ToastType.Error -> "files/lottie_error.json"
        ToastType.Warning -> "files/lottie_warning.json"
        ToastType.Info -> "files/lottie_info.json"
    }

private fun lottieUsesExpressions(type: ToastType): Boolean =
    when (type) {
        ToastType.Success -> false
        else -> true
    }

private val galleryStyles =
    listOf(
        ToastStyle.Soft,
        ToastStyle.Minimal,
        ToastStyle.Outline,
        ToastStyle.Elevated,
        ToastStyle.OuterShadow,
        ToastStyle.BottomSheet,
        ToastStyle.Gradient,
        ToastStyle.AnimatedBorder,
        ToastStyle.Glass,
    )

private fun styleLabel(style: ToastStyle): String =
    when (style) {
        ToastStyle.Soft -> "Soft"
        ToastStyle.Minimal -> "Minimal"
        ToastStyle.Outline -> "Outline"
        ToastStyle.Elevated -> "Elevated"
        ToastStyle.OuterShadow -> "OuterShadow"
        ToastStyle.BottomSheet -> "BottomSheet"
        ToastStyle.Gradient -> "Gradient"
        ToastStyle.AnimatedBorder -> "AnimatedBorder"
        ToastStyle.Glass -> "Glass"
    }

private fun demoTitle(type: ToastType): String =
    when (type) {
        ToastType.Success -> DemoSuccessTitle
        ToastType.Warning -> DemoWarningTitle
        ToastType.Error -> DemoErrorTitle
        ToastType.Info -> DemoInfoTitle
    }

private fun demoMessage(type: ToastType): String =
    when (type) {
        ToastType.Success -> DemoSuccessMessage
        ToastType.Warning -> DemoWarningMessage
        ToastType.Error -> DemoErrorMessage
        ToastType.Info -> DemoInfoMessage
    }

@Composable
private fun DemoLottieIcon(
    type: ToastType,
    modifier: Modifier = Modifier,
) {
    val path = lottieAssetForType(type)
    var json: String? by remember(path) { mutableStateOf(null) }
    LaunchedEffect(path) {
        json =
            try {
                Res.readBytes(path).decodeToString()
            } catch (_: Throwable) {
                null
            }
    }
    val data = json
    if (data.isNullOrEmpty()) {
        Box(modifier)
        return
    }
    key(data) {
        val compositionResult =
            rememberLottieComposition(data) {
                LottieCompositionSpec.JsonString(data)
            }
        val composition by compositionResult
        if (compositionResult.isFailure || composition == null) {
            Box(modifier)
        } else {
            Image(
                painter =
                    rememberLottiePainter(
                        composition = composition,
                        iterations = Compottie.IterateForever,
                        isPlaying = true,
                        enableExpressions = lottieUsesExpressions(type),
                    ),
                contentDescription = null,
                modifier = modifier,
            )
        }
    }
}

@Composable
private fun demoToastConfig(
    type: ToastType,
    style: ToastStyle,
): ToastConfig {
    val lottieSlot: @Composable (ToastType) -> Unit =
        { t ->
            DemoLottieIcon(t, Modifier.size(36.dp))
        }
    return ToastConfig(
        title = demoTitle(type),
        message = demoMessage(type),
        type = type,
        style = style,
        showIcon = true,
        showClose = false,
        iconContent = lottieSlot,
        position = ToastPosition.BottomCenter,
        fullWidth =
            when (style) {
                ToastStyle.BottomSheet -> true
                else -> null
            },
    )
}

@Composable
fun App() {
    var useDarkTheme by remember { mutableStateOf(false) }
    var sectionVisible by remember {
        mutableStateOf(galleryStyles.associateWith { true })
    }

    val toastTheme: ToastTheme =
        if (useDarkTheme) {
            ToastThemeDefaults.dark
        } else {
            ToastThemeDefaults.light
        }

    val materialScheme =
        if (useDarkTheme) {
            darkColorScheme()
        } else {
            lightColorScheme()
        }

    val titleBrush =
        if (useDarkTheme) {
            Brush.linearGradient(
                colors =
                    listOf(
                        Color(0xFF93C5FD),
                        Color(0xFFC4B5FD),
                        Color(0xFFF472B6),
                    ),
            )
        } else {
            Brush.linearGradient(
                colors =
                    listOf(
                        Color(0xFF4F46E5),
                        Color(0xFF7C3AED),
                        Color(0xFFDB2777),
                    ),
            )
        }

    CompositionLocalProvider(LocalToastTheme provides toastTheme) {
        MaterialTheme(colorScheme = materialScheme) {
            val scheme = MaterialTheme.colorScheme
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(scheme.background)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 12.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Switch(
                        checked = useDarkTheme,
                        onCheckedChange = { useDarkTheme = it },
                    )
                }

                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "ToastX",
                        style =
                            TextStyle(
                                brush = titleBrush,
                                fontSize = 44.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 4.sp,
                            ),
                    )
                }

                galleryStyles.forEach { style ->
                    val visible = sectionVisible[style] ?: true
                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = styleLabel(style),
                            style = MaterialTheme.typography.titleMedium,
                            color = scheme.onBackground,
                        )
                        Switch(
                            checked = visible,
                            onCheckedChange = { on ->
                                sectionVisible = sectionVisible + (style to on)
                            },
                        )
                    }

                    if (visible) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(14.dp),
                        ) {
                            ToastType.entries.forEach { type ->
                                ToastRenderer(
                                    config = demoToastConfig(type, style),
                                    hostPosition = ToastPosition.BottomCenter,
                                    swipeToDismiss = false,
                                    modifier = Modifier.fillMaxWidth(),
                                )
                            }
                        }
                        Spacer(Modifier.height(24.dp))
                        HorizontalDivider(color = scheme.outline.copy(alpha = 0.35f))
                        Spacer(Modifier.height(8.dp))
                    }
                }

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}
