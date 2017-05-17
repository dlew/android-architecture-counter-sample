package net.danlew.counter.data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity(
    tableName = "counter"
)
data class Counter (
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String = "",
    val position: Long = 0,
    val count: Long = 0
)
