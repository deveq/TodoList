package com.soldemom.todolist

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {
    lateinit var todo: Todo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        detail_todo_input //할일
        detail_todo_time //시간 입력
        detail_todo_tag //해시태그
        detail_cancel_btn //취소
        detail_confirm_btn //확인

        val bundle = intent.getBundleExtra("data")

        val position = intent.getIntExtra("position",0)

        bundle?.apply {
            todo = getSerializable("todo") as Todo
            detail_todo_input.setText(todo.text)
            detail_todo_tag.setText(todo.hashTag)
        }



        val listener = View.OnClickListener { v ->
            if (v == detail_cancel_btn) {
                setResult(Activity.RESULT_CANCELED)
                finish()
            } else if (v == detail_confirm_btn) {

                val todoInput = detail_todo_input.text.toString()
                val todoTag = detail_todo_tag.text.toString()

                todo.text = todoInput
                todo.hashTag = todoTag
                bundle.putSerializable("todo",todo)
                intent.putExtra("data", bundle)

                setResult(Activity.RESULT_OK,intent)
                finish()
            }
        }

        detail_cancel_btn.setOnClickListener(listener)
        detail_confirm_btn.setOnClickListener(listener)




    }
}