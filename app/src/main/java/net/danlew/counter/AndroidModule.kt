package net.danlew.counter

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AndroidModule(private val context: Application) {

  @Singleton @Provides fun provideContext(): Context = context
  @Singleton @Provides fun provideViewModelFactory(): ViewModelFactory = ViewModelFactory(context)
}