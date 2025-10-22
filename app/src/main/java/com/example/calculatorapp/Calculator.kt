package com.example.calculatorapp

import kotlin.math.sqrt
import kotlin.math.sin
import kotlin.math.cos
import kotlin.math.tan
import kotlin.math.ln
import kotlin.math.log10
import kotlin.math.pow

/**
 * Calculator class handles all mathematical operations
 * Includes memory functions and scientific operations
 */
class Calculator {

    private var currentValue: Double = 0.0
    private var pendingValue: Double = 0.0
    private var pendingOperation: String = ""
    private var isNewOperation: Boolean = true

    // Memory storage
    private var memoryValue: Double = 0.0

    // Operation history for display
    var operationDisplay: String = ""
        private set

    /**
     * Performs the calculation based on the pending operation
     */
    fun calculate(value: Double, operation: String): Double {
        // Update operation display
        if (operation != "=" && operation.isNotEmpty()) {
            operationDisplay = "$value $operation"
        }

        if (isNewOperation && operation != "=") {
            pendingValue = value
            pendingOperation = operation
            isNewOperation = false
            return value
        }

        when (pendingOperation) {
            "+" -> currentValue = pendingValue + value
            "-" -> currentValue = pendingValue - value
            "×" -> currentValue = pendingValue * value
            "÷" -> {
                if (value != 0.0) {
                    currentValue = pendingValue / value
                } else {
                    throw ArithmeticException("Division by zero")
                }
            }
            else -> currentValue = value
        }

        if (operation == "=") {
            // Clear operation display when equals is pressed
            operationDisplay = "$pendingValue $pendingOperation $value ="
            isNewOperation = true
            pendingOperation = ""
        } else {
            pendingValue = currentValue
            pendingOperation = operation
            operationDisplay = "$currentValue $operation"
        }

        return currentValue
    }

    /**
     * Calculates square root
     */
    fun squareRoot(value: Double): Double {
        if (value < 0) {
            throw ArithmeticException("Cannot calculate square root of negative number")
        }
        currentValue = sqrt(value)
        operationDisplay = "√$value ="
        isNewOperation = true
        return currentValue
    }

    /**
     * Memory Clear - Clears memory
     */
    fun memoryClear() {
        memoryValue = 0.0
    }

    /**
     * Memory Recall - Returns stored memory value
     */
    fun memoryRecall(): Double {
        return memoryValue
    }

    /**
     * Memory Store - Stores current value in memory
     */
    fun memoryStore(value: Double) {
        memoryValue = value
    }

    /**
     * Memory Add - Adds value to memory
     */
    fun memoryAdd(value: Double) {
        memoryValue += value
    }

    /**
     * Memory Subtract - Subtracts value from memory
     */
    fun memorySubtract(value: Double) {
        memoryValue -= value
    }

    /**
     * Checks if memory has a value
     */
    fun hasMemory(): Boolean {
        return memoryValue != 0.0
    }

    /**
     * Scientific operations
     */
    fun sin(value: Double): Double {
        currentValue = sin(Math.toRadians(value))
        operationDisplay = "sin($value) ="
        isNewOperation = true
        return currentValue
    }

    fun cos(value: Double): Double {
        currentValue = cos(Math.toRadians(value))
        operationDisplay = "cos($value) ="
        isNewOperation = true
        return currentValue
    }

    fun tan(value: Double): Double {
        currentValue = tan(Math.toRadians(value))
        operationDisplay = "tan($value) ="
        isNewOperation = true
        return currentValue
    }

    fun log(value: Double): Double {
        if (value <= 0) {
            throw ArithmeticException("Logarithm undefined for non-positive values")
        }
        currentValue = log10(value)
        operationDisplay = "log($value) ="
        isNewOperation = true
        return currentValue
    }

    fun ln(value: Double): Double {
        if (value <= 0) {
            throw ArithmeticException("Natural log undefined for non-positive values")
        }
        currentValue = ln(value)
        operationDisplay = "ln($value) ="
        isNewOperation = true
        return currentValue
    }

    fun power(base: Double, exponent: Double): Double {
        currentValue = base.pow(exponent)
        operationDisplay = "$base ^ $exponent ="
        isNewOperation = true
        return currentValue
    }

    /**
     * Resets calculator to initial state
     */
    fun clear() {
        currentValue = 0.0
        pendingValue = 0.0
        pendingOperation = ""
        isNewOperation = true
        operationDisplay = ""
    }

    /**
     * Updates operation display when entering new numbers
     */
    fun updateOperationDisplay(value: String) {
        if (pendingOperation.isNotEmpty() && !isNewOperation) {
            operationDisplay = "$pendingValue $pendingOperation $value"
        } else {
            operationDisplay = value
        }
    }

    /**
     * Check if starting new operation
     */
    fun isNewOp(): Boolean = isNewOperation

    /**
     * Set new operation flag
     */
    fun setNewOp(value: Boolean) {
        isNewOperation = value
    }
}