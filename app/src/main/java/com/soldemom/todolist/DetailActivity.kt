package com.soldemom.todolist

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.soldemom.todolist.todos.Todo
import kotlinx.android.synthetic.main.activity_detail.*
import java.util.*

class DetailActivity : AppCompatActivity() {
    lateinit var todo: Todo

    lateinit var time: String
    lateinit var date: String

    lateinit var bundle : Bundle

    var timeFlag = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        detail_todo_input //할일
        detail_todo_tag //해시태그
        detail_cancel_btn //취소
        detail_confirm_btn //확인

        bundle = intent.getBundleExtra("data")

        val position = intent.getIntExtra("position",0)

        bundle?.apply {
            todo = getSerializable("todo") as Todo
            detail_todo_input.setText(todo.text)
            detail_todo_tag.setText(todo.hashTag)
        }

        // 오늘 연,월,일을 get
        val calendar = Calendar.getInstance()
        val year = todo?.year ?: calendar.get(Calendar.YEAR)
        val month = todo?.month ?: calendar.get(Calendar.MONTH)
        val day = todo?.day ?: calendar.get(Calendar.DAY_OF_MONTH)
        val hour = todo?.hour ?: 9
        val minute = todo?.minute ?: 0
        date =  "${year}.${month+1}.$day"
        time = String.format("%02d:%02d",hour,minute)

        detail_todo_date.text = date
        detail_todo_time.text = time


        //날짜 지정
        val datePickerListener =
            DatePickerDialog.OnDateSetListener { view, _year, _month, dayOfMonth ->
                date =  "${_year}.${_month+1}.$dayOfMonth"
                detail_todo_date.text = date
                timeFlag = true
                todo.year = _year
                todo.month = _month
                todo.day = dayOfMonth
                todo.date = date
            }

        val datePickerDialog = DatePickerDialog(this, datePickerListener,year,month,day)
        val datePicker = datePickerDialog.datePicker
        datePicker.minDate = System.currentTimeMillis()

        detail_todo_date.setOnClickListener {
            datePickerDialog.show()
        }

        //시간 지정
        val timePickerListener =
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                val strHour = String.format("%02d",hourOfDay)
                val strMin = String.format("%02d",minute)

                time = "$strHour:$strMin"
                detail_todo_time.text = time
                timeFlag = true
                todo.hour = hourOfDay
                todo.minute = minute
                todo.time = time
            }


        val timePickerDialog = TimePickerDialog(this,android.R.style.Theme_Holo_Light_Dialog, timePickerListener, 9,0,true)

        detail_todo_time.setOnClickListener {
            timePickerDialog.show()
        }


        // 확인버튼을 눌렀을때
        detail_confirm_btn.setOnClickListener(detailBtnListener)
        // 취소 버튼 눌렀을 때
        detail_cancel_btn.setOnClickListener(detailBtnListener)




        // HashTag를 수정한 후 focus를 잃을 때
        // 동작안됨..
/*        detail_todo_tag.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                val text = (v as EditText).text.toString()
                if (text != "") {
                    todo.hashTag = text
                }
            }
        }*/
        


    }

    val detailBtnListener = View.OnClickListener { view->

        when (view) {
            //눌린 버튼이 확인 버튼일 때
            detail_confirm_btn -> {
                // 날짜, 시간을 변경한적이 있다면
                if (timeFlag) {
                    todo.time = time
                    todo.date = date
                }
                // 할일
                todo.text = detail_todo_input.text.toString()

                // Tag
                val text = detail_todo_tag.text.toString()
                if (text != "") {
                    todo.hashTag = text
                }

                bundle.putSerializable("todo",todo)
                intent.putExtra("data",bundle)
                setResult(Activity.RESULT_OK,intent)
                finish()
            }
            //눌린 버튼이 취소버튼일 때
            detail_cancel_btn -> {
                setResult(Activity.RESULT_CANCELED, intent)
                finish()
            }
        }

    }
}