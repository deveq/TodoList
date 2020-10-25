package com.soldemom.todolist

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.soldemom.todolist.todos.AppDatabase
import com.soldemom.todolist.todos.Todo
import com.soldemom.todolist.todos.TodoViewModel
import com.soldemom.todolist.todos.ViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var todayAdapter : MyAdapter
    lateinit var viewModel : TodoViewModel
    lateinit var todoList: LiveData<MutableList<Todo>>

    companion object {
        const val RC_GO_TO_DETAIL = 1004
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        viewModel = ViewModelProvider(this,ViewModelProviderFactory(this.application))
            .get(TodoViewModel::class.java)

        todoList = viewModel.getAll()

        todoList.observe(this, Observer {
            //추가해야함
            todayAdapter.notifyDataSetChanged()

        })


        todayAdapter = MyAdapter(this,todoList.value?:mutableListOf<Todo>(),viewModel,::goToDetail)

        today_list.adapter = todayAdapter
        today_list.layoutManager = LinearLayoutManager(this)


        todo_add.setOnClickListener {
            if (todo_input.text.toString() != "") {
                val todo = Todo(todo_input.text.toString())
                viewModel.insert(todo)
                todayAdapter.itemList.add(todo)

                todo_input.setText("")
            }
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater = menuInflater

        inflater.inflate(R.menu.main_menu,menu)
        val actionBar = supportActionBar
        actionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }

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

//            todayList[position!!] = todo
            viewModel.update(todo)
            todayAdapter.itemList[position!!] = todo
        }
    }
}