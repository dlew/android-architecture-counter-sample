package net.danlew.counter.ui

import android.arch.lifecycle.ViewModel
import net.danlew.counter.data.AppDatabase
import net.danlew.counter.data.Counter
import javax.inject.Inject

class CounterViewModel @Inject constructor(val db: AppDatabase) : ViewModel() {

  fun hasCounters() = db.counterModel().count() != 0

  fun counters() = db.counterModel().counters()

  fun createCounter(name: String = "") = db.counterModel().createCounter(name)

  fun undoDelete(counter: Counter) = db.counterModel().insertOrUpdate(counter)

  fun modifyCount(counterId: Long, difference: Long) = db.counterModel().modifyCount(counterId, difference)

  fun modifyName(counterId: Long, name: String) = db.counterModel().modifyName(counterId, name)

  fun move(fromCounterId: Long, toCounterId: Long) = db.counterModel().move(fromCounterId, toCounterId)

  fun delete(counterId: Long): Counter? {
    db.counterModel().counter(counterId)?.let {
      db.counterModel().delete(it)
      return it
    }

    return null
  }
}