package net.danlew.counter.ui

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import net.danlew.counter.CounterApplication
import net.danlew.counter.data.AppDatabase
import net.danlew.counter.data.Counter
import javax.inject.Inject

class CounterViewModel constructor(application: Application) : AndroidViewModel(application) {

  @Inject lateinit var db: AppDatabase

  init {
    (application as CounterApplication).appComponent.inject(this)
  }

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