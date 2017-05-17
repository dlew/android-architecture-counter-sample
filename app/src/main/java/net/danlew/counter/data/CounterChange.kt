package net.danlew.counter.data

/**
 * Represents requests to change a counter's properties.
 */
sealed class CounterChange {
  data class Create(val name: String = "") : CounterChange()
  data class Name(val counterId: Long, val name: String) : CounterChange()
  data class Count(val counterId: Long, val difference: Long) : CounterChange()
  data class Move(val fromCounterId: Long, val toCounterId: Long) : CounterChange()
  data class Delete(val counterId: Long) : CounterChange()
  data class UndoDelete(val counter: Counter) : CounterChange()
}
