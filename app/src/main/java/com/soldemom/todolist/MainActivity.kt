package com.soldemom.todolist

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.soldemom.todolist.todos.Todo
import com.soldemom.todolist.todos.TodoViewModel
import com.soldemom.todolist.todos.ViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var todayAdapter : MyAdapter
    lateinit var viewModel : TodoViewModel
    lateinit var todoList: MutableLiveData<MutableList<Todo>>

    companion object {
        const val RC_GO_TO_DETAIL = 1004
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //뷰모델 받아오기
        viewModel = ViewModelProvider(this, ViewModelProviderFactory(this.application))
            .get(TodoViewModel::class.java)

        //recycler view에 보여질 아이템 Room에서 받아오기
        todoList = viewModel.mutableLiveData
        todoList.observe(this, Observer {
            todayAdapter.itemList = it
            todayAdapter.notifyDataSetChanged()
            Toast.makeText(this, "옵저버 내부", Toast.LENGTH_SHORT).show()
        })



        todayAdapter = MyAdapter(this, mutableListOf<Todo>(), viewModel, ::goToDetail, ::setList)

        //recycler view에 adapter와 layout manager 넣기
        today_list.adapter = todayAdapter
        today_list.layoutManager = LinearLayoutManager(this)


        todo_add.setOnClickListener {
            if (todo_input.text.toString() != "") {
                val todo = Todo(todo_input.text.toString())
                viewModel.insert(todo)
                setList()
                todo_input.setText("")
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater = menuInflater

        inflater.inflate(R.menu.main_menu, menu)
        val actionBar = supportActionBar
        actionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }

        val menuItem = menu?.findItem(R.id.menu_search)
        val searchView = menuItem?.actionView  as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != "" && query != null) {
                    todoList.value = viewModel.getTodosByText(query)
                } else {
                    setList()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        searchView.setOnCloseListener {
            setList()
            false
        }

//        searchView.setOnFocusChangeListener { _, hasFocus ->
//            if (hasFocus) {
//                todo_add.visibility = View.INVISIBLE
//            } else {
//                todo_add.visibility = View.VISIBLE
//            }
//        }


        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_sort_register -> {
                viewModel.isTimeOrder = false

            }
            R.id.menu_sort_date -> {
                viewModel.isTimeOrder = true
            }
            R.id.menu_delete_done -> {
                val alertDialog = AlertDialog.Builder(this)
                alertDialog.setMessage("완료된 할 일 목록을 전체 지우시겠습니까?")
                    .setNegativeButton("취소", null)
                    .setPositiveButton("확인") { _, _ ->
                        for (todo in todayAdapter.itemList) {
                            if (todo.isDone) {
                                viewModel.delete(todo)
                            }
                        }
                        setList()
                    }
                    .show()
            }
        }
        setList()
        // 등록일 순인지, 날짜순인지 나타내는 isTimeOrder를 설정한 후 화면에 다시 출력해줄 수 있또록
        // MutableLiveData인 todoList의 value에 다시 getList의 반환값인 MutableList<Todo>를 넣어줌
        return false
    }

    fun goToDetail(todo: Todo, position: Int) {
        val intent = Intent(this, DetailActivity::class.java)
        val bundle = Bundle()
        bundle.putSerializable("todo", todo)
        intent.putExtra("data", bundle)
        intent.putExtra("position", position)
        startActivityForResult(intent, RC_GO_TO_DETAIL)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_GO_TO_DETAIL && resultCode == Activity.RESULT_OK) {

            val bundle = data?.getBundleExtra("data")
            val todo = bundle?.getSerializable("todo") as Todo
            viewModel.update(todo)
            setList()
        }
    }

    fun setList() {
        todoList.value = viewModel.getList(viewModel.isTimeOrder)
    }

}