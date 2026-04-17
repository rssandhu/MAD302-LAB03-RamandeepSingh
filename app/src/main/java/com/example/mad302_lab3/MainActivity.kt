// *******************************************************************************
// File Header
// Course code and lab number: W2026 MAD302-01 Android Development – LAB 3
// Full name and Student ID: Ramandeep Singh – A00194321
// Date of Submission: 2026-04-16
// Short description:
//   Android app that:
//   - Simulates an API call using delay(2000) via a Coroutine (lifecycleScope.launch).
//   - Requests CAMERA permission (ActivityResultContracts.RequestPermission).
//   - Displays the result in a TextView.
//   - Shows proper messages if permission is denied or network/simulated error occurs.
// *******************************************************************************

package com.example.mad302_lab3

import android.Manifest
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * MainActivity for MAD302-01 Android Development Lab 3.
 *
 * Responsibilities:
 * - Setup UI elements (Button, TextView).
 * - Handle permission request for CAMERA.
 * - Launch coroutine to simulate API call (delay(2000)).
 * - Update UI with result or error message.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var buttonFetch: Button
    private lateinit var textViewResult: TextView

    /**
     * Launches a simulated API call (using delay(2000)) inside a Coroutine.
     *
     * @param permissionGranted true if CAMERA permission is granted; false otherwise.
     * @return Result<String> containing success message or error message.
     */
    private suspend fun simulateApiCall(permissionGranted: Boolean): Result<String> {
        return try {
            // Simulate API/network delay
            delay(2000)

            if (!permissionGranted) {
                // Simulated error when permission is denied
                throw Exception("Permission denied: cannot fetch data")
            }

            // Simulated success case
            Result.success("✅ Data fetched successfully (permission OK)")
        } catch (e: Exception) {
            // Simulated network / generic error
            Result.failure(Exception("❌ Network/simulated error: ${e.message}"))
        }
    }

    /**
     * Updates the TextView with the given text.
     *
     * @param text Text to display in the result TextView.
     */
    private fun updateResultText(text: String) {
        textViewResult.text = text
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Bind UI elements
        buttonFetch = findViewById(R.id.buttonFetch)
        textViewResult = findViewById(R.id.textViewResult)

        /**
         * Contract for requesting CAMERA permission.
         * Uses ActivityResultContracts.RequestPermission().
         */
        val requestCameraPermission =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                if (granted) {
                    // Permission granted → start async fetch
                    lifecycleScope.launch {
                        // Run simulated API call on background but can update UI
                        val result = simulateApiCall(permissionGranted = true)
                        displayResult(result)
                    }
                } else {
                    // Permission denied → show message
                    lifecycleScope.launch {
                        val error = Result.failure<String>(
                            Exception("Permission denied: CAMERA access not granted")
                        )
                        displayResult(error)
                    }
                }
            }

        /**
         * Click listener for "Fetch Data" button.
         * - Request CAMERA permission (ActivityResultLauncher handles callback).
         */
        buttonFetch.setOnClickListener {
            // Remove the `if (!requestCameraPermission.isRegistered)` line; it does not exist
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                Toast.makeText(
                    this,
                    "Camera permission is needed to fetch data.",
                    Toast.LENGTH_SHORT
                ).show()
            }

            // Request permission (contract handles callback)
            requestCameraPermission.launch(Manifest.permission.CAMERA)
        }
    }

    /**
     * Displays the result (success or error) in the TextView.
     * Uses try-catch around the simulated error.
     *
     * @param result Result<String> from simulateApiCall.
     */
    private fun displayResult(result: Result<String>) {
        // Handle simulated error in try-catch
        try {
            when (result) {
                is Result.Success -> {
                    updateResultText(result.data)
                }
                is Result.Failure -> {
                    updateResultText(result.exception.message ?: "Unknown error")
                }
            }
        } catch (e: Exception) {
            updateResultText("Unexpected error in displayResult: ${e.message}")
        }
    }
}

/**
 * Custom Result type for Kotlin (similar to Kotlin std Result).
 * Used to wrap success or failure from the simulated API.
 */
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Failure(val exception: Exception) : Result<Nothing>()

    companion object {
        /**
         * Helper to create a Result.Success instance.
         */
        fun <T> success(data: T): Result<T> = Success(data)

        /**
         * Helper to create a Result.Failure instance.
         */
        fun <T> failure(exception: Exception): Result<T> = Failure(exception)
    }
}