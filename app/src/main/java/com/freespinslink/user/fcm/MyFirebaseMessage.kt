/*
 *
 * Created by Saad Iftikhar on 10/20/21, 10:35 AM
 * Copyright (c) 2021. All rights reserved
 *
 */

package com.freespinslink.user.fcm

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.freespinslink.user.R
import com.freespinslink.user.views.activity.SplashActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessage : FirebaseMessagingService() {

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        if (p0.notification != null) {

            p0.notification?.let {
                val title = it.title
                val body = it.body
                Log.d("FCM_Message", "onMessageReceived: $title $body")
                sendNotification(title.toString(), body.toString())
            }

        } else
            Log.d("FCM_Message", "Empty")

    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun sendNotification(title: String, body: String) {

        val intent = Intent(applicationContext, SplashActivity::class.java)

        val pendingIntent = try {
            PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        } catch (e: Exception) {
            null
        }

        val channelId = applicationContext.getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }


}