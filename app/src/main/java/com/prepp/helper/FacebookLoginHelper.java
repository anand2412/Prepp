package com.prepp.helper;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.prepp.activity.BranchSelectionActivity;
import com.prepp.activity.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * <p/>
 * Project: <b>e-Protege</b><br/>
 * Created by: Anand K. Rai on 24/9/15.<br/>
 * Email id: anand.rai@tothenew.com<br/>
 * <p/>
 */


public class FacebookLoginHelper {

    private FacebookHelper facebookHelper;
    private Activity activity;


    FacebookCallback<LoginResult> loginResultFacebookCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {

            GraphRequest request = GraphRequest.newMeRequest(
                    AccessToken.getCurrentAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject jsonObject, GraphResponse response) {
                            try {
                                ((LoginActivity)activity).isUserExists(true,jsonObject.getString("email"),jsonObject.getString("name"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email");
            request.setParameters(parameters);
            request.executeAsync();
        }


        @Override
        public void onCancel() {
            if (AccessToken.getCurrentAccessToken() != null)
                facebookHelper.logout();
        }

        @Override
        public void onError(FacebookException error) {
            if (AccessToken.getCurrentAccessToken() != null)
                facebookHelper.logout();
        }

    };


    public FacebookLoginHelper(Activity activity) {
        this.activity = activity;
        FacebookSdk.sdkInitialize(activity.getApplicationContext());
        facebookHelper = FacebookHelper.getInstance(activity);
        facebookHelper.addFacebookPermissions(Arrays.asList(new String[]{"public_profile, email"}));

    }

    public void login() {
        facebookHelper.login(loginResultFacebookCallback);
    }

    public void getKeyHash(String packageName) throws PackageManager.NameNotFoundException, NoSuchAlgorithmException {
        facebookHelper.getHashKey(packageName);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        facebookHelper.onActivityResult(requestCode, resultCode, data);
    }
}
