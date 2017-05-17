package net.danlew.counter

import dagger.Component
import net.danlew.counter.data.DataModule
import net.danlew.counter.ui.CounterViewModel
import javax.inject.Singleton

@Component(
  modules = arrayOf(
      AndroidModule::class,
      DataModule::class
  )
)
@Singleton
interface AppComponent {
  fun inject(into: CounterViewModel)
}