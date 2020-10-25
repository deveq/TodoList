package com.soldemom.todolist.todos

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Todo(var text: String?, var isDone: Boolean = false) : Serializable {
    @PrimaryKey
    var registerTime : Long = System.currentTimeMillis()
    @ColumnInfo
    var hashTag: String? = null
    @ColumnInfo
    var time: String? = null
    @ColumnInfo
    var date: String? = null
}
