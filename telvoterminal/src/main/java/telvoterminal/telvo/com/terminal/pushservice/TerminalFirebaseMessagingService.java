package telvoterminal.telvo.com.terminal.pushservice;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.zip.ZipEntry;

import telvoterminal.telvo.com.terminal.R;
import telvoterminal.telvo.com.terminal.auth.LoginActivity;
import telvoterminal.telvo.com.terminal.mainscreen.MainActivity;
import telvoterminal.telvo.com.terminal.model.User;
import telvoterminal.telvo.com.terminal.model.transfer.TransferMoney;
import telvoterminal.telvo.com.terminal.preference.ApplicationPreferences;
import telvoterminal.telvo.com.terminal.utility.AppManager;
import telvoterminal.telvo.com.terminal.utility.Constant;

/**
 * Created by Invariant on 10/25/17.
 */

public class TerminalFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FirebaseMessaging";
    private ApplicationPreferences preferences;
    private User user;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        preferences = new ApplicationPreferences(this);
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            if (remoteMessage.getData().get("state").equals("ATM")) {
                Intent pushNotification = new Intent(Constant.ATM_PUSH);
                pushNotification.putExtra("message", remoteMessage.getData().get("message"));
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
            } else if (remoteMessage.getData().get("state").equals("ReceiveMoney")) {
                Intent pushNotification = new Intent(Constant.QRCODE_PUSH);
                pushNotification.putExtra("message", remoteMessage.getData().get("message"));
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
            } else if (remoteMessage.getData().get("state").equals("SysTransfer")) {
                setLocalBroadcast(remoteMessage.getData().get("message"));
            } else if (remoteMessage.getData().get("state").equals("NonSysTransfer")) {
                setLocalBroadcast(remoteMessage.getData().get("message"));
            }
        }
    }

    private void setLocalBroadcast(String message) {
        if (!TextUtils.isEmpty(message)) {
            TransferMoney transferMoney = (TransferMoney) AppManager.getClassObject(message, new TransferMoney());
            if (!preferences.getValue("USER").equals("")) {
                user = (User) AppManager.getClassObject(preferences.getValue("USER"), new User());
                user.setBalance(transferMoney.getBalance());
                preferences.setValue("USER", AppManager.getClassString(user));
                showNotification("Transfer Successful", transferMoney.getMessage());

                Intent pushNotification = new Intent(Constant.BALANCE_UPDATE_PUSH);
                pushNotification.putExtra("Balance", user.getBalance());
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
            }
        }
    }

    private void showNotification(String title, String message) {
        Log.d(TAG, "showNotification");

        int mNotificationId = 1000;

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this, "TransferNotificationChannel")
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle(title)
                        .setAutoCancel(true)
                        .setPriority(NotificationManagerCompat.IMPORTANCE_HIGH)
                        .setContentText(message);

        Intent mainIntent = new Intent(this, MainActivity.class);
        PendingIntent mainPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        mainIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        Intent loginIntent = new Intent(this, LoginActivity.class);
        PendingIntent loginPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        loginIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        boolean foreground = preferences.getValue(Constant.APPLICATION_FOREGROUND, false);
        boolean loggedIn = preferences.getValue(Constant.USER_LOGGED_IN, false);

        if (foreground && loggedIn) {
            mBuilder.setContentIntent(mainPendingIntent);
        } else {
            mBuilder.setContentIntent(loginPendingIntent);
        }

        if (new ApplicationPreferences(this).getValue("SOUND", true)) {
            mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        }

        if (new ApplicationPreferences(this).getValue("VIBRATION", true)) {
            mBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
        }

        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (mNotifyMgr != null) {
            mNotifyMgr.notify(mNotificationId, mBuilder.build());
        }
    }
}
