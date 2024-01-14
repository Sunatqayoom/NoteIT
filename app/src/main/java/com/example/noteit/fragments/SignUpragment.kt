package com.example.noteit.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.noteit.R
import com.example.noteit.databinding.FragmentSignInBinding
import com.example.noteit.databinding.FragmentSignUpragmentBinding
import com.google.firebase.auth.FirebaseAuth


class SignUpragment : Fragment() {
    private lateinit var auth:FirebaseAuth
    private lateinit var navController:NavController  //to navigate through difeerent fragements
    private lateinit var binding: FragmentSignUpragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=FragmentSignUpragmentBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        registerEvents()
    }

    private fun registerEvents() {

        binding.authTextView.setOnClickListener{
            navController.navigate(R.id.action_signUpragment2_to_signInFragment2)

        }
        binding.nextbtn.setOnClickListener {
            val email=binding.emailEt.text.toString().trim()
            val pass=binding.passEt.text.toString().trim()
            val verifyPass=binding.repassEt.text.toString().trim()

            if(email.isNotEmpty() && pass.isNotEmpty() && verifyPass.isNotEmpty()) {
                if(pass==verifyPass) {
                    auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener({
                        if(it.isSuccessful) {
                            Toast.makeText(context,"Registered Successfully",Toast.LENGTH_SHORT).show()
                            //send to home fragment
                            navController.navigate(R.id.action_signUpragment2_to_homeFragment2)
                        } else {
                            Toast.makeText(context,it.exception?.message,Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
        }
    }

    private fun init(view: View) {
        navController=Navigation.findNavController(view)
        auth=FirebaseAuth.getInstance()

    }


}