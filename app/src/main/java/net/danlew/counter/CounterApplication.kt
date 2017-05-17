package net.danlew.counter

import android.app.Application
import timber.log.Timber

class CounterApplication : Application() {

  val appComponent: AppComponent = DaggerAppComponent.builder()
      .androidModule(AndroidModule(this))
      .build()

  override fun onCreate() {
    super.onCreate()

    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
    }
  }
}