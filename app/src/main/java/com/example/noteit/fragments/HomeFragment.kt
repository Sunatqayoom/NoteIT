package com.example.noteit.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noteit.databinding.FragmentHomeBinding
import com.example.noteit.utils.ToDoData
import com.example.noteit.utils.TodoAdapter
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HomeFragment : Fragment(), AddToDoPopUpFragment.DialogueNextBtnClickListner,
    TodoAdapter.ToDoAdapterClickInterface {
    private lateinit var auth:FirebaseAuth
    private lateinit var databaseRef : DatabaseReference
    private lateinit var navController: NavController
    private lateinit var binding:FragmentHomeBinding
    private  var popUpFragment: AddToDoPopUpFragment?=null
    private lateinit var adapter: TodoAdapter
    private lateinit var mlist:MutableList<ToDoData>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)

        getDataFromFirebase()
        registerEvents()
    }

    private fun registerEvents() {
        binding.adddBtnHome.setOnClickListener {
            if(popUpFragment!=null) {
                childFragmentManager.beginTransaction().remove(popUpFragment!!).commit()
            }
            popUpFragment= AddToDoPopUpFragment()
            popUpFragment!!.setListener(this)
            popUpFragment!!.show(childFragmentManager,"AddToDoPopUpFragment")

        }
    }

    private fun init(view: View) {
        navController=Navigation.findNavController(view)
        auth=FirebaseAuth.getInstance()
        databaseRef=FirebaseDatabase.getInstance().reference.child("Tasks").child(auth.currentUser?.uid.toString())


        binding.recylerView.setHasFixedSize(true)
        binding.recylerView.layoutManager=LinearLayoutManager(context)
        mlist= mutableListOf()
        adapter= TodoAdapter(mlist)
        adapter.setListner(this)
        binding.recylerView.adapter=adapter

    }

    private fun getDataFromFirebase() {
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mlist.clear()
                for (taskSnapshot in snapshot.children) {
                    val todoTask = taskSnapshot.key?.let { ToDoData(it, taskSnapshot.value.toString()) }
                    if (todoTask != null) {
                        mlist.add(todoTask)
                    }
                }
                Log.d(TAG, "onDataChange: " + mlist)
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }


    override fun onSaveTask(todo: String, todoEt: TextInputEditText) {
        // Extract the text content from the TextInputEditText
        val todoText = todoEt.text.toString().trim()

        // Check if the text is not empty before saving to the database
        if (todoText.isNotEmpty()) {
            databaseRef.push().setValue(todoText).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(context, "Task Added Successfully", Toast.LENGTH_SHORT).show()
                    todoEt.text = null
                } else {
                    Toast.makeText(context, it.exception.toString(), Toast.LENGTH_SHORT).show()
                }
                popUpFragment!!.dismiss()
            }
        } else {
            // Handle the case where the text is empty
            Toast.makeText(context, "Task text is empty", Toast.LENGTH_SHORT).show()
            popUpFragment!!.dismiss()
        }
    }


    override fun updateTask(tododata: ToDoData, todoEdit: TextInputEditText) {
        val map = HashMap<String, Any>()
        map[tododata.taskId] = tododata.task
        databaseRef.updateChildren(map).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
            popUpFragment!!.dismiss()
        }
    }

    override fun onDeleteTaskBtnClicked(toDoData: ToDoData) {
        databaseRef.child(toDoData.taskId).removeValue().addOnCompleteListener{
            if(it.isSuccessful){

                Toast.makeText(context,"Deleted Successfully",Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()


            }
        }
    }

    override fun onEditTaskBtnClicked(toDoData: ToDoData) {
        if(popUpFragment!=null) {
            childFragmentManager.beginTransaction().remove(popUpFragment!!).commit()
        }
        popUpFragment = AddToDoPopUpFragment.newInstance(toDoData.taskId, toDoData.task)
        popUpFragment!!.setListener(this)
        popUpFragment!!.show(
            childFragmentManager,
            AddToDoPopUpFragment.TAG
        )


    }


}




