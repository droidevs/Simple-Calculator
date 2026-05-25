package io.droidevs.calculatorplus.domain.token

import androidx.annotation.CallSuper
import io.droidevs.calculatorplus.domain.components.Component
import io.droidevs.calculatorplus.domain.components.isE
import io.droidevs.calculatorplus.domain.validation.ValidationArgument
import io.droidevs.calculatorplus.domain.validation.ValidationResult

open class LinkedToken(var component: Component) {

    val length: Int
        get() = component.text.length

    var startIndex: Int = -1

    val endIndex: Int
        get() = startIndex + length - 1

    var next: LinkedToken? = null

    private fun refreshIndexForward() {
        var current: LinkedToken = this
        while (current.next?.isNotEmpty() == true) {
            val n = current.next!!
            n.startIndex = current.endIndex + 1
            current = n
        }
        current.next?.startIndex = current.endIndex + 1
    }

    var prev: LinkedToken? = null
        set(value) {
            field = value
            startIndex = if (value?.isNotEmpty() == true) value.endIndex + 1 else 0
            refreshIndexForward()
        }

    private fun validateIndex(): Boolean {
        val p = prev
        return if (p?.isNotEmpty() != true) startIndex == 0 else startIndex == p.endIndex + 1
    }

    @CallSuper
    final fun validate(): ValidationResult {
        if (!validateIndex()) return ValidationResult.Invalid
        if (!isValid(ValidationArgument.of(this))) return ValidationResult.Invalid
        val n = next
        // BUG FIX #6: Stop walking the chain when we reach an EmptyToken sentinel.
        // Previously, the validate() call would recurse into the trailing EmptyToken added
        // by TokenizerFormatterService, which unconditionally returned Valid — meaning
        // expressions ending with an operator (e.g. "5+") would pass validation.
        return if (n != null && n.isNotEmpty()) n.validate() else ValidationResult.Valid
    }

    open fun isValid(argument: ValidationArgument): Boolean = true
}

fun LinkedToken.isEmpty(): Boolean = this is SpecialToken.EmptyToken
fun LinkedToken.isNotEmpty(): Boolean = !isEmpty()
fun LinkedToken.isOperator(): Boolean = this is OperatorToken
fun LinkedToken.isDecimal(): Boolean = this is SpecialToken.DecimalToken
fun LinkedToken.isDigit(): Boolean = this is DigitToken
fun LinkedToken.isFunction(): Boolean = this is FunctionToken
fun LinkedToken.isParenthesis(): Boolean = this is ParenthesisToken
fun LinkedToken.isOpenParenthesis(): Boolean = this is ParenthesisToken.OpenParenthesisToken
fun LinkedToken.isCloseParenthesis(): Boolean = this is ParenthesisToken.CloseParenthesisToken
fun LinkedToken.isSpecial(): Boolean = this is SpecialToken
fun LinkedToken.isEToken(): Boolean =
    this is ConstantToken && (this.component as io.droidevs.calculatorplus.domain.components.Constant).isE()

fun LinkedToken.headToken(): LinkedToken {
    var h = this
    while (h.prev?.isNotEmpty() == true) h = h.prev!!
    return h
}

fun LinkedToken.refreshIndicesFromThisAsHead() {
    var current: LinkedToken = this
    current.startIndex = 0
    while (current.next?.isNotEmpty() == true) {
        val n = current.next!!
        n.startIndex = current.endIndex + 1
        current = n
    }
    current.next?.startIndex = current.endIndex + 1
}

fun LinkedToken.count(predicate: (LinkedToken) -> Boolean): Int {
    var current: LinkedToken? = this
    var count = 0
    while (current != null && current.isNotEmpty()) {
        if (predicate(current)) count++
        current = current.next
    }
    return count
}

fun LinkedToken.getTokenAt(startIndex: Int): LinkedToken? {
    var current: LinkedToken? = this
    while (current != null && current.isNotEmpty()) {
        if (current.startIndex == startIndex) return current
        current = current.next
    }
    return null
}

// BUG FIX #5: insertAt(0, ...) was creating a disconnected EmptyToken for the new head's
// prev pointer, and the returned token was the old head — callers received the wrong head.
//
// The fix:
//   1. When inserting before the first real token, set newToken.startIndex = 0 explicitly
//      before wiring the prev/next links (the prev setter triggers refreshIndexForward,
//      which would use the old startIndex before our explicit assignment).
//   2. Always return newToken (the new actual head) when inserting at position 0.
//   3. Do NOT assign a fake EmptyToken as prev for the new head — leave prev = null
//      so headToken() traversal terminates correctly.
fun LinkedToken.insertAt(startIndex: Int, newToken: LinkedToken): LinkedToken {
    val head = this.headToken()

    if (startIndex <= head.startIndex) {
        // Insert before the current head
        newToken.startIndex = 0
        newToken.next = head
        // Do NOT set head.prev via the setter yet — the setter calls refreshIndexForward
        // which would use newToken's now-correct startIndex=0 to recalc the chain.
        head.prev = newToken
        // newToken.prev stays null — it IS the head
        return newToken
    }

    var current: LinkedToken? = head
    while (current != null && current.isNotEmpty()) {
        if (current.startIndex >= startIndex) {
            val prevToken = current.prev
            if (prevToken != null) {
                prevToken.next = newToken
            }
            newToken.next = current
            newToken.prev = prevToken  // triggers index refresh forward from newToken
            current.prev = newToken   // triggers index refresh (no-op if already correct)
            return head
        }
        current = current.next
    }

    // Append after the last non-empty token
    var tail: LinkedToken = head
    while (tail.next?.isNotEmpty() == true) {
        tail = tail.next!!
    }
    val end = tail.next
    tail.next = newToken
    newToken.next = end
    newToken.prev = tail   // triggers index refresh
    end?.prev = newToken
    return head
}

fun LinkedToken.replaceAt(pos: Int, newToken: LinkedToken): LinkedToken {
    var current: LinkedToken? = this
    while (current != null && current.isNotEmpty()) {
        if (current.startIndex == pos) {
            if (newToken.isNotEmpty()) {
                current.replaceWith(newToken)
            } else {
                val prevToken = current.prev
                val nextToken = current.next
                if (prevToken == null && nextToken == null) return SpecialToken.EmptyToken()
                if (prevToken != null) prevToken.next = nextToken
                if (nextToken != null) nextToken.prev = prevToken
            }
            return this.headToken()
        }
        current = current.next
    }
    return this
}

fun LinkedToken.replaceWith(newToken: LinkedToken) {
    val prevToken = this.prev
    val nextToken = this.next
    if (newToken.isNotEmpty()) {
        newToken.next = nextToken
        newToken.prev = prevToken
        prevToken?.next = newToken
        nextToken?.prev = newToken
    }
}

inline fun LinkedToken.find(predicate: (LinkedToken) -> Boolean): LinkedToken? {
    var current: LinkedToken? = this
    while (current != null && current.isNotEmpty()) {
        if (predicate(current)) return current
        current = current.next
    }
    return null
}