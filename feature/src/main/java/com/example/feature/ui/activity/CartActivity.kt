/*
 * Copyright 2021 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.feature.ui.activity

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.core.network.utilities.Resource
import com.example.feature.R
import com.example.feature.databinding.ActivityCartBinding
import com.example.feature.ui.viewmodel.CartViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_cart.*

@AndroidEntryPoint
class CartActivity : AppCompatActivity() {

    //viewBinding
    private val cartVModel: CartViewModel by viewModels()

    private val binding : ActivityCartBinding by lazy {
        DataBindingUtil.setContentView<ActivityCartBinding>(this, R.layout.activity_cart).apply {
            lifecycleOwner = this@CartActivity
            vm = cartVModel
        }
    }
    //private var _binding: ActivityCartBinding? = null
    //private val binding get() = _binding!!
    private var buttonSend: Button? = null
    private var name: EditText? = null
    private var loadingProgress: ProgressBar? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding = ActivityCartBinding.inflate(layoutInflater)
        val root = binding.root
        setContentView(root)
        initView()
        val intent = intent
        val message = intent.getStringExtra("message")

        val fcmtoken = FirebaseMessaging.getInstance().token
        Log.e("Cart-Activity", "Token perangkat ini: ${fcmtoken}")
        if(!message.isNullOrEmpty()) {
            AlertDialog.Builder(this)
                .setTitle("Notification")
                .setMessage(message)
                .setPositiveButton("Ok", { dialog, which -> }).show()
        }
    }

    private fun initView(){

        loadingProgress = binding.loading
        buttonSend = binding.btnSend
        name = binding.etName

        //listen to click event
        buttonSend!!.setOnClickListener {

            //hide button
            buttonSend!!.visibility = View.GONE

            //show progress bar
            loadingProgress!!.visibility = View.VISIBLE

            //register user
            doRegisterUser()
        }

    }

    private fun doRegisterUser(){

        //get user notification token provided by firebase
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("token_failed", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val notificationToken = task.result
            val nameString = etName.text.toString()
            //store the user name
            cartVModel.doSendNotification(nameString, notificationToken!!)
            setupObserver()
        })

    }

    private fun setupObserver(){

        //observe data obtained
        cartVModel.sendNotification.observe(this, Observer {

            when(it.status){

                Resource.Status.SUCCESS ->{

                    if(it.data?.status == "success"){

                        //stop progress bar
                        loadingProgress!!.visibility = View.GONE
                        buttonSend!!.visibility = View.VISIBLE

                        //show toast message
                        Toast.makeText(this, "Notification sent successfully", Toast.LENGTH_LONG).show()
                    }

                    else if(it.data?.status == "fail"){

                        //stop progress bar
                        loadingProgress!!.visibility = View.GONE
                        buttonSend!!.visibility = View.VISIBLE

                        //something went wrong, show error message
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()

                    }


                }
                Resource.Status.ERROR -> {

                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()

                    loading!!.visibility = View.GONE
                    buttonSend!!.visibility = View.VISIBLE

                }
                Resource.Status.LOADING -> {

                    loading!!.visibility = View.VISIBLE
                    buttonSend!!.visibility = View.GONE

                }
            }

        })

    }


}