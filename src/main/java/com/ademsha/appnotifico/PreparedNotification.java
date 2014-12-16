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

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

public class PreparedNotification {

    private int id;
    private int largeIcon;
    private int smallIcon;
    private String ticker;
    private String title;
    private String text;
    private String expandedSummary="";
    private String expanded="";
    private Service service;
    private Intent intent;
    private Context context;

    public PreparedNotification(Context context){
        this.context = context;
    }

    public PendingIntent getPendingIntentForActivity()
    {
       return PendingIntent.getActivity(getContext(), 0, getIntent(), 0);
    }

    public PendingIntent getPendingIntentForService()
    {
        return PendingIntent.getActivity(getContext(), 0, getIntent().setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getExpandedSummary() {
        return expandedSummary;
    }

    public void setExpandedSummary(String expandedSummary) {
        this.expandedSummary = expandedSummary;
    }

    public String getExpanded() {
        return expanded;
    }

    public void setExpanded(String expanded) {
        this.expanded = expanded;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public Context getContext() {
        return context;
    }

    public Bitmap getLargeIcon() {
        return BitmapFactory.decodeResource(context.getResources(), largeIcon);
    }

    public void setLargeIcon(int largeIcon) {
        this.largeIcon = largeIcon;
    }

    public int getSmallIcon() {
        return smallIcon;
    }

    public void setSmallIcon(int smallIcon) {
        this.smallIcon = smallIcon;
    }

    public int getId() {
        if(id>0)
            return id;
        else
            return new Random().nextInt(10000);
    }

    public void setId(int id) {
        this.id = id;
    }
}
