package com.prepp.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.prepp.receiver.QuizReceiver;


public class AlarmService extends Service {
    public AlarmService() {
    }

    QuizReceiver quizReceiver = new QuizReceiver();

    public void onCreate() {
        super.onCreate();
        quizReceiver.SetAlarm(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
}
