package presenter;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.Log;

import com.example.android.actionbarcompat.styled.R;

import androidx.core.app.NotificationCompat;
import devices.BTCallback;
import devices.BTUtil;

public class NotificationHelper extends ContextWrapper implements BTCallback {
    public static final String channelID = "channelID";
    public static final String channelName = "Channel Name";
    private String btAdd = "00:18:91:D7:DF:E4";
    private BTUtil btUtil;


    private NotificationManager mManager;
    private MediaPlayer mp;


    public NotificationHelper(Context base, MediaPlayer mp) {
        super(base);
        this.mp = mp;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }


    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);

        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return mManager;
    }

    public NotificationCompat.Builder getChannelNotification() {
        start();
       // process();
        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle("Notificacion Alarma!")
                .setContentText("Programacion apagado paso de gas")
                .setSmallIcon(R.drawable.ic_launcher_background);
    }

    public void start() {
        mp.start();
    }

    public void process() {

        btUtil = new BTUtil();
        try {
            btUtil.connect(btAdd, this);
            btUtil.write("1");
        } catch (Exception e) {
            Log.e("Falla Connected", "Error Conexion Bluetooth ", e);
        }
        btUtil.close();
    }

    @Override
    public void onNext(String data, boolean flag, String Trama) {

    }

    @Override
    public void onError(Exception e) {

    }
}