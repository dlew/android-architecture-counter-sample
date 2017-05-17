package net.danlew.counter.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

@Database(
    entities = arrayOf(Counter::class),
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

  abstract fun counterModel(): CounterDao

  companion object {
    private const val DB_NAME = "counter.db"

    fun createInMemoryDatabase(context: Context): AppDatabase
        = Room.inMemoryDatabaseBuilder(context.applicationContext, AppDatabase::class.java).build()

    fun createPersistentDatabase(context: Context): AppDatabase
        = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, DB_NAME).build()
  }

}