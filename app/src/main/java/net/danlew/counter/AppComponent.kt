package net.danlew.counter

import dagger.Component
import net.danlew.counter.data.DataModule
import net.danlew.counter.ui.CounterViewModel
import net.danlew.counter.ui.MainActivity
import javax.inject.Singleton

@Component(
  modules = arrayOf(
      AndroidModule::class,
      DataModule::class
  )
)
@Singleton
interface AppComponent {
  fun counterViewModel(): CounterViewModel
  fun inject(mainActivity: MainActivity)
}