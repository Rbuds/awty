package com.example.ryan.arewethereyet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Ryan on 5/12/2015.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent != null) {
            String targetNum = intent.getStringExtra("targetNum");
            Toast.makeText(context, targetNum + ": Are we there yet?", Toast.LENGTH_SHORT).show();
        }
    }
}
