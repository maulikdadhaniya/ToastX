# 🚀 ToastX – Compose Multiplatform Toast Library (Task.md)

## 📌 Overview

Build **ToastX**, a **plug & play toast notification library** for Compose Multiplatform that works across:

* Android
* iOS
* Desktop
* Web (WASM)

👉 Core Principle:
**Only ONE toast visible at a time (no queue system)**
New toast replaces the current one instantly.

---

## 📊 All variants × all types

`ToastStyle` picks the **layout** (banner, soft, glass, …). `ToastType` picks **palette, icon, and glow** (neutral, info, success, warning, error). Every cell below is valid: use `ToastConfig(type = …, style = …)`.

| Style ↓ / Type → | Neutral | Info | Success | Warning | Error |
|------------------|:-------:|:----:|:-------:|:-------:|:-----:|
| **Banner** | ✓ | ✓ | ✓ | ✓ | ✓ |
| **Soft** | ✓ | ✓ | ✓ | ✓ | ✓ |
| **Minimal** | ✓ | ✓ | ✓ | ✓ | ✓ |
| **Outline** | ✓ | ✓ | ✓ | ✓ | ✓ |
| **Elevated** | ✓ | ✓ | ✓ | ✓ | ✓ |
| **OuterShadow** | ✓ | ✓ | ✓ | ✓ | ✓ |
| **BottomSheet** | ✓ | ✓ | ✓ | ✓ | ✓ |
| **Gradient** | ✓ | ✓ | ✓ | ✓ | ✓ |
| **AnimatedBorder** | ✓ | ✓ | ✓ | ✓ | ✓ |
| **Glass** | ✓ | ✓ | ✓ | ✓ | ✓ |

**Totals:** 10 styles × 5 types = **50** combinations.

---

# 🎯 Core Requirements

## 🧠 Behavior Rules (CRITICAL)

* ✅ Only one toast visible at a time
* ✅ New toast replaces old toast immediately
* ✅ Timer resets on new toast
* ✅ Manual dismiss supported
* ✅ Optional swipe-to-dismiss

---

## 🎨 Variants (Based on Designs)

Implement **minimum 6 UI styles**:

### 1. CTA Banner Toast

* Title + message + action button
* Gradient / colored background

### 2. Soft Success Toast

* Icon + subtle background + border

### 3. Info Toast

* Clean blue style

### 4. Warning Toast

* Orange alert style

### 5. Error Toast

* Red strong feedback

### 6. Neutral Toast

* Minimal gray default

---

## ⚙️ States (All Variants Support)

* `Success`
* `Error`
* `Warning`
* `Info`

---

## 🧱 Customization (MANDATORY)

Every toast must support:

### Content

* `title` (optional)
* `message` (optional)
* `icon` (optional / toggle)
* `action button` (optional)

### Behavior

* `duration`
* `autoDismiss`
* `onClick`
* `onDismiss`

### Style

* background color
* border color
* icon color
* shape (rounded / pill)
* padding / spacing
* elevation / shadow

👉 If user does NOT provide → use default values

---

## 🧠 Default Behavior

```kotlin
ToastX.show("Saved successfully")
```

Defaults:

* Info state
* Soft style
* Default icon
* 3s duration

---

# 🧱 Architecture

```text
ToastManager (Single State)
        ↓
StateFlow<ToastData?>
        ↓
ToastHost (Overlay)
        ↓
ToastRenderer (Variant Switch)
        ↓
ToastItem (UI)
```

---

# ⚙️ Phase 1: Core Setup

## ✅ Task 1: Project Structure

```text
toastx-core
toastx-ui
toastx-sample
```

---

## ✅ Task 2: Data Models

```kotlin
sealed class ToastState {
    object Success : ToastState()
    object Error : ToastState()
    object Warning : ToastState()
    object Info : ToastState()
}

sealed class ToastStyle {
    object Banner : ToastStyle()
    object Soft : ToastStyle()
    object Minimal : ToastStyle()
    object Elevated : ToastStyle()
    object Outline : ToastStyle()
    object Glass : ToastStyle()
}

data class ToastConfig(
    val id: String = UUID.randomUUID().toString(),
    val title: String? = null,
    val message: String? = null,
    val state: ToastState = ToastState.Info,
    val style: ToastStyle = ToastStyle.Soft,
    val showIcon: Boolean = true,
    val duration: Long = 3000,
    val action: ToastAction? = null,
    val onDismiss: (() -> Unit)? = null
)

data class ToastAction(
    val label: String,
    val onClick: () -> Unit
)
```

