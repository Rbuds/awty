package com.example.ryan.arewethereyet;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class StartScreen extends ActionBarActivity implements View.OnClickListener {

    private PendingIntent alarmIntent = null;
    BroadcastReceiver alarmReceiver;
    AlarmManager am;
    private boolean started = false;
    public String areaCode = "";
    public String prefix = "";
    public String suffix = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        Button startBtn = (Button) findViewById(R.id.startBtn);
        startBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        EditText msgView = (EditText) findViewById(R.id.msgField);
        EditText targetView = (EditText) findViewById(R.id.targetPhone);
        String msg = msgView.getText().toString();
        final String targetNum = targetView.getText().toString();

        if (msg != null && targetNum != null) {
            EditText timeText = (EditText) findViewById(R.id.intervalField);
            String time = timeText.getText().toString();
            int interval = Integer.parseInt(time);
            Button btn = (Button) findViewById(R.id.startBtn);
            if (interval > 0) {
                registerReceiver(alarmReceiver, new IntentFilter("com.example.ryan.arewethereyet"));
                am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent i = new Intent();
                alarmIntent = PendingIntent.getBroadcast(this, 0, i, 0);
                if (!started) {
                    btn.setText("Stop");
                    started = true;
                    areaCode = targetNum.substring(0, 3);
                    prefix = targetNum.substring(3, 6);
                    suffix = targetNum.substring(6, 10);
                    start(areaCode, prefix, suffix, interval);
                } else {
                    btn.setText("Start");
                    started = false;
                    cancel();
                }
            }
        }
    }

    public void start(String areaCode, String prefix, String suffix, int interval) {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, alarmIntent);
        Toast.makeText(this, "(" + areaCode + ") " + prefix + "-" + suffix + ": Are we there yet?", Toast.LENGTH_SHORT).show();
    }

    public void cancel() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.cancel(alarmIntent);
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
