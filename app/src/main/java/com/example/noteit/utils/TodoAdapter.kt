package com.example.noteit.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noteit.databinding.EachTodoItemBinding
import com.example.noteit.databinding.FragmentAddToDoPopUpBinding

class TodoAdapter(private val list:MutableList<ToDoData>):RecyclerView.Adapter<TodoAdapter.ToDoViewHolder>() {
    private var listner:ToDoAdapterClickInterface?=null
    fun setListner(listner:ToDoAdapterClickInterface) {
        this.listner=listner
    }

    inner class ToDoViewHolder( val binding:EachTodoItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val binding=EachTodoItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ToDoViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        with(holder) {
            with(list[position]) {
                binding.todoTask.text=this.task
                binding.deleteTask.setOnClickListener {

                    listner?.onDeleteTaskBtnClicked(this)
                }
                binding.editTask.setOnClickListener {
                    listner?.onEditTaskBtnClicked(this)

                }

            }
        }
    }

    interface ToDoAdapterClickInterface{
        fun onDeleteTaskBtnClicked(toDoData: ToDoData)
        fun onEditTaskBtnClicked(toDoData: ToDoData)
    }
}