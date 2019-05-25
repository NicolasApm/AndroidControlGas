/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.actionbarcompat.styled;

import android.app.Notification;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.Toast;

import UtilNot.GlobalNotificationBuilder;
import UtilNot.NotificationUtil;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import me.itangqi.waveloadingview.WaveLoadingView;
import presenter.MockDatabase;

/**
 * This sample shows you how to use ActionBarCompat with a customized theme. It utilizes a split
 * action bar when running on a device with a narrow display, and show three tabs.
 * <p>
 * This Activity extends from {@link AppCompatActivity}, which provides all of the function
 * necessary to display a compatible Action Bar on devices running Android v2.1+.
 * <p>
 * The interesting bits of this sample start in the theme files
 * ('res/values/styles.xml' and 'res/values-v14</styles.xml').
 * <p>
 * Many of the drawables used in this sample were generated with the
 * 'Android Action Bar Style Generator': http://jgilfelt.github.io/android-actionbarstylegenerator
 */
public class MainActivity extends AppCompatActivity {

    // private TextView mTextView;

    NotificationCompat.Builder notificacion;
    MediaPlayer mp;
    private static final String TAG = MainActivity.class.getName();
    public static final int NOTIF_ID = 1001;
    public static final String NOTIF_MESSAGE = "NOTIF_MESSAGE";
    public static final int NOTIFICATION_ID = 888;
    private NotificationManagerCompat mNotificationManagerCompat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_main);

        SeekBar seekBar;
        final WaveLoadingView waveLoadingView;
        mNotificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());

        notificacion = new NotificationCompat.Builder(this);
        notificacion.setAutoCancel(true);

        seekBar = findViewById(R.id.seekbar);
        waveLoadingView = findViewById(R.id.wave);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // progress=20;
                waveLoadingView.setProgressValue(progress);
                mp = MediaPlayer.create(getBaseContext(), R.raw.humo);

                String title = String.valueOf(progress) + "%";
                waveLoadingView.setBottomTitle("");
                waveLoadingView.setCenterTitle("");
                waveLoadingView.setTopTitle("");

                if (progress < 50) {
                    waveLoadingView.setBottomTitle(title);
                } else if (progress == 50) {
                    waveLoadingView.setCenterTitle(title);
                } else {
                    waveLoadingView.setTopTitle(title);
                    generateBigPictureStyleNotification();
                    mp.start();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu from menu resource (res/menu/main)
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();

        switch (itemThatWasClickedId) {
            case R.id.menu_refresh:
                Toast.makeText(getBaseContext(), "1", Toast.LENGTH_SHORT).show();
                Intent j = new Intent(this, AlarmActivity.class);
                startActivity(j);
                break;
            case R.id.menu_settings:
                Toast.makeText(getBaseContext(), "2", Toast.LENGTH_SHORT).show();
                Intent k = new Intent(this, DispositivosBT.class);
                startActivity(k);
                break;

            case R.id.menu_settings2:
                Toast.makeText(getBaseContext(), "3", Toast.LENGTH_SHORT).show();


                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void generateBigPictureStyleNotification() {

        Log.d(TAG, "generateBigPictureStyleNotification()");

        MockDatabase.BigPictureStyleSocialAppData bigPictureStyleSocialAppData =
                MockDatabase.getBigPictureStyleData();

        String notificationChannelId =
                NotificationUtil.createNotificationChannel(this, bigPictureStyleSocialAppData);

        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle()
                .bigPicture(BitmapFactory.decodeResource(getResources(), R.drawable.earth))
                .setBigContentTitle("Alerta!")
                .setSummaryText("Nivel de gas limite");

        NotificationCompat.Builder notificationCompatBuilder =
                new NotificationCompat.Builder(getApplicationContext(), notificationChannelId);

        GlobalNotificationBuilder.setNotificationCompatBuilderInstance(notificationCompatBuilder);
        notificationCompatBuilder
                .setStyle(bigPictureStyle)
                .setContentTitle("Alerta!")
                .setContentText("Se ha cerrado el paso de gas")
                .setSmallIcon(R.drawable.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(
                        getResources(),
                        R.drawable.alert_circle))
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                .setSubText(Integer.toString(1))
                // .addAction(replyAction)
                .setCategory(Notification.CATEGORY_SOCIAL);

        Notification notification = notificationCompatBuilder.build();
        mNotificationManagerCompat.notify(NOTIFICATION_ID, notification);
    }

}


