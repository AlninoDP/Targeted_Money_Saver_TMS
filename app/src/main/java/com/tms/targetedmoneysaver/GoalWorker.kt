package com.tms.targetedmoneysaver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.tms.targetedmoneysaver.data.local.room.GoalDao
import com.tms.targetedmoneysaver.data.local.room.GoalDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GoalWorker(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return getClosestGoal()
    }

    private suspend fun getClosestGoal(): Result {
        return withContext(Dispatchers.IO) {
            try {
                val closestGoal = goalDao.getClosestGoalForNotification()
                if (closestGoal != null) {
                    val title = "Don't forget to save for ${closestGoal.title}"
                    val description =
                        "Only ${closestGoal.daysRemaining} save remaining, save ${closestGoal.dailySave} now"
                    showNotification(title, description)
                } else {
                    val title = "Don\t forget to save today"
                    val description = "Your goal is within reach, keep going to achieve it"
                    showNotification(title, description)
                }
                Result.success()
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching closest goal", e)
                showNotification("Something went wrong", e.message)
                Result.failure()
            }
        }

    }

    private val goalDao: GoalDao by lazy {
        GoalDatabase.getInstance(applicationContext).goalDao()
    }


    private fun showNotification(title: String, description: String?) {
        val notificationManager =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val notification: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)

        val channel =
            NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)

        notification.setChannelId(CHANNEL_ID)
        notificationManager.createNotificationChannel(channel)

        notificationManager.notify(NOTIFICATION_ID, notification.build())

    }

    companion object {
        private val TAG = Worker::class.java.simpleName
        const val NOTIFICATION_ID = 1
        const val CHANNEL_ID = "channel_01"
        const val CHANNEL_NAME = "goal_channel"
    }

}