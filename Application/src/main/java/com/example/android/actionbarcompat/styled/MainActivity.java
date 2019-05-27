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
import android.widget.ImageView;
import android.widget.Toast;

import UtilNot.GlobalNotificationBuilder;
import UtilNot.NotificationUtil;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import devices.BTCallback;
import devices.BTUtil;
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
public class MainActivity extends AppCompatActivity implements BTCallback {

    // private TextView mTextView;

    NotificationCompat.Builder notificacion;
    MediaPlayer mp;
    private static final String TAG = MainActivity.class.getName();
    public static final int NOTIF_ID = 1001;
    public static final String NOTIF_MESSAGE = "NOTIF_MESSAGE";
    public static final int NOTIFICATION_ID = 888;
    private NotificationManagerCompat mNotificationManagerCompat;
    private String btAdd = "00:18:91:D7:DF:E4";
    private BTUtil btUtil;
    private String data="";
    private String DataS="";
    private  int dataRctp=0;
    private WaveLoadingView waveLoadingView;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_main);

        image = findViewById(R.id.imgoff);
    }

    @Override
    public void onResume() {
        super.onResume();

        DataS="O";

        mNotificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());

        notificacion = new NotificationCompat.Builder(this);
        notificacion.setAutoCancel(true);

        btUtil = new BTUtil();

        waveLoadingView = findViewById(R.id.wave);

        process(DataS);
    }

    @Override
    public void onPause() {
        super.onPause();
        try { // Cuando se sale de la aplicación esta parte permite
            // que no se deje abierto el socket
            btUtil.close();
        } catch (Exception e2) {

        }
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
                btUtil.close();
                DataS="O";
                //process(DataS);
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

    public void process(String dataS) {

        try {
            btUtil.connect(btAdd, this);
            Write(dataS);
        } catch (Exception e) {
            Log.e("Falla Connected", "Error Conexion Bluetooth ", e);
        }
    }

    @Override
    public void onNext(String data, boolean flag ,String Trama) {
        this.data=data;
        /*try {
            btUtil.close();
        } catch (Exception ex) {
            Log.e("Close Socket", "Error cerrando", ex);
        }*/
        Log.d("DataRecept", data+"//"+Trama);
        try {

            //String[] btns = data.split(",");

            dataRctp = Integer.parseInt(data);
            try {
                //dataRctp=dataRctp;
                waveLoadingView.setProgressValue(dataRctp);
                mp = MediaPlayer.create(getBaseContext(), R.raw.humo);

                String title = String.valueOf(dataRctp) + "%";
                waveLoadingView.setBottomTitle("");
                waveLoadingView.setCenterTitle("");
                waveLoadingView.setTopTitle("");

                if (dataRctp < 50) {
                    waveLoadingView.setBottomTitle(title);
                } else if (dataRctp == 50) {
                    waveLoadingView.setCenterTitle(title);
                } else {
                    waveLoadingView.setTopTitle(title);
                    btUtil.write("0");
                    generateBigPictureStyleNotification();
                    mp.start();
                }
            }catch (Exception e){
                Log.e("eroor", String.valueOf(e));
            }
            if (flag){
                image.setImageResource(R.drawable.imagesonoff);
            }
            else {image.setImageResource(R.drawable.off);}

        } catch (Exception e) {

            // view.Msg("Intenta de nuevo");
        }

    }

    @Override
    public void onError(Exception e) {

    }

    private void Write (String data){
        btUtil.write(data);
    }
}


