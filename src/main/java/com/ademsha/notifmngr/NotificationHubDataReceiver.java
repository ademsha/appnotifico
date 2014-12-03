package com.ademsha.notifmngr;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NotificationHubDataReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getExtras()!=null) {
            String type=intent.getStringExtra("type");
            Toast.makeText(context,type,Toast.LENGTH_SHORT).show();
        }
    }
}
