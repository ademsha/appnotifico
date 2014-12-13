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

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

public class NotificationHelper {

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
