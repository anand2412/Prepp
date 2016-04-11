package com.prepp.receiver;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;


import com.prepp.utils.AppConstants;
import com.prepp.R;
import com.prepp.activity.SplashActivity;
import com.prepp.utils.PreferencesUtility;

import java.util.Calendar;

/**
 * <p/>
 * Project: <b>Chuglee</b><br/>
 * Created by: Anand K. Rai on 11/9/15.<br/>
 * Email id: anand.rai@tothenew.com<br/>
 * <p/>
 */

public class QuizReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();

        showNotification(context);
        wl.release();
    }

    private void showNotification(Context context) {

        boolean isQuizPlayed=PreferencesUtility.getSharedPrefBooleanData(context, AppConstants.PREF_QUIZ_PLAYED);
        if(isQuizPlayed) {
            PreferencesUtility.setSharedPrefBooleanData(context, AppConstants.PREF_QUIZ_PLAYED,false);
            Intent intent = new Intent(context, SplashActivity.class);
            PendingIntent pIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent, 0);
            NotificationCompat.Builder n = new NotificationCompat.Builder(context)
                    .setContentTitle(context.getString(R.string.app_name))
                    .setContentText(context.getString(R.string.quiz_ready_msg))
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pIntent)
                    .setAutoCancel(true);


            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

            notificationManager.notify(0, n.build());
        }
    }

    public void SetAlarm(Context context) {

        Calendar calendaram = Calendar.getInstance();
        calendaram.setTimeInMillis(System.currentTimeMillis());
        calendaram.set(Calendar.HOUR_OF_DAY, 23);
        calendaram.set(Calendar.MINUTE, 59);
        calendaram.set(Calendar.SECOND, 58);

        Intent intent = new Intent(context, QuizReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager am = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendaram.getTimeInMillis(),AlarmManager.INTERVAL_DAY, sender);

    }

    public void CancelAlarm(Context context) {
        Intent intent = new Intent(context, QuizReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}
