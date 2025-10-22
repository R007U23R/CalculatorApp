package com.example.calculatorapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    // Meaningful variable names (Requirement #1)
    private lateinit var txtDisplay: TextView
    private lateinit var txtOperation: TextView
    private lateinit var txtMemoryIndicator: TextView

    // Number buttons
    private lateinit var btn0: Button
    private lateinit var btn1: Button
    private lateinit var btn2: Button
    private lateinit var btn3: Button
    private lateinit var btn4: Button
    private lateinit var btn5: Button
    private lateinit var btn6: Button
    private lateinit var btn7: Button
    private lateinit var btn8: Button
    private lateinit var btn9: Button

    // Operation buttons
    private lateinit var btnAdd: Button
    private lateinit var btnSubtract: Button
    private lateinit var btnMultiply: Button
    private lateinit var btnDivide: Button
    private lateinit var btnSqrt: Button
    private lateinit var btnEquals: Button

    // Special buttons
    private lateinit var btnClear: Button
    private lateinit var btnBack: Button
    private lateinit var btnSign: Button
    private lateinit var btnDecimal: Button

    // Memory buttons
    private lateinit var btnMC: Button
    private lateinit var btnMR: Button
    private lateinit var btnMS: Button
    private lateinit var btnMPlus: Button
    private lateinit var btnMMinus: Button

    // Calculator logic instance
    private val calculator = Calculator()

    // Current display value
    private var displayValue = "0"
    private var waitingForOperand = false
    private var decimalAdded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeViews()
        setupNumberButtons()
        setupOperationButtons()
        setupSpecialButtons()
        setupMemoryButtons()
    }

    /**
     * Initialize all view references with meaningful names
     */
    private fun initializeViews() {
        // Display
        txtDisplay = findViewById(R.id.txtDisplay)
        txtOperation = findViewById(R.id.txtOperation)
        txtMemoryIndicator = findViewById(R.id.txtMemoryIndicator)

        // Number buttons
        btn0 = findViewById(R.id.btn0)
        btn1 = findViewById(R.id.btn1)
        btn2 = findViewById(R.id.btn2)
        btn3 = findViewById(R.id.btn3)
        btn4 = findViewById(R.id.btn4)
        btn5 = findViewById(R.id.btn5)
        btn6 = findViewById(R.id.btn6)
        btn7 = findViewById(R.id.btn7)
        btn8 = findViewById(R.id.btn8)
        btn9 = findViewById(R.id.btn9)

        // Operation buttons
        btnAdd = findViewById(R.id.btnAdd)
        btnSubtract = findViewById(R.id.btnSubtract)
        btnMultiply = findViewById(R.id.btnMultiply)
        btnDivide = findViewById(R.id.btnDivide)
        btnSqrt = findViewById(R.id.btnSqrt)
        btnEquals = findViewById(R.id.btnEquals)

        // Special buttons
        btnClear = findViewById(R.id.btnClear)
        btnBack = findViewById(R.id.btnBack)
        btnSign = findViewById(R.id.btnSign)
        btnDecimal = findViewById(R.id.btnDecimal)

        // Memory buttons
        btnMC = findViewById(R.id.btnMC)
        btnMR = findViewById(R.id.btnMR)
        btnMS = findViewById(R.id.btnMS)
        btnMPlus = findViewById(R.id.btnMPlus)
        btnMMinus = findViewById(R.id.btnMMinus)
    }

    /**
     * Set up click listeners for number buttons
     */
    private fun setupNumberButtons() {
        val numberButtons = listOf(
            btn0 to "0", btn1 to "1", btn2 to "2", btn3 to "3", btn4 to "4",
            btn5 to "5", btn6 to "6", btn7 to "7", btn8 to "8", btn9 to "9"
        )

        numberButtons.forEach { (button, digit) ->
            button.setOnClickListener {
                inputDigit(digit)
            }
        }
    }

    /**
     * Set up click listeners for operation buttons
     */
    private fun setupOperationButtons() {
        btnAdd.setOnClickListener { performOperation("+") }
        btnSubtract.setOnClickListener { performOperation("-") }
        btnMultiply.setOnClickListener { performOperation("×") }
        btnDivide.setOnClickListener { performOperation("÷") }
        btnEquals.setOnClickListener { performOperation("=") }

        btnSqrt.setOnClickListener { performSquareRoot() }
    }

    /**
     * Set up click listeners for special buttons
     */
    private fun setupSpecialButtons() {
        // Clear button - resets calculator
        btnClear.setOnClickListener {
            clear()
        }

        // Back button - removes last digit
        btnBack.setOnClickListener {
            backspace()
        }

        // Sign change button - toggles positive/negative
        btnSign.setOnClickListener {
            toggleSign()
        }

        // Decimal button - adds decimal point
        btnDecimal.setOnClickListener {
            inputDecimal()
        }
    }

    /**
     * Set up click listeners for memory buttons
     */
    private fun setupMemoryButtons() {
        // Memory Clear
        btnMC.setOnClickListener {
            calculator.memoryClear()
            updateMemoryIndicator()
        }

        // Memory Recall
        btnMR.setOnClickListener {
            displayValue = formatNumber(calculator.memoryRecall())
            waitingForOperand = true
            updateDisplay()
        }

        // Memory Store
        btnMS.setOnClickListener {
            val value = displayValue.toDoubleOrNull() ?: 0.0
            calculator.memoryStore(value)
            updateMemoryIndicator()
        }

        // Memory Add
        btnMPlus.setOnClickListener {
            val value = displayValue.toDoubleOrNull() ?: 0.0
            calculator.memoryAdd(value)
            updateMemoryIndicator()
        }

        // Memory Subtract
        btnMMinus.setOnClickListener {
            val value = displayValue.toDoubleOrNull() ?: 0.0
            calculator.memorySubtract(value)
            updateMemoryIndicator()
        }
    }

    /**
     * Handles digit input
     */
    private fun inputDigit(digit: String) {
        if (waitingForOperand) {
            displayValue = digit
            waitingForOperand = false
            decimalAdded = false
        } else {
            displayValue = if (displayValue == "0") digit else displayValue + digit
        }

        // Update operation display to show current number being entered
        calculator.updateOperationDisplay(displayValue)
        updateDisplay()
    }

    /**
     * Handles decimal point input
     */
    private fun inputDecimal() {
        if (waitingForOperand) {
            displayValue = "0."
            waitingForOperand = false
            decimalAdded = true
        } else if (!decimalAdded) {
            displayValue += "."
            decimalAdded = true
        }

        calculator.updateOperationDisplay(displayValue)
        updateDisplay()
    }

    /**
     * Performs arithmetic operation
     */
    private fun performOperation(operation: String) {
        try {
            val inputValue = displayValue.toDouble()

            if (calculator.isNewOp() && operation != "=") {
                calculator.calculate(inputValue, operation)
            } else {
                val result = calculator.calculate(inputValue, operation)
                displayValue = formatNumber(result)
            }

            waitingForOperand = true
            decimalAdded = false
            updateDisplay()

        } catch (e: ArithmeticException) {
            displayValue = getString(R.string.error_divide_zero)
            txtOperation.text = ""
            updateDisplay()
            waitingForOperand = true
        } catch (e: Exception) {
            displayValue = getString(R.string.error_invalid)
            txtOperation.text = ""
            updateDisplay()
            waitingForOperand = true
        }
    }

    /**
     * Performs square root operation
     */
    private fun performSquareRoot() {
        try {
            val inputValue = displayValue.toDouble()
            val result = calculator.squareRoot(inputValue)
            displayValue = formatNumber(result)
            waitingForOperand = true
            decimalAdded = false
            updateDisplay()
        } catch (e: ArithmeticException) {
            displayValue = getString(R.string.error_invalid)
            txtOperation.text = ""
            updateDisplay()
            waitingForOperand = true
        }
    }

    /**
     * Clears the calculator
     */
    private fun clear() {
        displayValue = "0"
        calculator.clear()
        waitingForOperand = false
        decimalAdded = false
        updateDisplay()
    }

    /**
     * Removes last entered digit
     */
    private fun backspace() {
        if (!waitingForOperand && displayValue.length > 1) {
            // Check if we're removing the decimal point
            if (displayValue.last() == '.') {
                decimalAdded = false
            }
            displayValue = displayValue.dropLast(1)
        } else {
            displayValue = "0"
            decimalAdded = false
        }

        calculator.updateOperationDisplay(displayValue)
        updateDisplay()
    }

    /**
     * Toggles sign of the current number
     */
    private fun toggleSign() {
        val value = displayValue.toDoubleOrNull() ?: return
        displayValue = formatNumber(-value)
        calculator.updateOperationDisplay(displayValue)
        updateDisplay()
    }

    /**
     * Updates the display with current value and operation
     */
    private fun updateDisplay() {
        txtDisplay.text = displayValue
        txtOperation.text = calculator.operationDisplay
    }

    /**
     * Updates the memory indicator visibility
     */
    private fun updateMemoryIndicator() {
        txtMemoryIndicator.visibility = if (calculator.hasMemory()) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }
    }

    /**
     * Formats number for display (removes unnecessary decimals)
     */
    private fun formatNumber(value: Double): String {
        return if (value == value.toLong().toDouble()) {
            value.toLong().toString()
        } else {
            val df = DecimalFormat("#.##########")
            df.format(value)
        }
    }
}