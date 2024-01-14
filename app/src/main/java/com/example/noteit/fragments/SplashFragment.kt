package com.example.noteit.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.noteit.R
import com.google.firebase.auth.FirebaseAuth


class SplashFragment : Fragment() {
    lateinit var auth: FirebaseAuth
    lateinit var navController: NavController



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        auth=FirebaseAuth.getInstance()
        navController=Navigation.findNavController(view)
        Handler(Looper.myLooper()!!).postDelayed(Runnable {
            if (auth.currentUser!=null) {
                navController.navigate(R.id.action_splashFragment2_to_homeFragment2)

        } else {

            navController.navigate(R.id.action_splashFragment2_to_signInFragment2)
            }


        },2000)
    }


    }
