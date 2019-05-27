package com.example.android.actionbarcompat.styled;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import devices.BTCallback;
import devices.BTUtil;
import presenter.AlertReceiver;
import presenter.TimePickerFragment;

public class AlarmActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, BTCallback {
    private TextView mTextView;
    private String btAdd = "00:18:91:D7:DF:E4";
    private String Data;
    private BTUtil btUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //presenter = new AlertReceiver(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Data = "O";
        btUtil = new BTUtil();
        process(Data);
        mTextView = findViewById(R.id.textView);
        Button buttonTimePicker = findViewById(R.id.button_timepicker);
        buttonTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });
        Button buttonCancelAlarm = findViewById(R.id.button_cancel);
        buttonCancelAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAlarm();
            }
        });

        Button ButtonOn = findViewById(R.id.button_On);
        ButtonOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Data = "1";
                Write(Data);
            }
        });
        Button ButtonOff = findViewById(R.id.button_Off);
        ButtonOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Data = "0";
                Write(Data);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu from menu resource (res/menu/main)
        getMenuInflater().inflate(R.menu.main2, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        switch (itemThatWasClickedId) {
            case R.id.menu_settings:
                Toast.makeText(getBaseContext(), "3", Toast.LENGTH_SHORT).show();
                Intent j = new Intent(this, MainActivity.class);
                startActivity(j);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        updateTimeText(c);
        startAlarm(c);
    }

    private void updateTimeText(Calendar c) {
        String timeText = "Programar Alarma: ";
        timeText += DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
        mTextView.setText(timeText);
    }

    private void startAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        alarmManager.cancel(pendingIntent);
        mTextView.setText("Alarma Cancelada");

    }

    @Override
    public void onNext(String data, boolean flag, String Trama) {

    }

    @Override
    public void onError(Exception e) {

    }

    public void process(String data) {

        try {
            btUtil.connect(btAdd, this);
            Write(data);
        } catch (Exception e) {
            Log.e("Falla Connected", "Error Conexion Bluetooth ", e);
        }
    }

    public void Write(String data) {
        btUtil.write(data);
    }
}

