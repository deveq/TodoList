package com.soldemom.todolist.todos

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

// TodoViewModel 객체를 생성할 때 Application을 생성자에 넣어줘야하기때문에,
// 생성자가 있는 ViewModel을 생성하기 위해서는 viewModel이 AndroidViewModel(application)을 상속 받거나
// ViewModel을 상속받은 경우는 ViewModelProvider.Factory 인터페이스를 구현해주어야함.

class ViewModelProviderFactory(val context: Context) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(TodoViewModel::class.java)) {
            TodoViewModel(context) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}