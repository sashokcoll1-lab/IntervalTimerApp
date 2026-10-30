package ru.ansya.intervaltimerapp

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.ceil

class TimerActivity : AppCompatActivity() {
    private lateinit var phaseTextView: TextView
    private lateinit var roundTextView: TextView
    private lateinit var countdownTextView: TextView
    private lateinit var startPauseButton: Button
    private lateinit var resetButton: Button
    private lateinit var backToSettingsButton: Button

    private var workSeconds = 0
    private var restSeconds = 0
    private var totalRounds = 0
    private var currentRound = 1
    private var completedRounds = 0
    private var remainingSeconds = 0
    private var isWorkPhase = true
    private var isRunning = false
    private var countdownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        workSeconds = intent.getIntExtra(EXTRA_WORK_SECONDS, 30)
        restSeconds = intent.getIntExtra(EXTRA_REST_SECONDS, 15)
        totalRounds = intent.getIntExtra(EXTRA_ROUND_COUNT, 5)

        phaseTextView = findViewById(R.id.phaseTextView)
        roundTextView = findViewById(R.id.roundTextView)
        countdownTextView = findViewById(R.id.countdownTextView)
        startPauseButton = findViewById(R.id.startPauseButton)
        resetButton = findViewById(R.id.resetButton)
        backToSettingsButton = findViewById(R.id.backToSettingsButton)

        startPauseButton.setOnClickListener {
            if (isRunning) pauseTimer() else startTimer()
        }
        resetButton.setOnClickListener { resetWorkout() }
        backToSettingsButton.setOnClickListener { finish() }

        resetWorkout()
    }

    private fun startTimer() {
        if (remainingSeconds <= 0) return

        isRunning = true
        updateUi()
        countdownTimer = object : CountDownTimer(remainingSeconds * 1000L, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                remainingSeconds = ceil(millisUntilFinished / 1000.0).toInt()
                updateUi()
            }

            override fun onFinish() {
                remainingSeconds = 0
                isRunning = false
                moveToNextPhase()
            }
        }.start()
    }

    private fun pauseTimer() {
        countdownTimer?.cancel()
        countdownTimer = null
        isRunning = false
        updateUi()
    }

    private fun resetWorkout() {
        countdownTimer?.cancel()
        countdownTimer = null
        currentRound = 1
        completedRounds = 0
        isWorkPhase = true
        isRunning = false
        remainingSeconds = workSeconds
        updateUi()
    }

    private fun moveToNextPhase() {
        if (isWorkPhase) {
            completedRounds++
            if (completedRounds == totalRounds) {
                showResult()
                return
            }
            isWorkPhase = false
            remainingSeconds = restSeconds
        } else {
            currentRound++
            isWorkPhase = true
            remainingSeconds = workSeconds
        }
        startTimer()
    }

    private fun showResult() {
        startActivity(
            Intent(this, ResultActivity::class.java).apply {
                putExtra(ResultActivity.EXTRA_COMPLETED_ROUNDS, completedRounds)
                putExtra(ResultActivity.EXTRA_TOTAL_ROUNDS, totalRounds)
            },
        )
        finish()
    }

    private fun updateUi() {
        phaseTextView.text = getString(
            if (isWorkPhase) R.string.work_phase else R.string.rest_phase,
        )
        roundTextView.text = getString(R.string.round_progress, currentRound, totalRounds)
        countdownTextView.text = getString(
            R.string.countdown_format,
            remainingSeconds / 60,
            remainingSeconds % 60,
        )
        startPauseButton.text = getString(
            if (isRunning) R.string.pause_timer else R.string.start_timer,
        )
    }

    override fun onDestroy() {
        countdownTimer?.cancel()
        super.onDestroy()
    }

    companion object {
        const val EXTRA_WORK_SECONDS = "extra_work_seconds"
        const val EXTRA_REST_SECONDS = "extra_rest_seconds"
        const val EXTRA_ROUND_COUNT = "extra_round_count"
    }
}
