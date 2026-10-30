package ru.ansya.intervaltimerapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val workDurationInput = findViewById<EditText>(R.id.workDurationEditText)
        val restDurationInput = findViewById<EditText>(R.id.restDurationEditText)
        val roundCountInput = findViewById<EditText>(R.id.roundCountEditText)
        val preferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE)

        workDurationInput.setText(preferences.getInt(KEY_WORK_SECONDS, DEFAULT_WORK_SECONDS).toString())
        restDurationInput.setText(preferences.getInt(KEY_REST_SECONDS, DEFAULT_REST_SECONDS).toString())
        roundCountInput.setText(preferences.getInt(KEY_ROUND_COUNT, DEFAULT_ROUND_COUNT).toString())

        findViewById<Button>(R.id.startTimerButton).setOnClickListener {
            val workSeconds = workDurationInput.text.toString().toIntOrNull() ?: 0
            val restSeconds = restDurationInput.text.toString().toIntOrNull() ?: 0
            val roundCount = roundCountInput.text.toString().toIntOrNull() ?: 0

            if (workSeconds !in 1..3600 || restSeconds !in 1..3600 || roundCount !in 1..99) {
                Toast.makeText(this, R.string.invalid_settings, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            preferences.edit()
                .putInt(KEY_WORK_SECONDS, workSeconds)
                .putInt(KEY_REST_SECONDS, restSeconds)
                .putInt(KEY_ROUND_COUNT, roundCount)
                .apply()

            val timerIntent = Intent(this, TimerActivity::class.java).apply {
                putExtra(TimerActivity.EXTRA_WORK_SECONDS, workSeconds)
                putExtra(TimerActivity.EXTRA_REST_SECONDS, restSeconds)
                putExtra(TimerActivity.EXTRA_ROUND_COUNT, roundCount)
            }
            startActivity(timerIntent)
        }

        findViewById<Button>(R.id.aboutButton).setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
        }
    }

    companion object {
        private const val PREFERENCES_NAME = "workout_preferences"
        private const val KEY_WORK_SECONDS = "work_seconds"
        private const val KEY_REST_SECONDS = "rest_seconds"
        private const val KEY_ROUND_COUNT = "round_count"
        private const val DEFAULT_WORK_SECONDS = 30
        private const val DEFAULT_REST_SECONDS = 15
        private const val DEFAULT_ROUND_COUNT = 5
    }
}
