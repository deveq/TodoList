package com.soldemom.todolist.todos

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.room.*

class TodoViewModel(context: Context) : ViewModel() {

    private val todoDatabase = Room.databaseBuilder(context, AppDatabase::class.java, "todo").allowMainThreadQueries().build()

    private val todoDao = todoDatabase.todoDao()
    private val todos = todoDao.getAll()

    fun getAll() : LiveData<MutableList<Todo>> {
        return todos
    }

    //전체 얻기. 날짜순으로 얻기.
    fun getAllTimeOrder() : LiveData<MutableList<Todo>> {
        return todoDao.getAllTimeOrder()
    }

    //할일 키워드로 얻기
    fun getTodosByText(text: String) : LiveData<MutableList<Todo>> {
        return todoDao.getTodosByText(text)
    }

    //해시태그로 얻기
    fun getTodosByHashTag(hashTag: String) : LiveData<MutableList<Todo>> {
        return todoDao.getTodosByHashTag(hashTag)
    }

    fun insert(todo: Todo) {
        todoDao.insert(todo)
    }

    fun update(todo: Todo) {
        todoDao.update(todo)
    }

    fun delete(todo: Todo) {
        todoDao.delete(todo)
    }

    fun deleteAll(todo: Array<Todo>){
        todoDao.deleteAll(*todo)
    }



}