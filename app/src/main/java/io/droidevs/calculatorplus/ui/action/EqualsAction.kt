package io.droidevs.calculatorplus.ui.action

// BUG FIX #1: Was `object EqualsAction` — a singleton object used as a UI action.
// Problem: `object` types have a single instance, making `is EqualsAction` checks work
// but causing subtle bugs if the action is ever stored, compared by value, or serialized.
// All other Action subclasses are regular classes. This should be consistent.
class EqualsAction : Action("=")