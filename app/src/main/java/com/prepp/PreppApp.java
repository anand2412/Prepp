package com.prepp;

import android.app.Application;
import android.content.Context;

import com.prepp.dataService.DataServiceFactory;
import com.prepp.presenter.PresenterFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p/>
 * Project: <b>PREPP</b><br/>
 * Created by: Anand K. Rai on 9/1/16.<br/>
 * Email id: anand.rai@tothenew.com<br/>
 * <p/>
 */
public class PreppApp extends Application {

    private final static List<AppReadyCallBack> sCallbacks = new ArrayList<AppReadyCallBack>();
    public static boolean sAppReady = false;
    private static Context sAppContext;
    private static DataServiceFactory mDataServiceFactory;
    private static PresenterFactory mPresenterFactory;
    private static ExecutorService mExecutor;

    public static Context getAppContext() {
        return sAppContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //mExecutor = Executors.newFixedThreadPool(3);
        //Fabric.with(this, new Crashlytics());
        sAppContext = PreppApp.this;
        new Thread(new Runnable() {
            @Override
            public void run() {
                mDataServiceFactory = DataServiceFactory.getInstance();
                mPresenterFactory = PresenterFactory.getInstance(mDataServiceFactory);

                fireCallbacks();

            }
        }).start();
    }

    public Executor getExecutor(){
        return mExecutor;
    }

    public PresenterFactory getPresenterFactory() {
        return mPresenterFactory;
    }

    private void fireCallbacks() {
        synchronized (sCallbacks) {
            for (AppReadyCallBack callback : sCallbacks) {
                callback.onReady();
            }
            sAppReady = true;
            sCallbacks.clear();
        }
    }

    public void attachCallback(AppReadyCallBack callback) {
        synchronized (sCallbacks) {
            if (sAppReady) {
                callback.onReady();
            } else {
                sCallbacks.add(callback);
            }
        }
    }

    public interface AppReadyCallBack {
        void onReady();
    }
}
