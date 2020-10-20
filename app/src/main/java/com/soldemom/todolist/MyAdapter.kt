package com.soldemom.todolist

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_todo.view.*
import java.text.SimpleDateFormat

class MyAdapter(val context: Context,
                val itemList: ArrayList<Todo>
) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_todo,parent,false)
        return MyViewHolder(view)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val todo = itemList[position]
        holder.todoText.text = todo.text

        holder.todoIsDone.isChecked = todo.isDone

        if (todo.hashTag != null) {
            holder.todoHashTag.apply{
                visibility = View.VISIBLE
                text = "#${todo.hashTag}"
            }
        } else {
            holder.todoHashTag.apply {
                visibility = View.GONE
                text = ""
            }
        }

        if (todo.time != null) {
            val sdf = SimpleDateFormat("HH:mm")
            holder.todoTime.apply {
                text = sdf.format(todo.time)
                visibility = View.VISIBLE
            }
        } else {
            holder.todoTime.apply {
                text = ""
                visibility = View.GONE
            }
        }

        holder.todoDelete.setOnClickListener {
            var alertDialog = AlertDialog.Builder(context)
                .setMessage("정말 삭제하시겠습니까?")
                .setPositiveButton("삭제") {str, dialogInterface ->
                    itemList.removeAt(position)
                    notifyDataSetChanged()
                }
                .setNegativeButton("취소",null)
            alertDialog.show()
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        val todoText = itemView.todo_text
        val todoTime = itemView.todo_time
        val todoHashTag = itemView.todo_hash_tag
        val todoDelete = itemView.todo_delete
        val todoIsDone: CheckBox = itemView.todo_done



    }


}