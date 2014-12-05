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
        if(!isAccessEnabled || !checkAccessViaContentResolver(context)){
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
