package com.soldemom.todolist.todos

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TodoDao {
    //전체 얻기. 기본값. 등록순서로 보이기
    @Query("select * from Todo order by registerTime desc")
    fun getAll() : LiveData<MutableList<Todo>>

    //전체 얻기. 날짜순으로 얻기.
    @Query("select * from todo order by date, time")
    fun getAllTimeOrder() : LiveData<MutableList<Todo>>

    //할일 키워드로 얻기
    @Query("select * from todo where text = (:text)")
    fun getTodosByText(text: String) : LiveData<MutableList<Todo>>

    //해시태그로 얻기
    @Query("select * from todo where hashTag = (:hashTag)")
    fun getTodosByHashTag(hashTag: String) : LiveData<MutableList<Todo>>

    @Insert
    fun insert(todo: Todo)

    @Update
    fun update(todo: Todo)

    @Delete
    fun delete(todo: Todo)

    @Delete
    fun deleteAll(vararg todo: Todo)



}