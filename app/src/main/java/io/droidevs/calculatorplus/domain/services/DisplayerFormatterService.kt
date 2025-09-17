package io.droidevs.calculatorplus.domain.services

import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import io.droidevs.calculatorplus.domain.components.Special
import io.droidevs.calculatorplus.domain.token.LinkedToken


class ExpressionDisplayFormatter {

    fun format(token: LinkedToken): CharSequence {
        var expression = ""
        var current : LinkedToken? = token
        while(current != null){
            expression+= current.component.text
            current = current.next
        }
        val formated = formatNumbers(expression)
        return highlightSpecialSymbols(formated)
    }

    fun highlightSpecialSymbols(text: CharSequence) : CharSequence {
        val spannableString = SpannableString(text)

        for (i in text.indices) {
            val c = text[i]
            val colorSpan = ForegroundColorSpan(Color.GRAY)
            if (c in listOf('+', '-', '×', '÷', '^')) {
                spannableString.setSpan(colorSpan, i, i + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
            }
        }

        return spannableString
    }

    private fun formatNumbers(text: String): String {
        val decimalSeparatorSymbol = Special.Decimal.text
        val groupingSeparatorSymbol = ","
        val textNoSeparator = text//removeSeparators(text)
        val numbersList = extractNumbers(textNoSeparator, decimalSeparatorSymbol)
        val numbersWithSeparators = addSeparators(numbersList, decimalSeparatorSymbol, groupingSeparatorSymbol)
        var textWithSeparators = textNoSeparator
        numbersList.forEachIndexed { index, number ->
            textWithSeparators = textWithSeparators.replaceFirst(number, numbersWithSeparators[index])
        }
        return textWithSeparators
    }


    private fun extractNumbers(text: String, decimalSeparatorSymbol : String): List<String> {
        val numberRegex = "(\\d+\\$decimalSeparatorSymbol\\d+)|(\\d+\\$decimalSeparatorSymbol)|(\\$decimalSeparatorSymbol\\d+)|(\\$decimalSeparatorSymbol)|(\\d+)".toRegex()

        val results = numberRegex.findAll(text)
        return results.map { it.value }.toList()
    }

    private fun addSeparators(numbersList: List<String>, decimalSeparatorSymbol: String, groupingSeparatorSymbol: String): List<String> {
        return numbersList.map {
            if (it.contains(decimalSeparatorSymbol)) {
                if (it.first() == decimalSeparatorSymbol[0]) {
                    //this means the floating point number doesn't have integers
                    it
                } else {
                    val integersPart = it.substring(0, it.indexOf(decimalSeparatorSymbol))
                    val fractions = it.substring(it.indexOf(decimalSeparatorSymbol) + 1)
                    formatIntegers(integersPart, groupingSeparatorSymbol) + decimalSeparatorSymbol + fractions
                }
            } else {
                formatIntegers(it, groupingSeparatorSymbol)
            }
        }
    }

    private fun formatIntegers(integers: String, groupingSeparatorSymbol: String): String {
        // sample input  : 00110
        return integers.reversed()                  // reversed      : 01100
            .chunked(3)                        // chunked       : [011, 00]
            .joinToString(groupingSeparatorSymbol)  // joinedToString: 011,00
            .reversed()                             // reversed      : 00,110
    }

}