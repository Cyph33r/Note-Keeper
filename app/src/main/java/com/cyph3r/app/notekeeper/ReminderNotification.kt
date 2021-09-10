package com.cyph3r.app.notekeeper

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat

/**
 * Helper class for showing and canceling reminder
 * notifications.
 *
 *
 * This class makes heavy use of the [NotificationCompat.Builder] helper
 * class to create notifications in a backward-compatible way.
 */
object ReminderNotification {
    /**
     * The unique identifier for this type of notification.
     */
    private const val NOTIFICATION_TAG = "Reminder"

    const val REMINDER_CHANNEL = "reminders"

    /**
     * Shows the notification, or updates a previously shown notification of
     * this type, with the given parameters.
     *
     * @see .cancel
     */
    fun notify(context: Context, titleText: String,
               noteText: String, number: Int) {

        val shareIntent = PendingIntent.getActivity(context,
            0,
            Intent.createChooser(Intent(Intent.ACTION_SEND)
                .setType("text/plain")
                .putExtra(Intent.EXTRA_TEXT,
                    noteText
                ),
                "Share Note Reminder"),
            PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(context, REMINDER_CHANNEL)

            // Set appropriate defaults for the notification light, sound,
            // and vibration.
            .setDefaults(Notification.DEFAULT_ALL)

            // Set required fields, including the small icon, the
            // notification title, and text.
            .setSmallIcon(R.drawable.ic_assignment_black_24dp)
            .setContentTitle(titleText)
            .setContentText(noteText)

            // All fields below this line are optional.

            // Use a default priority (recognized on devices running Android
            // 4.1 or later)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            // Set ticker text (preview) information for this notification.
            .setTicker(titleText)

            // Set the pending intent to be initiated when the user touches
            // the notification.
            .setContentIntent(
                PendingIntent.getActivity(
                    context,
                    0,
                    Intent(context, NoteListActivity::class.java),
                    PendingIntent.FLAG_UPDATE_CURRENT))

            // Automatically dismiss the notification when it is touched.
            .setAutoCancel(true)

            // Add a share action
            .addAction(R.drawable.ic_share_black_24dp,
                "Share", shareIntent)

        notify(context, builder.build())
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private fun notify(context: Context, notification: Notification) {
        val nm = context
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.notify(NOTIFICATION_TAG, 0, notification)
    }

    /**
     * Cancels any notifications of this type previously shown using
     * [.notify].
     */
    @TargetApi(Build.VERSION_CODES.ECLAIR)
    fun cancel(context: Context) {
        val nm = context
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.cancel(NOTIFICATION_TAG, 0)
        } else {
            nm.cancel(NOTIFICATION_TAG.hashCode())
        }
    }
}
