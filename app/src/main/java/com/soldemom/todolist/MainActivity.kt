package com.soldemom.todolist

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var todayList : ArrayList<Todo>
    lateinit var tomorrowList : ArrayList<Todo>
    lateinit var todayAdapter : MyAdapter
    lateinit var tomorrowAdapter: MyAdapter

    companion object {
        const val RC_GO_TO_DETAIL = 1004
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        todayList = arrayListOf<Todo>(
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
        tomorrowList = arrayListOf<Todo>(
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

        todayAdapter = MyAdapter(this,todayList,::goToDetail)
        tomorrowAdapter = MyAdapter(this,tomorrowList,::goToDetail)

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

//          기본 메뉴
        val inflater = menuInflater

        inflater.inflate(R.menu.main_menu,menu)
        val actionBar = supportActionBar
        actionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }


        //커스텀 메뉴
//        val actionBar = supportActionBar
////        val inflater = menuInflater
//        val inflater = LayoutInflater.from(this)
//        val actionBarView = inflater.inflate(R.layout.custom_action_bar,null)
//
//        actionBar?.apply {
//            setDisplayShowCustomEnabled(true)
//            setDisplayHomeAsUpEnabled(true)
//            setDisplayShowTitleEnabled(false)
//            setDisplayShowHomeEnabled(false)
//            customView = actionBarView
//        }
//
//        val parent = actionBarView.parent as Toolbar
//        parent.setContentInsetsAbsolute(0,0)


        return super.onCreateOptionsMenu(menu)
    }

    fun goToDetail(todo: Todo, position: Int) {
        val intent = Intent(this, DetailActivity::class.java)
        val bundle = Bundle()
        bundle.putSerializable("todo",todo)
        intent.putExtra("data",bundle)
        intent.putExtra("position",position)
        startActivityForResult(intent, RC_GO_TO_DETAIL)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_GO_TO_DETAIL && resultCode == Activity.RESULT_OK) {



            val bundle = data?.getBundleExtra("data")
            

            val position = data?.getIntExtra("position",0)
            Log.d("bundle","$position 값이나왓어용 0이 아니길!")

            val todo = bundle?.getSerializable("todo") as Todo

            todayList[position!!] = todo
            todayAdapter.notifyDataSetChanged()



        }




    }
}