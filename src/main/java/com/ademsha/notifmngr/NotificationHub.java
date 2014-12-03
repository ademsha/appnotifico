package com.ademsha.notifmngr;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

public class NotificationHub extends NotificationListenerService {

    private NotificationHubCommandReceiver notificationHubCommandReceiver;

    @Override
    public IBinder onBind(Intent mIntent) {
        IBinder mIBinder = super.onBind(mIntent);
        NotificationAccessHelper.isAccessEnabled = true;
        return mIBinder;
    }

    @Override
    public boolean onUnbind(Intent mIntent) {
        boolean mOnUnbind = super.onUnbind(mIntent);
        NotificationAccessHelper.isAccessEnabled = false;
        return mOnUnbind;
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
        intent.putExtra("type", type);
        intent.putExtra("data",NotificationHelper.getStatusBarNotificationDataAsJSON(statusBarNotification).toString());
        sendBroadcast(intent);
    }
}
