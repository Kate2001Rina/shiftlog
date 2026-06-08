# ShiftLog

**ShiftLog** is a native Android app for shift workers to track working hours, view monthly stats, and export data — built with modern Android stack.

## Download

[⬇️ Download APK (v1.0.0)](https://github.com/Kate2001Rina/shiftLog/releases/tag/v1.0.0)

> To install: enable **Install from unknown sources** in Android Settings → Security

---

## Features

- **One-tap shift tracking** — start and end shifts with animated timer
- **Monthly calendar** — visual overview of all worked days
- **Statistics** — weekly/monthly hours, average shift duration, overtime alerts
- **CSV export** — share your shifts as a spreadsheet via any app
- **Background sync** — WorkManager syncs data periodically when online
- **Dark mode** — full Material 3 dark/light theme support

---

## 🛠 Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Architecture | MVVM + Clean Architecture |
| Async | Coroutines + StateFlow + Flow |
| Local DB | Room (SQLite) |
| DI | Hilt |
| Background | WorkManager |
| Network | Ktor Client |
| Build | Gradle with Version Catalogs |

---

## Architecture

The project follows **Clean Architecture** with three layers:

```
presentation/   ← Compose UI, ViewModels, StateFlow
domain/         ← Use Cases, Repository interfaces, Models (pure Kotlin)
data/           ← Room DB, Ktor API, Repository implementations
di/             ← Hilt modules
```

Key patterns used:
- **Sealed interfaces** for UI state (`HomeUiState`, `ExportState`)
- **`flatMapLatest`** for reactive month switching in CalendarViewModel
- **`stateIn` with `WhileSubscribed(5000)`** for lifecycle-aware flows
- **`FileProvider`** for secure CSV file sharing

---

## Build & Run

1. Clone the repo:
```bash
git clone https://github.com/Kate2001Rina/shiftLog.git
```
2. Open in **Android Studio Hedgehog** or newer
3. Run on emulator or device (minSdk 26)

---

## License

MIT License — free to use and modify.
