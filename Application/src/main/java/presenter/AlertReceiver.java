package presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

import com.example.android.actionbarcompat.styled.R;

import androidx.core.app.NotificationCompat;

public class AlertReceiver extends BroadcastReceiver {

    MediaPlayer mp;

    @Override
    public void onReceive(Context context, Intent intent) {
        mp = MediaPlayer.create(context, R.raw.humo);
        NotificationHelper notificationHelper = new NotificationHelper(context,mp);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
        notificationHelper.getManager().notify(1, nb.build());
        }
}
