package ru.ansya.intervaltimerapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val completedRounds = intent.getIntExtra(EXTRA_COMPLETED_ROUNDS, 0)
        val totalRounds = intent.getIntExtra(EXTRA_TOTAL_ROUNDS, 0)

        findViewById<TextView>(R.id.resultTextView).text =
            getString(R.string.result_summary, completedRounds, totalRounds)
        findViewById<Button>(R.id.newWorkoutButton).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    companion object {
        const val EXTRA_COMPLETED_ROUNDS = "extra_completed_rounds"
        const val EXTRA_TOTAL_ROUNDS = "extra_total_rounds"
    }
}
