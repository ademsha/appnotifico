/*
 * Copyright 2014 ademsha.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License"));
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

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class NotificationHelper {

    public static JSONObject getStatusBarNotificationDataAsJSON(StatusBarNotification statusBarNotification)
    {
        JSONObject notification=new JSONObject();
        try {
            notification.put("id",String.valueOf(statusBarNotification.getId()).replace("null",""));
            notification.put("ticker_text",String.valueOf(statusBarNotification.getNotification().tickerText).replace("null",""));
            notification.put("priority",String.valueOf(statusBarNotification.getNotification().priority).replace("null",""));
            notification.put("number",String.valueOf(statusBarNotification.getNotification().number).replace("null",""));
            notification.put("tag",String.valueOf(statusBarNotification.getTag()).replace("null",""));
            notification.put("posted_at",String.valueOf(statusBarNotification.getPostTime()).replace("null",""));
            notification.put("source",String.valueOf(statusBarNotification.getPackageName()).replace("null",""));
            notification.put("when",String.valueOf(statusBarNotification.getNotification().when).replace("null",""));
            notification.put("led_argb",String.valueOf(statusBarNotification.getNotification().ledARGB).replace("null",""));
            notification.put("led_OnMS",String.valueOf(statusBarNotification.getNotification().ledOnMS).replace("null",""));
            notification.put("led_OnMS",String.valueOf(statusBarNotification.getNotification().ledOffMS).replace("null",""));
            notification.put("vibrate", Arrays.toString(statusBarNotification.getNotification().vibrate).replace("null",""));
            if(statusBarNotification.getNotification().sound!=null)
            {
                notification.put("sound", String.valueOf(statusBarNotification.getNotification().sound.getPath()));
            }
            notification.put("action_intent_package",String.valueOf(statusBarNotification.getNotification().contentIntent.getCreatorPackage()));
            notification.put("action_intent_uid",String.valueOf(statusBarNotification.getNotification().contentIntent.getCreatorUid()));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                notification.put("visibility",String.valueOf(statusBarNotification.getNotification().visibility));
                notification.put("color",String.valueOf(statusBarNotification.getNotification().color));
                notification.put("category",String.valueOf(statusBarNotification.getNotification().category));
                notification.put("user",String.valueOf(statusBarNotification.getUser().toString()));
                notification.put("group_key",String.valueOf(statusBarNotification.getGroupKey()));
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                notification.put("key",String.valueOf(statusBarNotification.getKey()));
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                notification = getNotificationExtras(notification,statusBarNotification);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return notification;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static JSONObject getNotificationExtras(JSONObject notification,StatusBarNotification statusBarNotification) {
        try {
            Bundle extras = statusBarNotification.getNotification().extras;
            if (extras != null) {
                notification.put("text", extras.getString(Notification.EXTRA_TEXT));
                notification.put("sub_text", extras.getString(Notification.EXTRA_SUB_TEXT));
                notification.put("summary_text", extras.getString(Notification.EXTRA_SUMMARY_TEXT));
                notification.put("text_lines", Arrays.toString(extras.getCharSequenceArray(Notification.EXTRA_TEXT_LINES)).replace("null",""));
                notification.put("icon", String.valueOf(extras.getInt(Notification.EXTRA_SMALL_ICON)));
                if(extras.getParcelable(Notification.EXTRA_LARGE_ICON)!=null) {
                    notification.put("large_icon", String.valueOf(extras.getParcelable(Notification.EXTRA_LARGE_ICON).toString()).replace("null",""));
                }
                notification.put("title", extras.getString(Notification.EXTRA_TITLE));
                notification.put("title_big", extras.getString(Notification.EXTRA_TITLE_BIG));
                notification.put("progress", extras.getInt(Notification.EXTRA_PROGRESS));
                notification.put("progress_indeterminate", String.valueOf(extras.getBoolean(Notification.EXTRA_PROGRESS_INDETERMINATE)));
                notification.put("progress_max", String.valueOf(extras.getInt(Notification.EXTRA_PROGRESS_MAX)));
                notification.put("people", extras.getStringArray(Notification.EXTRA_PEOPLE));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return notification;
    }

    public static void notify(PreparedNotification preparedNotification) {

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(preparedNotification.getContext())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setOngoing(false)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true);

        builder.setContentTitle(preparedNotification.getTitle());
        builder.setContentText(preparedNotification.getText());
        if(preparedNotification.getLargeIcon()!=null) {
            builder.setLargeIcon(preparedNotification.getLargeIcon());
        }
        if(preparedNotification.getSmallIcon()>0) {
            builder.setSmallIcon(preparedNotification.getSmallIcon());
        }
        builder.setTicker(preparedNotification.getTicker());

        if(!preparedNotification.getExpanded().equals("") && !preparedNotification.getExpandedSummary().equals("")) {
            builder.setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(preparedNotification.getExpanded())
                        .setBigContentTitle(preparedNotification.getTitle())
                        .setSummaryText(preparedNotification.getExpandedSummary()));
        }

        if(preparedNotification.getPendingIntentForActivity()!=null) {
            builder.setContentIntent(preparedNotification.getPendingIntentForActivity());
        }

        NotificationManager notificationManager = (NotificationManager) preparedNotification.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(preparedNotification.getId(), builder.build());

    }

    public static void notifyForForegroundService(final Service service, final PreparedNotification preparedNotification) {

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(preparedNotification.getContext())
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setOngoing(true)
                .setAutoCancel(false)
                .setOnlyAlertOnce(true);

        if(preparedNotification.getPendingIntentForActivity()!=null){
            builder.setContentIntent(preparedNotification.getPendingIntentForService());
        }

        builder.setContentTitle(preparedNotification.getTitle());
        builder.setContentText(preparedNotification.getText());

        if(preparedNotification.getLargeIcon()!=null) {
            builder.setLargeIcon(preparedNotification.getLargeIcon());
        }

        if(preparedNotification.getSmallIcon()>0) {
            builder.setSmallIcon(preparedNotification.getSmallIcon());
        }

        builder.setTicker(preparedNotification.getTicker());

        if(!preparedNotification.getExpanded().equals("") && !preparedNotification.getExpandedSummary().equals("")) {
            builder.setStyle(new NotificationCompat.BigTextStyle()
                    .bigText(preparedNotification.getExpanded())
                    .setBigContentTitle(preparedNotification.getTitle())
                    .setSummaryText(preparedNotification.getExpandedSummary()));
        }

        if(preparedNotification.getPendingIntentForActivity()!=null) {
            builder.setContentIntent(preparedNotification.getPendingIntentForActivity());
        }

        Notification notification;
        notification = builder.build();
        notification.flags |= Notification.FLAG_ONGOING_EVENT | Notification.FLAG_FOREGROUND_SERVICE | Notification.FLAG_NO_CLEAR;
        service.startForeground(10000, notification);
    }
}
