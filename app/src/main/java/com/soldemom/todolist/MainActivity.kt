package com.soldemom.todolist

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val todayList = arrayListOf<Todo>(
            Todo("밥먹기"),
            Todo("밥먹기",true),
            Todo("밥먹기").apply{
                time = System.currentTimeMillis()
            },
            Todo("밥먹기",true).apply{
                time = System.currentTimeMillis()
                hashTag = "밥"
            },
        )
        val tomorrowList = arrayListOf<Todo>(
            Todo("공부"),
            Todo("공부",true),
            Todo("공부").apply{
                time = System.currentTimeMillis()
            },
            Todo("공부").apply {
                hashTag = "빨리 취업하고싶당ㅜㅜ"
            },
            Todo("공부"),
        )

        val todayAdapter = MyAdapter(this,todayList)
        val tomorrowAdapter = MyAdapter(this,tomorrowList)

        today_list.adapter = todayAdapter
        today_list.layoutManager = LinearLayoutManager(this)

        tomorrow_list.adapter = tomorrowAdapter
        tomorrow_list.layoutManager = LinearLayoutManager(this)

        todo_today.setOnClickListener {
            today_list.apply {
                val option = if (visibility == View.VISIBLE) {
                    View.GONE
                } else {
                    View.VISIBLE
                }
                visibility = option
            }
        }

        todo_tomorrow.setOnClickListener {
            tomorrow_list.apply {
                val option = if (visibility == View.VISIBLE) {
                    View.GONE
                } else {
                    View.VISIBLE
                }
                visibility = option
            }
        }

        todo_add.setOnClickListener {
            if (todo_input.text.toString() != "") {
                val todo = Todo(todo_input.text.toString())
                todayList.add(0,todo)
                todayAdapter.notifyDataSetChanged()
                todo_input.setText("")
            }
        }





    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu,menu)



        return super.onCreateOptionsMenu(menu)
    }
}