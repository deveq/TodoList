package com.soldemom.todolist.todos

import androidx.room.Database
import androidx.room.RoomDatabase
import com.soldemom.todolist.todos.Todo
import com.soldemom.todolist.todos.TodoDao

@Database(entities = [Todo::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}