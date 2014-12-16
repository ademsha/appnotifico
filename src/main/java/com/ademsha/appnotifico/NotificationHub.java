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

import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

public class NotificationHub extends NotificationListenerService {

    private NotificationHubCommandReceiver notificationHubCommandReceiver;

    @Override
    public IBinder onBind(Intent mIntent) {
        IBinder binder = super.onBind(mIntent);
        NotificationAccessHelper.isAccessEnabled = true;
        return binder;
    }

    @Override
    public boolean onUnbind(Intent mIntent) {
        boolean state = super.onUnbind(mIntent);
        NotificationAccessHelper.isAccessEnabled = false;
        return state;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerCommandReceiver();
    }

    private void registerCommandReceiver() {
        notificationHubCommandReceiver = new NotificationHubCommandReceiver(this);
        IntentFilter filter = new IntentFilter(NotificationHubConfig.NOTIFICATION_HUB_COMMAND_RECIEVER_INTENT);
        registerReceiver(notificationHubCommandReceiver,filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(notificationHubCommandReceiver);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification statusBarNotification) {
        sendDataToReceiver(statusBarNotification, "notification-added");
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification statusBarNotification) {
        sendDataToReceiver(statusBarNotification, "notification-removed");
    }

    private void sendDataToReceiver(StatusBarNotification statusBarNotification, String type) {
        Intent intent = new  Intent(NotificationHubConfig.NOTIFICATION_HUB_DATA_RECIEVER_INTENT);
        intent.putExtra("command", type);
        intent.putExtra("data",NotificationDataHelper.getStatusBarNotificationDataAsJSON(statusBarNotification).toString());
        sendBroadcast(intent);
    }
}
