package net.danlew.counter.data

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {

  @Singleton @Provides fun provideAppDatabase(context: Context) = AppDatabase.createPersistentDatabase(context)

}