package com.example.simplewordle

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible

const val WORD_LENGTH = 4
const val GUESS_LIMIT = 3

class MainActivity : AppCompatActivity() {

    private lateinit var wordToGuess: String
    private lateinit var editText: EditText
    private lateinit var submitButton: Button
    private lateinit var resetButton: Button
    private lateinit var hintsView: TextView
    private lateinit var answerView: TextView
    private var guessCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        wordToGuess = FourLetterWordList.getRandomFourLetterWord()
        Log.d("Target Word", "word to guess is $wordToGuess")

        hintsView = findViewById<TextView>(R.id.hints_view)
        answerView = findViewById<TextView>(R.id.answer_view)
        editText = findViewById<EditText>(R.id.et_simple)

        // filters that makes edit text only takes in all caps and has max length of WORD_LENGTH
        editText.filters = arrayOf(InputFilter.AllCaps(), InputFilter.LengthFilter(WORD_LENGTH))

        submitButton = findViewById<Button>(R.id.submit_button)
        resetButton = findViewById<Button>(R.id.reset_button)

        submitButton.setOnClickListener{
            onSubmit()
        }

        resetButton.setOnClickListener{
            onReset()
        }
    }

    /**
     * Parameters / Fields:
     *   wordToGuess : String - the target word the user is trying to guess
     *   guess : String - what the user entered as their guess
     *
     * Returns a String of 'O', '+', and 'X', where:
     *   'O' represents the right letter in the right place
     *   '+' represents the right letter in the wrong place
     *   'X' represents a letter not in the target word
     */
    private fun checkGuess(guess: String) : String {
        var result = ""
        for (i in 0..3) {
            if (guess[i] == wordToGuess[i]) {
                result += "O"
            }
            else if (guess[i] in wordToGuess) {
                result += "+"
            }
            else {
                result += "X"
            }
        }
        return result
    }

    private fun onSubmit(){
        val userInput = editText.text.toString()
        if (userInput.length < WORD_LENGTH){
            Toast.makeText(this, "Must enter 4 letters!", Toast.LENGTH_SHORT).show()
            return
        }
        Log.d("User Input", "Input is $userInput")

        guessCount += 1

        // disable submit button when the user exceed limits
        if (guessCount >= GUESS_LIMIT){
            resetButton.isVisible = true
            submitButton.isEnabled = false
            var colorInt = ContextCompat.getColor(this, R.color.disabled_button_color)
            submitButton.background = ColorDrawable(colorInt)
            answerView.isVisible = true
            answerView.setText("Answer: $wordToGuess")
        }

        val result = checkGuess(userInput)
        hintsView.setText("Hints: $result")
        Log.d("Result", "User got this much right $result")
        Log.d("Guess count", "Number of guesses so far: $guessCount")
    }

    private fun onReset(){
        wordToGuess = FourLetterWordList.getRandomFourLetterWord()
        editText.setText("")
        hintsView.setText("Hints:")
        guessCount = 0

        submitButton.isVisible = true
        submitButton.isEnabled = true
        var submitButtonColor = ColorDrawable(ContextCompat.getColor(this,R.color.submit_button_color))
        submitButton.background = submitButtonColor

        resetButton.isVisible = false

        answerView.isVisible = false

    }
}