---

## ✅ Task 3: ToastManager (NO QUEUE)

```kotlin
object ToastManager {

    private val _toast = MutableStateFlow<ToastConfig?>(null)
    val toast: StateFlow<ToastConfig?> = _toast

    private var job: Job? = null

    fun show(config: ToastConfig) {
        job?.cancel()

        _toast.value = config

        job = CoroutineScope(Dispatchers.Main).launch {
            delay(config.duration)
            dismiss()
        }
    }

    fun dismiss() {
        job?.cancel()
        _toast.value?.onDismiss?.invoke()
        _toast.value = null
    }
}
```

**Acceptance**

* Only one toast active
* New toast replaces old
* Timer resets

---

# 🎨 Phase 2: UI Layer

## ✅ Task 4: ToastHost

* Global overlay composable
* Positioned top-center / top-end

```kotlin
@Composable
fun ToastHost(content: @Composable () -> Unit)
```

---

## ✅ Task 5: ToastRenderer

```kotlin
@Composable
fun ToastRenderer(config: ToastConfig)
```

* Switch UI based on:

  * `ToastStyle`
  * `ToastState`

---

## ✅ Task 6: Implement Variants

```kotlin
@Composable fun BannerToast()
@Composable fun SoftToast()
@Composable fun MinimalToast()
@Composable fun OutlineToast()
@Composable fun ElevatedToast()
@Composable fun GlassToast()
```

---

# ✨ Phase 3: Animation

## ✅ Task 7: Enter / Exit Animation

Use:

* slide (top)
* fade
* scale (optional)

---

## ✅ Task 8: Replace Animation

When new toast arrives:

* old toast animates out
* new toast animates in

---

## ✅ Task 9: Swipe to Dismiss

* Horizontal drag gesture
* Velocity-based dismiss

---

# 🎨 Phase 4: Theme System

## ✅ Task 10: ToastTheme

```kotlin
data class ToastTheme(
    val successColor: Color,
    val errorColor: Color,
    val warningColor: Color,
    val infoColor: Color
)
```

Use:

```kotlin
val LocalToastTheme = compositionLocalOf { defaultTheme }
```

---

# ⚡ Phase 5: Public API

## ✅ Task 11: Developer-Friendly API

```kotlin
ToastX.show("Message")

ToastX.success("Saved")

ToastX.error("Failed")

ToastX.warning("Check input")

ToastX.info("FYI")

ToastX.custom(
    ToastConfig(
        title = "Upload",
        message = "Completed",
        style = ToastStyle.Banner
    )
)
```

---

# 🧪 Phase 6: Testing

## ✅ Unit Tests

* Replace logic
* Dismiss logic
* Timer reset

## ✅ UI Preview

* All variants
* All states

---

# 🌍 Phase 7: Multiplatform

Ensure:

* No Android-only APIs
* Works on all targets

---

# 🚀 Phase 8: Advanced Features (Optional)

* Duplicate suppression
* Smart replace logic
* Progress indicator
* Gesture physics animation
* Center toast mode
* Analytics hooks

---

# 📦 Phase 9: Packaging

* Publish as library
* Add README
* Add GIF previews
* Add sample app

---

# 🎯 Definition of Done

* ✅ 6 variants implemented
* ✅ 4 states supported
* ✅ Fully customizable
* ✅ Default fallback working
* ✅ Single-toast system working
* ✅ Smooth animations
* ✅ Cross-platform support
* ✅ Clean developer API

---

# 💡 Vision

ToastX should evolve into:

👉 Cross-platform UX feedback system
👉 Notification framework
👉 UI toolkit component

---

# ⭐ Tagline

Plug & play toast notifications for Android, iOS, Desktop, and Web — powered by Compose Multiplatform.

---
