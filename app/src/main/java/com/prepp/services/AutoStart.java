package com.prepp.services;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * <p/>
 * Project: <b>Chuglee</b><br/>
 * Created by: Anand K. Rai on 11/9/15.<br/>
 * Email id: anand.rai@tothenew.com<br/>
 * <p/>
 */
public class AutoStart extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
        {

            ActivityManager manager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (AlarmService.class.getName().equals(service.service.getClassName())) {
                    //your service is running

                }else{
                    context.startService(new Intent(context, AlarmService.class));
                }
            }

        }
    }
}
