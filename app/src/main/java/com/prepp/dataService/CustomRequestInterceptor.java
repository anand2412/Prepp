package com.prepp.dataService;

import android.content.Context;

import com.prepp.utils.AppConstants;
import com.prepp.PreppApp;
import com.prepp.utils.PreferencesUtility;

import retrofit.RequestInterceptor;


/**
 * Created by Vaibhav Chahal on 7/9/15.
 */
public class CustomRequestInterceptor implements RequestInterceptor {
    Context context= PreppApp.getAppContext().getApplicationContext();
      @Override
        public void intercept(RequestFacade request) {

          request.addHeader("X-Auth-Token", PreferencesUtility.getSharedPrefStringData(context, AppConstants.X_AUTH_TOKEN));
      }
    }

