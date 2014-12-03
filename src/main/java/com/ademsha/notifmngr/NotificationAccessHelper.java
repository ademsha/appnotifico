package com.ademsha.notifmngr;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;

public class NotificationAccessHelper {

    public static boolean isAccessEnabled;

    public static boolean checkAccessViaContentResolver(Context context){
        ContentResolver contentResolver = context.getContentResolver();
        String enabledNotificationListeners = Settings.Secure.getString(contentResolver, "enabled_notification_listeners");
        String packageName = context.getPackageName();
        return !(enabledNotificationListeners == null || !enabledNotificationListeners.contains(packageName));
    }

    public static void askUser(final Context context) {
        if(!isAccessEnabled || !checkAccessViaContentResolver(context)) {
            new AlertDialog.Builder(context)
                    .setMessage("Please enable access to notifications for this app")
                    .setTitle("Notification Access")
                    .setIconAttribute(android.R.attr.alertDialogIcon)
                    .setCancelable(true)
                    .setPositiveButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    openSettings(context);
                                }
                            })
                    .setNegativeButton(android.R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // do nothing
                                }
                            })
                    .create().show();
        }
    }

    private static void openSettings(Context context) {
        context.startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
    }
}
