package com.prepp.helper;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p/>
 * Project: <b>e-Protege</b><br/>
 * Created by: Anand K. Rai on 24/9/15.<br/>
 * Email id: anand.rai@tothenew.com<br/>
 * <p/>
 */
public class FacebookHelper {

    private static FacebookHelper facebookHelper;
    private static Activity activity;
    private static CallbackManager callbackManager;
    private ArrayList<String> facebookPermissions = new ArrayList<>();

    private FacebookHelper() {
    }

    /**
     * This initializes <B>facebookSDK</B> and <B>current</B> instance.
     *
     * @param activity1
     * @return facebookHelper instance.
     */
    public static FacebookHelper getInstance(Activity activity1) {
        activity = activity1;
        if (facebookHelper == null) {
            FacebookSdk.sdkInitialize(activity.getApplicationContext());
            facebookHelper = new FacebookHelper();
        }
        if (callbackManager == null) {
            callbackManager = CallbackManager.Factory.create();
        }
        return facebookHelper;
    }


    /**
     * Facebook LoginButton implementation.
     * <p/>
     * <b>Facebook login</b>
     * <p/>
     * There are two ways to implement Facebook login on Android:<p/>
     * <b>LoginButton class -</b> Which provides a button you can add to your UI. It follows the current access token and can log people in and out.<br/>
     * <b>LoginManager class -</b> To initiate login without using a UI element.
     *
     * @param loginButton                 facebook LoginButton to register callback.
     * @param loginResultFacebookCallback listener for response after login. <p/>
     *                                    If login succeeds, the LoginResult parameter has the new AccessToken, and the most recently granted or declined permissions.
     *                                    <p/>Now you can call newGraphMeRequest() for facebook response in onSuccess() callback method.
     *                                    <p/>You can check if a person is already logged in by checking AccessToken.getCurrentAccessToken() and Profile.getCurrentProfile().
     *                                    <p/>
     *                                    You should check <i>network availability</i> before going to call login().
     * @throws NullPointerException If you are using fragment and fragment not register with LoginButton. Please call setFragment(loginButton, fragment) to add Fragment.
     */
    public void login(LoginButton loginButton, FacebookCallback<LoginResult> loginResultFacebookCallback) throws NullPointerException {

        if (callbackManager == null)
            callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions(getFacebookPermissions());
        loginButton.registerCallback(callbackManager, loginResultFacebookCallback);

    }

    /**
     * <b>Facebook login</b>
     * <p/>
     * There are two ways to implement Facebook login on Android:<p/>
     * <b>LoginButton class -</b> Which provides a button you can add to your UI. It follows the current access token and can log people in and out.<br/>
     * <b>LoginManager class -</b> To initiate login without using a UI element.
     *
     * @param loginResultFacebookCallback listener for response after login. <p/>
     *                                    If login succeeds, the LoginResult parameter has the new AccessToken, and the most recently granted or declined permissions.
     *                                    <p/>Now you can call newGraphMeRequest() for facebook response in onSuccess() callback method.
     *                                    <p/>You can check if a person is already logged in by checking AccessToken.getCurrentAccessToken() and Profile.getCurrentProfile().
     *                                    <p/>
     *                                    You should check <i>network availability</i> before going to call login().
     */
    public void login(FacebookCallback<LoginResult> loginResultFacebookCallback) {

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager, loginResultFacebookCallback);
        LoginManager.getInstance().logInWithReadPermissions(activity, getFacebookPermissions());
    }


    /**
     * Facebook graph response for current logged-in user.
     *
     * @param accessToken             get AccessToken from success() in FacebookCallback<LoginResult> callback. or if user already logged-in then by checking AccessToken.getCurrentAccessToken()
     * @param graphJSONObjectCallback get response data in this callback.
     */
    public void newGraphMeRequest(final AccessToken accessToken, Bundle parameters, GraphRequest.GraphJSONObjectCallback graphJSONObjectCallback) {

        GraphRequest request = GraphRequest.newMeRequest(
                accessToken, graphJSONObjectCallback);

        if (parameters != null)
            request.setParameters(parameters);
        request.executeAsync();

    }

    public void newGraphPathRequest(final AccessToken accessToken, String graphPath, Bundle parameters, GraphRequest.Callback graphCallback) {

        GraphRequest request = GraphRequest.newGraphPathRequest(
                accessToken, graphPath, graphCallback);

        if (parameters != null)
            request.setParameters(parameters);
        request.executeAsync();

    }


    /**
     * Facebook log out.
     */
    public void logout() {
        LoginManager loginManager = LoginManager.getInstance();
        loginManager.logOut();
    }

    /**
     * @return get current facebook permissions list.
     */
    public ArrayList<String> getFacebookPermissions() {
        return facebookPermissions;
    }

    /**
     * Add one more permission to previous permission list.
     *
     * @param facebookPermission facebook permission would be added in current permission list.
     */
    public void addFacebookPermissions(String facebookPermission) {
        this.facebookPermissions.add(facebookPermission);
    }

    /**
     * Add a list of more permissions to previous permission list.
     *
     * @param facebookPermissions a list of facebook permissions would be added in current permission list.
     */
    public void addFacebookPermissions(List<String> facebookPermissions) {
        this.facebookPermissions.addAll(facebookPermissions);
    }

    /**
     * Hash key must be set to your facebook application.
     *
     * @param appPackageName package name of current application.
     * @return hash key for current application.
     * @throws PackageManager.NameNotFoundException
     * @throws NoSuchAlgorithmException
     */
    public String getHashKey(String appPackageName) throws PackageManager.NameNotFoundException, NoSuchAlgorithmException {
        PackageInfo info = activity.getPackageManager().getPackageInfo(
                appPackageName,
                PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            return Base64.encodeToString(md.digest(), Base64.DEFAULT);
        }

        return null;
    }

    /**
     * Forward the login results to the callbackManager registered with loginButton or loginManager instance.
     * <p/>It must be called from onActivityResult() of your activity if you want to get callback from callbackManager.
     * <p/>
     * You can check request code by
     * <B>FacebookSdk.isFacebookRequestCode(</B>requestCode<B>)</B>,
     * If returns true then this method must be called.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void openFacebookUserProfile(Activity activity, String userId) {
        Intent intent;
        try {
            activity.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/" + userId));
        } catch (Exception e) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/" + userId));
        }
        activity.startActivity(intent);
    }
}
