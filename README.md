<p align="center">
  <img
    src="docs/readme-banner.png"
    alt="ToastX — Compose Multiplatform toast library: styles, Success Error Warning Info, and platform icons"
    width="920"
  />
</p>

<p align="center"><strong>Toast notifications for Compose Multiplatform</strong> · Android · iOS · Desktop · Web (JS &amp; Wasm)</p>

---

**ToastX** shows Material-style toasts from shared Kotlin code. You add **one** `ToastHost` at the root, then call **`ToastX`** from anywhere under it.

<p align="center">
  <img src="docs/ToastX_Demo.png" alt="ToastX demo" width="800" />
</p>

## What you need

- A **Kotlin Multiplatform** module with **Compose Multiplatform** (and the **Compose compiler** plugin for your Kotlin version).
- **`mavenCentral()`** in Gradle repositories.
- **Android `minSdk` 24** if you ship Android.
- **Coroutines** (ToastX uses them for auto-dismiss).

## Add the library

**Maven:** `io.github.maulikdadhaniya:toastx:1.0.2` — see [Releases](https://github.com/maulikdadhaniya/ToastX/releases) for newer versions.

In your shared **`build.gradle.kts`**, inside `kotlin { sourceSets { commonMain.dependencies { … } } }`:

```kotlin
implementation("io.github.maulikdadhaniya:toastx:1.0.2")
```

You also need Compose **runtime**, **foundation**, **material3**, and **ui** on `commonMain` (same as any Compose Multiplatform UI).

## Configure: host + show

**1. Wrap your app once** with `ToastHost` (theme + position + overlay). Put your real UI inside the trailing lambda.

```kotlin
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.maulik.toastx.ToastPosition
import com.maulik.toastx.ToastX
import com.maulik.toastx.theme.ToastThemeDefaults
import com.maulik.toastx.ui.ToastHost

@Composable
fun App() {
    var dark by remember { mutableStateOf(false) }
    val colors = if (dark) darkColorScheme() else lightColorScheme()

    ToastHost(
        lightTheme = ToastThemeDefaults.light,
        darkTheme = ToastThemeDefaults.dark,
        useDarkTheme = dark,
        position = ToastPosition.BottomCenter,
    ) {
        MaterialTheme(colorScheme = colors) {
            // Your screens — call ToastX from here or from child composables
        }
    }
}
```

**2. Show a toast** after the host exists (from any composable under `ToastHost`):

```kotlin
ToastX.success(title = "Done", message = "Saved.")
ToastX.error(title = "Error", message = "Something went wrong.")
ToastX.warning(title = "Heads up", message = "Check this.")
ToastX.info(title = "Info", message = "Details here.")
```

**3. More control** (style, duration in seconds, button, custom icon): use **`ToastX.custom(ToastConfig(…))`**. Set **`style = ToastStyle.…`**, **`durationSec`**, **`action`**, and optionally **`iconContent`** (see below).

## Custom left icon (image / vector)

Use **`ToastConfig.iconContent`**: a **`@Composable (ToastType) -> Unit`**. ToastX places it in the **left** icon area. Use about **`36.dp`** height/width so it lines up with every style.

```kotlin
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.maulik.toastx.ToastConfig
import com.maulik.toastx.ToastType
import com.maulik.toastx.ToastX

ToastX.custom(
    ToastConfig(
        title = "Uploaded",
        message = "File is on the server.",
        type = ToastType.Success,
        showIcon = true,
        iconContent = {
            Image(
                painter = myPainter, // any androidx.compose.ui.graphics.painter.Painter
                contentDescription = null,
                modifier = Modifier.size(36.dp),
            )
        },
    ),
)
```

Omit **`iconContent`** (or pass **`null`**) to keep the **built-in** type icon.

## Lottie (Compottie)

**1.** Add Compottie on **`commonMain`** (version can match this repo’s catalog, e.g. **2.0.3**):

```kotlin
implementation("io.github.alexzhirkevich:compottie:2.0.3")
```

**2.** Pass Lottie into **`iconContent`** (JSON as `String`, e.g. from **`Res.readBytes("files/…").decodeToString()`** or your own loader):

```kotlin
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.maulik.toastx.ToastConfig
import com.maulik.toastx.ToastType
import com.maulik.toastx.ToastX
import io.github.alexzhirkevich.compottie.Compottie
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter

@Composable
fun LottieIcon(json: String, modifier: Modifier = Modifier) {
    key(json) {
        val result by rememberLottieComposition(json) {
            LottieCompositionSpec.JsonString(json)
        }
        val composition by result
        if (!result.isFailure && composition != null) {
            Image(
                painter = rememberLottiePainter(
                    composition = composition,
                    iterations = Compottie.IterateForever,
                    isPlaying = true,
                ),
                contentDescription = null,
                modifier = modifier.size(36.dp),
            )
        }
    }
}

// Example toast
ToastX.custom(
    ToastConfig(
        title = "Syncing",
        message = "Please wait…",
        type = ToastType.Info,
        showIcon = true,
        iconContent = { LottieIcon(json = myLottieJsonString) },
    ),
)
```

## Types and looks (short)

- **`ToastType`**: `Success`, `Error`, `Warning`, `Info` — drives color and default icon.
- **`ToastStyle`**: `Soft`, `Minimal`, `Outline`, `Elevated`, `OuterShadow`, `BottomSheet`, `Gradient`, `AnimatedBorder`, `Glass` — changes the card layout.

## API docs (Dokka) and Git

HTML is generated into **`docs/api/`** (not under `build/`, so Git can track it).

```shell
./gradlew :toastxLib:dokkaGenerate
git add docs/api
git status   # review changed files
git commit -m "Update API documentation (Dokka)"
git push
```

**Tip:** Regenerate after API changes so **`docs/api`** stays in sync. If you use **GitHub Pages**, point the site root at **`docs/api`** (or move/copy `docs/api` into your Pages branch the way your workflow expects).

Browse locally: open **`docs/api/index.html`** in a browser.

## Sample in this repo

The **`composeApp`** module is a small runnable app (style previews + sign-in) you can copy from.

---

[Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html) · [Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform)
