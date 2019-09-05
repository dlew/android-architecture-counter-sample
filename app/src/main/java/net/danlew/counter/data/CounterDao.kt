package net.danlew.counter.data

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.reactivex.Flowable

// Note: Kotlin renames params, thus the awkward names
@Dao
abstract class CounterDao {

  companion object {
    private const val POSITION_OFFSET = 262144
  }

  @Query("SELECT COUNT(*) FROM counter")
  abstract fun count(): Int

  @Query("SELECT * FROM counter ORDER BY position")
  abstract fun counters(): Flowable<List<Counter>>

  @Query("SELECT * FROM counter WHERE id = :id")
  abstract fun counter(id: Long): Counter?

  @Query("SELECT position FROM counter WHERE id = :id")
  abstract fun position(id: Long): Long

  @Query("SELECT position FROM counter WHERE position < :position ORDER BY position DESC LIMIT 1")
  abstract fun previousPosition(position: Long): Long

  @Query("SELECT position FROM counter WHERE position > :position ORDER BY position ASC LIMIT 1")
  abstract fun nextPosition(position: Long): Long

  @Query("SELECT position FROM counter ORDER BY position DESC LIMIT 1")
  abstract fun lastPosition(): Long

  fun createCounter(name: String) {
    insertOrUpdate(Counter(0, name, lastPosition() + POSITION_OFFSET))
  }

  @Query("UPDATE counter " +
      "SET count = (count + :difference) " +
      "WHERE id = :counterId")
  abstract fun modifyCount(counterId: Long, difference: Long)

  @Query("UPDATE counter " +
      "SET name = :name " +
      "WHERE id = :counterId")
  abstract fun modifyName(counterId: Long, name: String)

  @Query("UPDATE counter " +
      "SET position = :position " +
      "WHERE id = :counterId")
  abstract fun modifyPosition(counterId: Long, position: Long)

  // Positioning is done in such a way that we don't have to rewrite the whole db each time you change anything,
  // just the single item that is being moved.
  fun move(fromCounterId: Long, toCounterId: Long) {
    val fromPosition = position(fromCounterId)
    val toPosition = position(toCounterId)

    val newPosition: Long
    if (fromPosition < toPosition) {
      val nextPosition = nextPosition(toPosition)
      if (nextPosition == 0L) {
        newPosition = toPosition + POSITION_OFFSET
      }
      else {
        newPosition = (nextPosition + toPosition) / 2L
      }
    }
    else {
      val previousPosition = previousPosition(toPosition)
      if (previousPosition == 0L) {
        newPosition = toPosition / 2L
      }
      else {
        newPosition = (previousPosition + toPosition) / 2L
      }
    }

    modifyPosition(fromCounterId, newPosition)
  }

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  abstract fun insertOrUpdate(vararg counters: Counter)

  @Delete
  abstract fun delete(counter: Counter)

}