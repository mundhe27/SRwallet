package com.bsr.bsrcoin.Noitification

//import com.google.firebase.messaging.FirebaseMessagingService
//import com.google.firebase.messaging.RemoteMessage

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.bsr.bsrcoin.Login

import com.bsr.bsrcoin.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


private const val CHANNEL_ID = "transaction"


class FirebaseService : FirebaseMessagingService() {

    var title = ""
    var body = ""

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)

        if(p0.notification?.body != null){
            title= p0.notification!!.title!!
            body= p0.notification!!.body!!
        }
        Log.e("Notifications", "onMessageReceived: $title" )
        Log.e("Notifications", "onMessageReceived: $body" )

        val intent = Intent(this, Login::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK )
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)

        val pending = PendingIntent.getActivity(this,0,intent,0 )

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
        builder.setContentTitle(title)
        builder.setContentText(body)
        builder.setPriority(NotificationCompat.PRIORITY_HIGH)
        builder.setSmallIcon(R.drawable.bill)
        builder.setVibrate(longArrayOf(1000,300,1000,300))
        builder.setStyle(NotificationCompat.BigTextStyle().bigText(body))
        builder.setContentIntent(pending)
        builder.setAutoCancel(true)

         val notificationmanager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val Notichannel = NotificationChannel(CHANNEL_ID,"TRANSACTIONS",NotificationManager.IMPORTANCE_HIGH)
            notificationmanager.createNotificationChannel(Notichannel)
        }
        notificationmanager.notify(0,builder.build())

    }
}



    /*

    companion object {
//        FirebaseMessagingService()
        var sharedPref: SharedPreferences? = null
        var token: String?
        get() {
            return sharedPref?.getString("token","")
        }
        set(value) {
            sharedPref?.edit()?.putString("token", value)?.apply()
        }
    }


    var mNotificationManager: NotificationManager? = null


    override fun onMessageReceived(remoteMessage: RemoteMessage) {


        super.onMessageReceived(remoteMessage)
        Log.e("helloworld", "onMessageReceived: ${remoteMessage.notification!!.body}", )


        // vibration
        val v = getSystemService(VIBRATOR_SERVICE) as Vibrator
        val pattern = longArrayOf(100, 300, 300, 300)
        v.vibrate(pattern, -1)
        val resourceImage = resources.getIdentifier(
            remoteMessage.notification!!.icon, "drawable",
            packageName
        )
        val builder = NotificationCompat.Builder(this, "CHANNEL_ID")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            builder.setSmallIcon(R.drawable.icontrans);
            builder.setSmallIcon(R.drawable.mywallet)
        } else {
//            builder.setSmallIcon(R.drawable.icon_kritikar);
            builder.setSmallIcon(resourceImage)
        }
        val resultIntent = Intent(this, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        builder.setContentTitle(remoteMessage.notification!!.title)
        builder.setContentText(remoteMessage.notification!!.body)
        builder.setContentIntent(pendingIntent)
        builder.setStyle(
            NotificationCompat.BigTextStyle().bigText(
                remoteMessage.notification!!.body
            )
        )
        builder.setAutoCancel(true)
        builder.priority = NotificationCompat.PRIORITY_MAX
////        builder.priority = RemoteMessage.Notification.PRIORITY_MAX
        mNotificationManager =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "100"
            val channel = NotificationChannel(
                channelId,
                "Transactions",
                IMPORTANCE_HIGH
            )
            mNotificationManager!!.createNotificationChannel(channel)
            builder.setChannelId(channelId)
        }


// notificationId is a unique int for each notification that you must define
        mNotificationManager!!.notify(100, builder.build())
    }

//    override fun onNewToken(newToken: String) {
//        super.onNewToken(newToken)
//        token = newToken
//    }

//    @SuppressLint("UnspecifiedImmutableFlag")
//    override fun onMessageReceived(message: RemoteMessage) {
//        super.onMessageReceived(message)
//
//        val intent = Intent(this,MainActivity::class.java)
//        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        val notificationId = Random.nextInt()
//
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            createNotificationChannel(notificationManager)
//        }
//
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        val pendingIntent = PendingIntent.getActivity(this, 0 , intent, FLAG_ONE_SHOT)
//        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
//            .setContentTitle(message.data["title"])
//            .setContentText(message.data["message"])
//            .setSmallIcon(R.drawable.ic_icon)
//            .setAutoCancel(true)
//            .setContentIntent(pendingIntent)
//            .build()
//
//        notificationManager.notify(notificationId, notification)
//    }

//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun createNotificationChannel(notificationManager: NotificationManager){
//        val channelName = "bsr_coin_wallet_channel"
//        val channel = NotificationChannel(CHANNEL_ID, channelName, IMPORTANCE_HIGH).apply {
//            description = "EWallet Channel Description"
//            enableLights(true)
//            lightColor = Color.GREEN
//        }
//        notificationManager.createNotificationChannel(channel)
//    }
}*/