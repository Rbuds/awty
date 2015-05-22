package com.example.ryan.arewethereyet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.widget.Toast;

/**
 * Created by Ryan on 5/12/2015.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent != null) {
            String targetNum = intent.getStringExtra("targetNum");
            String sms = intent.getStringExtra("message");

            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(targetNum, null, sms, null, null);
                Toast.makeText(context, "SMS Sent!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(context, "SMS faild, please try again later!",
                        Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }
}
