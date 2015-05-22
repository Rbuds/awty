package com.example.ryan.arewethereyet;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class StartScreen extends ActionBarActivity implements View.OnClickListener {

    private PendingIntent pendingIntent;
    private Intent alarmIntent;
    private boolean started = false;
    BroadcastReceiver alarmReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        alarmIntent = new Intent(StartScreen.this, AlarmReceiver.class);
        Button startBtn = (Button) findViewById(R.id.startBtn);
        startBtn.setOnClickListener(this);
        alarmReceiver = new BroadcastReceiver() {
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
        };
        registerReceiver(alarmReceiver, new IntentFilter("com.example.ryan.arewethereyet.alarms"));
        alarmIntent.setAction("com.example.ryan.arewethereyet.alarms");
    }

    @Override
    public void onClick(View v) {
        EditText msgView = (EditText) findViewById(R.id.msgField);
        EditText targetView = (EditText) findViewById(R.id.targetPhone);
        String msg = msgView.getText().toString();
        String targetNum = targetView.getText().toString();

        if (msg != null && targetNum != null) {
            EditText timeText = (EditText) findViewById(R.id.intervalField);
            String time = timeText.getText().toString();
            int interval = Integer.parseInt(time) * 1000 * 60;
            Button btn = (Button) findViewById(R.id.startBtn);
            if (interval > 0) {
                if (!started) {
                    btn.setText("Stop");
                    started = true;
                    alarmIntent.putExtra("targetNum", targetNum);
                    alarmIntent.putExtra("message", msg);
                    start(interval);
                } else {
                    btn.setText("Start");
                    started = false;
                    cancel();
                }
            }
        }
    }

    public void start(int interval) {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        pendingIntent = PendingIntent.getBroadcast(StartScreen.this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
    }

    public void cancel() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (pendingIntent == null) {
            Intent intent = new Intent(StartScreen.this, AlarmReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(StartScreen.this, 0, intent, 0);
        }
        manager.cancel(pendingIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
