package com.ademsha.notifmngr;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.service.notification.StatusBarNotification;

import org.json.JSONArray;

public class NotificationHubCommandReceiver extends BroadcastReceiver {

    private NotificationHub notificationHub;

    public NotificationHubCommandReceiver(final NotificationHub notificationHub)
    {
        this.notificationHub = notificationHub;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getExtras()!=null && intent.getStringExtra("command")!=null)
        {
            executeCommands(context,intent);
        }
    }

    private void executeCommands(Context context,Intent intent) {
        if(intent.getStringExtra("command").equals("cancel-all")){
            cancelAllNotifications();
        }
        else if(intent.getStringExtra("command").equals("cancel-list")){
            cancelNotifications(intent);
        }
        else if(intent.getStringExtra("command").equals("cancel")){
            cancelNotification(intent);
        }
        else if(intent.getStringExtra("command").equals("get-all")){
            getAllActiveNotifications(context, intent);
        }
        else if(intent.getStringExtra("command").equals("get-list")){
            getActiveNotifications(context, intent);
        }
    }

    private void getActiveNotifications(Context context, Intent intent) {
        String[] keys=intent.getStringArrayExtra("data");
        if(keys.length>0) {
            JSONArray notifications = new JSONArray();
            for (StatusBarNotification statusBarNotification : notificationHub.getActiveNotifications(keys)) {
                notifications.put(NotificationHelper.getStatusBarNotificationDataAsJSON(statusBarNotification).toString());
            }
            Intent dataIntent = new Intent(NotificationHubConfig.NOTIFICATION_HUB_DATA_RECIEVER_INTENT);
            dataIntent.putExtra("type", "get-all");
            dataIntent.putExtra("data", " " + notifications.toString() + "\n");
            context.getApplicationContext().sendBroadcast(intent);
        }
    }

    private void getAllActiveNotifications(Context context, Intent intent) {
        JSONArray notifications=new JSONArray();
        for (StatusBarNotification statusBarNotification : notificationHub.getActiveNotifications()) {
            notifications.put(NotificationHelper.getStatusBarNotificationDataAsJSON(statusBarNotification).toString());
        }
        Intent dataIntent = new  Intent(NotificationHubConfig.NOTIFICATION_HUB_DATA_RECIEVER_INTENT);
        dataIntent.putExtra("type","get-all");
        dataIntent.putExtra("data"," " + notifications.toString() + "\n");
        context.getApplicationContext().sendBroadcast(intent);
    }

    private void cancelNotification(Intent intent) {
        String key=intent.getStringExtra("data");
        if(!key.isEmpty()) {
            notificationHub.cancelNotification(key);
        }
    }

    private void cancelNotifications(Intent intent) {
        String[] keys=intent.getStringArrayExtra("data");
        if(keys.length>0) {
            notificationHub.cancelNotifications(keys);
        }
    }

    private void cancelAllNotifications() {
        notificationHub.cancelAllNotifications();
    }

}
