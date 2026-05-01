package com.maulik.toastx

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.maulik.toastx.theme.ToastThemeDefaults
import com.maulik.toastx.ui.ToastHost
import io.github.alexzhirkevich.compottie.Compottie
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import toastx.composeapp.generated.resources.Res

private const val DemoLoginEmail = "demo@toastx.dev"
private const val DemoLoginPasswordOk = "secret"
private const val DemoLoginPasswordTemp = "temp"

private const val TitleSuccess = "Success"
private const val TitleError = "Error"
private const val TitleWarning = "Warning"
private const val TitleInfo = "Info"

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

private fun toastDurationSec(type: ToastType): Int =
    when (type) {
        ToastType.Error -> 4
        ToastType.Warning -> 4
        else -> 3
    }

@Composable
private fun LoginLottieIcon(
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
private fun LoginScreen() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val scheme = MaterialTheme.colorScheme

    val lottieIcon: @Composable (ToastType) -> Unit = { t ->
        LoginLottieIcon(t, Modifier.size(36.dp))
    }

    fun showLoginToast(
        type: ToastType,
        title: String,
        message: String,
    ) {
        ToastX.custom(
            ToastConfig(
                title = title,
                message = message,
                type = type,
                style = ToastStyle.Elevated,
                showIcon = true,
                showClose = false,
                iconContent = lottieIcon,
                position = ToastPosition.BottomCenter,
                durationSec = toastDurationSec(type),
            ),
        )
    }

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Sign in",
            style = MaterialTheme.typography.headlineMedium,
            color = scheme.onBackground,
        )
        Text(
            text =
                "Success: $DemoLoginEmail + \"$DemoLoginPasswordOk\". " +
                    "Warning: same email + \"$DemoLoginPasswordTemp\". " +
                    "Error: wrong or empty fields. Info: Account help.",
            style = MaterialTheme.typography.bodySmall,
            color = scheme.onBackground.copy(alpha = 0.72f),
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
        )
        Button(
            onClick = {
                when {
                    email.isBlank() || password.isBlank() ->
                        showLoginToast(
                            ToastType.Error,
                            TitleError,
                            "Please enter email and password.",
                        )

                    email.equals(DemoLoginEmail, ignoreCase = true) &&
                        password == DemoLoginPasswordTemp ->
                        showLoginToast(
                            ToastType.Warning,
                            TitleWarning,
                            "You are using a temporary password. Change it after signing in.",
                        )

                    email.equals(DemoLoginEmail, ignoreCase = true) &&
                        password == DemoLoginPasswordOk ->
                        showLoginToast(
                            ToastType.Success,
                            TitleSuccess,
                            "Signed in successfully.",
                        )

                    else ->
                        showLoginToast(
                            ToastType.Error,
                            TitleError,
                            "Invalid email or password.",
                        )
                }
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Sign in")
        }
        TextButton(
            onClick = {
                showLoginToast(
                    ToastType.Info,
                    TitleInfo,
                    "Contact your administrator if you need an account or SSO access.",
                )
            },
        ) {
            Text("Account / SSO help")
        }
    }
}

@Composable
fun App() {
    var useDarkTheme by remember { mutableStateOf(false) }

    val materialScheme =
        if (useDarkTheme) {
            darkColorScheme()
        } else {
            lightColorScheme()
        }

    ToastHost(
        lightTheme = ToastThemeDefaults.light,
        darkTheme = ToastThemeDefaults.dark,
        useDarkTheme = useDarkTheme,
    ) {
        MaterialTheme(colorScheme = materialScheme) {
            val scheme = MaterialTheme.colorScheme
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(scheme.background),
            ) {
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
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
                            .fillMaxSize()
                            .padding(top = 48.dp),
                    contentAlignment = Alignment.TopCenter,
                ) {
                    LoginScreen()
                }
            }
        }
    }
}
