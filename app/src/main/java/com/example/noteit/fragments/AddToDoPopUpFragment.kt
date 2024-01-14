
package com.example.noteit.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.noteit.databinding.FragmentAddToDoPopUpBinding
import com.example.noteit.utils.ToDoData
import com.google.android.material.textfield.TextInputEditText

class AddToDoPopUpFragment : DialogFragment() {
    private lateinit var binding: FragmentAddToDoPopUpBinding
    private  lateinit var listener:DialogueNextBtnClickListner
    private var tododata:ToDoData?=null


    fun setListener(listener:DialogueNextBtnClickListner) {
        this.listener = listener
    }

    companion object {

        const val TAG="AddToDoPopUpFragment"
        @JvmStatic
        fun newInstance(taskId:String,task:String)=AddToDoPopUpFragment().apply {

            arguments = Bundle().apply {
                putString("taskId", taskId)
                putString("task", task)
            }
        }
    }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentAddToDoPopUpBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(arguments!=null){
            tododata = ToDoData(arguments?.getString("taskId").toString() ,arguments?.getString("task").toString())
            binding.todoEt.setText(tododata?.task)
        }

        registerEvents()
    }

    private fun registerEvents() {
        binding.todoNextBtn.setOnClickListener{
            val todoTask=binding.todoEt.toString().trim()
            if(todoTask.isNotEmpty()) {

                if(tododata==null)  {
                    listener.onSaveTask(todoTask,binding.todoEt)

                } else {
                    tododata?.task=todoTask
                    listener.updateTask(tododata!!,binding.todoEt)
                }
                listener.onSaveTask(todoTask,binding.todoEt)

            }else {
                Toast.makeText(context,"Please Enter Some Task",Toast.LENGTH_SHORT).show()
            }
        }
        binding.todoClose.setOnClickListener{
            dismiss()
        }
    }
    interface DialogueNextBtnClickListner{
         fun onSaveTask(todo:String,todoEt:TextInputEditText)
        fun updateTask(tododata: ToDoData , todoEdit:TextInputEditText)
    }


}