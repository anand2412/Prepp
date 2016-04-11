package com.prepp.dataService;

import android.content.Context;

import com.prepp.Interfaces.IUserHttpServiceClient;
import com.prepp.PreppApp;
import com.prepp.utils.ApiDetails;
import com.prepp.utils.AppConstants;
import com.prepp.utils.PreferencesUtility;
import com.squareup.okhttp.OkHttpClient;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.client.OkClient;


/**
 * Created by Mohit Sharma on 26/8/15.
 */
public class DataServiceFactory {

    private static DataServiceFactory sServiceFactory;
    private static RestAdapter.Builder retrofit;
    private UserDataService mUserDataService;


    public static DataServiceFactory getInstance() {
        if (sServiceFactory == null) {
            retrofit = getRestAdapter();
            sServiceFactory = new DataServiceFactory();
      }
        return sServiceFactory;
    }

    private <S> S getClient(Class<S> serviceClass) {
        RestAdapter adapter = retrofit.build();
        return adapter.create(serviceClass);
    }

    private static RestAdapter.Builder getRestAdapter(){

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(1, TimeUnit.MINUTES);
        okHttpClient.setConnectTimeout(1, TimeUnit.MINUTES);
        okHttpClient.setWriteTimeout(1, TimeUnit.MINUTES);

               RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(ApiDetails.BASE_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setRequestInterceptor(new CustomRequestInterceptor())
                .setClient(new OkClient(okHttpClient));
        return builder;
    }

    private DataServiceFactory(){
        mUserDataService=new UserDataService(getClient(IUserHttpServiceClient.class));

    }

    public UserDataService getUserDataService(){
        return mUserDataService;
    }


}
