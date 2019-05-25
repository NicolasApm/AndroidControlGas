package presenter;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.media.MediaPlayer;
import android.os.Build;

import com.example.android.actionbarcompat.styled.R;

import androidx.core.app.NotificationCompat;

public class NotificationHelper extends ContextWrapper {
    public static final String channelID = "channelID";
    public static final String channelName = "Channel Name";


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
        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle("Notificacion Alarma!")
                .setContentText("Alarma de gas activada")
                .setSmallIcon(R.drawable.ic_launcher_background);
    }

    public void start() {
        mp.start();
    }
}