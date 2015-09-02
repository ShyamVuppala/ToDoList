package com.utilities.shyamv.todolist;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;

public class NotifyService extends Service {
    public NotifyService()
    {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        if(intent != null)
        {
            long taskId = intent.getLongExtra(ToDoTaskListActivity.ARG_TASK_ID, -1);
            String taskTitle = intent.getStringExtra(ToDoTaskListActivity.ARG_TASK_TITLE);
            if(taskId != -1)
            {
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, ToDoTaskListActivity.class), 0);

                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
                mBuilder.setSmallIcon(R.mipmap.ic_launcher);
                mBuilder.setContentTitle("To Do Task");
                mBuilder.setContentText(taskTitle);
                mBuilder.setContentIntent(pendingIntent);
                mBuilder.setDefaults(Notification.DEFAULT_SOUND);
                mBuilder.setAutoCancel(true);
                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify((int)taskId, mBuilder.build());
            }
        }

        stopSelf(startId);

        return START_NOT_STICKY;
    }
}
