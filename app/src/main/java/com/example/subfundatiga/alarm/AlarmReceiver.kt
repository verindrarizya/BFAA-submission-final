package com.example.subfundatiga.alarm

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.subfundatiga.R
import com.example.subfundatiga.activities.MainActivity
import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        private const val ID_REPEATS = 101
        private val TAG = AlarmReceiver::class.java.simpleName
    }

    override fun onReceive(context: Context, intent: Intent) {
        showNotification(context)
        Log.d(TAG, "onReceive: SET")
    }

    fun setRepeatingAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATS, intent, 0)

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 9)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)

        Log.d(TAG, "setRepeatingAlarm: SET")
        val toastMessage = context.resources.getString(R.string.alarm_on_toast_message)
        Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
    }

    fun cancelAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATS, intent, 0)
        pendingIntent.cancel()

        alarmManager.cancel(pendingIntent)

        Log.d(TAG, "cancelAlarm: SET")
        val toastMessage = context.resources.getString(R.string.alarm_off_toast_message)
        Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
    }

    private fun showNotification(context: Context) {
        val channelId = "channel_1"
        val channelName = "repeating_alarm_manager_channel"

        val notificationManagerCompat = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val vibrationConfig = longArrayOf(1000, 1000, 1000, 1000, 1000)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(context, ID_REPEATS, intent, 0)

        val builder = NotificationCompat.Builder(context, channelId)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_alarms_black_24)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setContentTitle(context.resources.getString(R.string.alarm_notification_title))
            .setContentTitle(context.resources.getString(R.string.alarm_notification_text))
            .setSound(alarmSound)
            .setVibrate(vibrationConfig)
            .setAutoCancel(true)

        // cek android version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            channel.enableVibration(true)
            channel.vibrationPattern = vibrationConfig

            builder.setChannelId(channelId)

            notificationManagerCompat.createNotificationChannel(channel)
        }

        val notification = builder.build()
        notificationManagerCompat.notify(ID_REPEATS, notification)
    }
}