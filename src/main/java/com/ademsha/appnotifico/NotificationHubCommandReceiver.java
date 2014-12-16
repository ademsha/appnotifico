/*
 * Copyright 2014 ademsha.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ademsha.appnotifico;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
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
        else if(intent.getStringExtra("command").equals("cancel-list")
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            cancelNotifications(intent);
        }
        else if(intent.getStringExtra("command").equals("cancel")
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            cancelNotification(intent);
        }
        else if(intent.getStringExtra("command").equals("get-all")){
            getAllActiveNotifications(context);
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
                notifications.put(NotificationDataHelper.getStatusBarNotificationDataAsJSON(statusBarNotification));
            }
            Intent dataIntent = new Intent(NotificationHubConfig.NOTIFICATION_HUB_DATA_RECIEVER_INTENT);
            dataIntent.putExtra("command", "get-all");
            dataIntent.putExtra("data", " " + notifications.toString() + "\n");
            context.getApplicationContext().sendBroadcast(dataIntent);
        }
    }

    private void getAllActiveNotifications(Context context) {
        JSONArray notifications=new JSONArray();
        for (StatusBarNotification statusBarNotification : notificationHub.getActiveNotifications()) {
            notifications.put(NotificationDataHelper.getStatusBarNotificationDataAsJSON(statusBarNotification));
        }
        if(notifications.length()>0) {
            Intent dataIntent = new Intent(NotificationHubConfig.NOTIFICATION_HUB_DATA_RECIEVER_INTENT);
            dataIntent.putExtra("command", "get-all");
            dataIntent.putExtra("data", " " + notifications.toString() + "\n");
            context.getApplicationContext().sendBroadcast(dataIntent);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void cancelNotification(Intent intent) {
        String key=intent.getStringExtra("data");
        if(!key.isEmpty()) {
            notificationHub.cancelNotification(key);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
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
