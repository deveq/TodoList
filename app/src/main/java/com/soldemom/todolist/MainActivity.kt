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

        val menuItem = menu?.findItem(R.id.menu_search)
        val searchView = menuItem?.actionView  as SearchView

        //검색 기능
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                //검색어 입력버튼을 누른 후 내용이 있다면
                if (query != "" && query != null) {
                    //검색어를 통해 TodoList를 얻어옴.
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

        //검색 닫기를 누른 후 기본 TodoList로 복귀
       searchView.setOnCloseListener {
            setList()
            false
        }
        return super.onCreateOptionsMenu(menu)
    }

    //메뉴 이벤트 처리
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            //등록일 기준 정렬
            R.id.menu_sort_register -> {
                viewModel.isTimeOrder = false

            }
            //날짜 기준 정렬
            R.id.menu_sort_date -> {
                viewModel.isTimeOrder = true
            }
            //완료 일괄 삭제
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
        return false
    }

    // RecyclerView의 item을 눌릴 때 상세페이지로 들어가지게끔 하는 함수. Adapter의 인자로 넣어줌.
    fun goToDetail(todo: Todo, position: Int) {
        val intent = Intent(this, DetailActivity::class.java)
        val bundle = Bundle()
        bundle.putSerializable("todo", todo)
        intent.putExtra("data", bundle)
        intent.putExtra("position", position)
        startActivityForResult(intent, RC_GO_TO_DETAIL)
    }

    // DetailActivity에서 돌아온 이후의 처리
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_GO_TO_DETAIL && resultCode == Activity.RESULT_OK) {

            val bundle = data?.getBundleExtra("data")
            val todo = bundle?.getSerializable("todo") as Todo
            viewModel.update(todo)
            setList()
        }
    }

    // 화면을 다시 돌리기 위해 viewModel 내에 있는 LiveData의 value를 변경시켜줌.
    // value가 변경됨에 따라 observer에 설정된 함수가 실행되고 UI가 변경됨.
    fun setList() {
        todoList.value = viewModel.getList(viewModel.isTimeOrder)
    }

}