package net.danlew.counter

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import net.danlew.counter.ui.CounterViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(val application: Application) : ViewModelProvider.Factory {

  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    val appComponent = (application as CounterApplication).appComponent
    if (modelClass == CounterViewModel::class.java) {
      return appComponent.counterViewModel() as T
    }
    return modelClass.newInstance()
  }
}
