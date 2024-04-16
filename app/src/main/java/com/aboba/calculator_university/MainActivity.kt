package com.aboba.calculator_university

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import net.objecthunter.exp4j.ExpressionBuilder;


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        setOnClickListeners()

    }

    private fun setOnClickListeners() {
        val buttons = listOf(
            zero, one, two, three, four, five, six, seven, eight, nine,
            dot, plus, minus, multiply, divide
        )

        buttons.forEach { button ->
            button.setOnClickListener {
                handleButtonClick(button.text.toString())
            }
        }

        backspace.setOnClickListener {
            handleBackspace()
        }

        cls.setOnClickListener {
            handleClear()
        }

        equals.setOnClickListener {
            calculate()
        }

        percent.setOnClickListener {
            handleButtonClick("%")
        }

        // Позволяет нажимать Enter для получения результата
        txt_question.setOnEditorActionListener { _, _, _ ->
            calculate()
            true
        }
    }



    private fun handleButtonClick(value: String) {
        if (value == "%") {
            val inputExpression = txt_question.text.toString().trim()

            if (inputExpression.isNotEmpty()) {
                try {
                    // Преобразование выражения с процентом в результат
                    val expression = ExpressionBuilder(inputExpression).build()
                    val result = expression.evaluate() * 0.01

                    txt_question.text = result.toString()
                } catch (e: Exception) {
                    // Обработайте ошибку, если выражение не может быть вычислено
                    txt_question.text = "Error"
                    Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        } else {
            // Обычная обработка ввода для других кнопок
            txt_question.append(value)
        }
    }


    private fun handleBackspace() {
        val currentText = txt_question.text.toString()
        if (currentText.isNotEmpty()) {
            txt_question.text = currentText.substring(0, currentText.length - 1)
        }
    }

    private fun handleClear() {
        txt_question.text = ""
        txt_answer.text = ""
    }

    private fun calculate() {
        val inputExpression = txt_question.text.toString().trim()

        if (inputExpression.isEmpty()) {
            txt_answer.text = ""
            Toast.makeText(this@MainActivity, "Invalid Input", Toast.LENGTH_LONG).show()
            return
        }

        try {
            val input = ExpressionBuilder(inputExpression).build()
            val output = input.evaluate()
            val longOutput = output.toLong()

            if (output == longOutput.toDouble()) {
                txt_answer.text = longOutput.toString()
            } else {
                txt_answer.text = output.toString()
            }

        } catch (e: ArithmeticException) {
            txt_answer.text = "Error"
            Toast.makeText(this@MainActivity, "Arithmetic Error", Toast.LENGTH_LONG).show()
        } catch (e: IllegalArgumentException) {
            txt_answer.text = "Error"
            Toast.makeText(this@MainActivity, "Invalid Input", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            txt_answer.text = "Error"
            Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

}