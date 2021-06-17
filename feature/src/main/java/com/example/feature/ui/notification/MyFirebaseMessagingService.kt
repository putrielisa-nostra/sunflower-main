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

package com.example.feature.ui.notification

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.feature.R
import com.example.feature.ui.activity.CartActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    val TAG = "FirebaseMessagingService"

    @SuppressLint("LongLogTag")
    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        Log.d(TAG, "Dikirim dari: ${remoteMessage.from}")

        if (remoteMessage.notification != null) {
            showNotification(remoteMessage.notification?.title, remoteMessage.notification?.body)
        }
    }

    private fun showNotification(title: String?, body: String?) {
        val intent = Intent(this, CartActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT)

        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(soundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notificationBuilder.build())
    }
}
//class MyFirebaseMessagingService : FirebaseMessagingService() {
//
//    companion object {
//        const val TAG = "MyFirebaseMsgService"
//    }
//
//    //this is called when a message is received
//    override fun onMessageReceived(remoteMessage: RemoteMessage) {
//
//        //check messages
//        Log.d(TAG, "From: ${remoteMessage.from}")
//
//        // Check if message contains a data payload, you can get the payload here and add as an intent to your activity
//        remoteMessage.data.let {
//            Log.d(TAG, "Message data payload: " + remoteMessage.data)
//            //get the data
//        }
//
//        // Check if message contains a notification payload, send notification
//        remoteMessage.notification?.let {
//            Log.d(TAG, "Message Notification Body: ${it.body}")
//            sendNotification(it.body!!)
//
//            val intent = Intent(this@MyFirebaseMessagingService, CartActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            intent.putExtra("message", remoteMessage.notification!!.body!!)
//            startActivity(intent)
//        }
//
//    }
//
//    override fun onNewToken(token: String) {
//
//        Log.d("rfst", "Refreshed token: $token")
//        Log.d("rfst123", "Refreshed token: $token")
//
//        // If you want to send messages to this application instance or
//        // manage this apps subscriptions on the server side, send the
//        // FCM registration token to your app server.
//        sendRegistrationToServer(token)
//
//    }
//    private fun sendRegistrationToServer(token: String?) {
//
//        //you can send the updated value of the token to your server here
//
//    }
//
//    private fun sendNotification(messageBody: String){
//        val notificationManager = ContextCompat.getSystemService(applicationContext, NotificationManager::class.java) as NotificationManager
//        notificationManager.sendNotification(messageBody, applicationContext)
//    }
//
//}