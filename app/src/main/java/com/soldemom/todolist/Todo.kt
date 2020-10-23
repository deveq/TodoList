package com.soldemom.todolist

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class Todo(var text: String?, var isDone: Boolean = false) : Serializable {
    var hashTag: String? = null
    var time: Long? = null
    var date: String? = null


}
