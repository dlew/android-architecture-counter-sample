package net.danlew.counter

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AndroidModule(private val context: Context) {

  @Singleton @Provides fun provideContext(): Context = context

}