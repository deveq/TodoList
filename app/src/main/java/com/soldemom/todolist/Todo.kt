package com.soldemom.todolist

data class Todo(val text: String, var isDone: Boolean = false) {
    var hashTag: String? = null
    var time: Long? = null
}
