# MAD302-01 Android Development – LAB 3

## Overview
This Android app demonstrates:
- Asynchronous operations using **Kotlin Coroutines** (`lifecycleScope.launch` and `delay(2000)`).
- Requesting **Camera permission** at runtime using `ActivityResultContracts.RequestPermission()`.
- Simulating an API call and displaying the result or error in the UI.
- Proper error handling for:
  - Permission denied.
  - Simulated network/side‑effect error.

The “fetched data” is **mock text** (not real network data), as required for this lab.

---

## Features

- **"Fetch Data" Button**  
  - Triggers a simulated API call after checking Camera permission.
- **Camera Permission**  
  - Uses `ActivityResultContracts.RequestPermission()` for `CAMERA`.
  - Shows a message if permission is denied.
- **Async Operation with Coroutines**  
  - Simulates network delay via `delay(2000)` inside a coroutine launched with `lifecycleScope.launch`.
- **Error Handling**  
  - Permission denied → shows a user‑friendly message.
  - Simulated error in `simulateApiCall()` → caught with `try‑catch` and shown in the UI.
- **User Interface**  
  - `Button` labeled `"Fetch Data"`.
  - `TextView` to display success or error message.

---

## Setup

### 1. Android Studio
- Open or import the project in **Android Studio**.
- Ensure the package name matches your project (e.g., `com.example.mad302_lab3`).

### 2. Dependencies
- This app uses standard AndroidX and Kotlin Coroutines:
  - `androidx.lifecycle:lifecycle-runtime-ktx` (for `lifecycleScope`).
  - `androidx.activity:activity-ktx` (for `ActivityResultContracts`).

No extra libraries are required for this lab.

### 3. Permissions
- In `AndroidManifest.xml`, add:
  ```xml
  <uses-permission android:name="android.permission.CAMERA" />
  ```

### 4. Layout
- `activity_main.xml` contains:
  - A `Button` with ID `buttonFetch`.
  - A `TextView` with ID `textViewResult`.

---

## How It Works

1. Click **"Fetch Data"**.
2. App requests **Camera permission** (if not already granted).
3. If permission is **granted**:
   - A coroutine runs `simulateApiCall()`, which:
     - Waits `delay(2000)` to simulate a network round‑trip.
     - Returns a success message.
4. If permission is **denied**:
   - The app immediately returns an error message.
5. The result (success or error) is displayed in the `TextView`.

---

## File Structure

- `MainActivity.kt`  
  - Main logic: permission handling, coroutine, and UI update.
- `activity_main.xml`  
  - Simple layout with `Button` and `TextView`.
- `AndroidManifest.xml`  
  - Contains `CAMERA` permission declaration.

---

## Notes

- This is a **mock API** implementation; no real HTTP or JSON is used.
- Designed to meet LAB 3 requirements:
  - Async operation with Coroutines.
  - Camera permission handling.
  - UI display of result and error messages.
  - Proper documentation and comments in the code.

