package com.prepp.activity;

import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.FacebookSdk;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.prepp.PreppApp;
import com.prepp.R;
import com.prepp.model.ResponseMessage;
import com.prepp.model.SignUpInfo;
import com.prepp.model.UserData;
import com.prepp.presenter.UserPresenter;
import com.prepp.utils.AppConstants;
import com.prepp.utils.PreferencesUtility;
import com.prepp.utils.ValidateInputFields;
import com.prepp.customviews.ProgressDialogHandler;
import com.prepp.helper.FacebookLoginHelper;
import com.prepp.helper.ProgressDialogParams;
import com.prepp.utils.CommonUtility;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private static final int RC_SIGN_IN = 200;
    private static final String TAG = "LoginActivity";
    private AlertDialog alertDialog;
    private EditText mEdtTxtEmail,mEdtTxtName;
    private GoogleApiClient mGoogleApiClient;
    private FacebookLoginHelper facebookLoginHelper;
    private ProgressDialogHandler mProgressDialogHandler;
    private ProgressDialogParams mParams;
    private boolean mIsResolving = false;
    private boolean mShouldResolve = false;
    private UserPresenter mUserPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mUserPresenter=((PreppApp)getApplication()).getPresenterFactory().getUserPresenter();
        mProgressDialogHandler=new ProgressDialogHandler(this);
        mParams=new ProgressDialogParams();
        mParams.setMessage("Please wait");
        mParams.setCancellable(false);


        Button btnSignUpEmail=(Button)findViewById(R.id.btn_email);
        btnSignUpEmail.setOnClickListener(viewClickListener);

        SignInButton btnSignInGoogle=(SignInButton)findViewById(R.id.btn_google);
        btnSignInGoogle.setOnClickListener(viewClickListener);
        btnSignInGoogle.setScopes(gso.getScopeArray());

        Button btnSignInFacebook=(Button)findViewById(R.id.btn_facebook);
        btnSignInFacebook.setOnClickListener(viewClickListener);
        facebookLoginHelper=new FacebookLoginHelper(this);
        try {
            facebookLoginHelper.getKeyHash(getPackageName());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


    View.OnClickListener viewClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()){

                case R.id.btn_email:
                    showSignUpDialog();
                   break;

                case R.id.btn_signup:
                    if(validateEmptyField()) {
                        if(validateCorrectField()) {
                            /*Intent intent = new Intent(LoginActivity.this, BranchSelectionActivity.class);
                            intent.putExtra("NAME", mEdtTxtName.getText().toString().trim());
                            intent.putExtra("EMAIL", mEdtTxtEmail.getText().toString().trim());
                            intent.putExtra("SOCIAL",false);
                            startActivity(intent);
                            */
                            Log.e("hgfghf","hfhgf");
                            isUserExists(false, mEdtTxtEmail.getText().toString().trim(),mEdtTxtName.getText().toString().trim());
                        }
                    }
                    break;

                case R.id.btn_google:
                    signInWithGoogle();
                    break;

                case R.id.btn_facebook:
                    signInWithFacebook();
                    break;
            }
        }
    };

    private void signInWithFacebook() {
        facebookLoginHelper.login();

    }

    private void signInWithGoogle() {
        mProgressDialogHandler.showProgressDialog(mParams);

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    private boolean validateCorrectField() {

        if (!ValidateInputFields.isEmailValid(mEdtTxtEmail.getText().toString())) {
            CommonUtility.showToast(this, getString(R.string.validation_mail));
            return false;
        }

        return true;
    }

    private boolean validateEmptyField() {

        if (TextUtils.isEmpty(mEdtTxtName.getText().toString())) {
            CommonUtility.showToast(this, getString(R.string.validation_empty_name));
            return false;
        } else if (TextUtils.isEmpty(mEdtTxtEmail.getText().toString())) {
            CommonUtility.showToast(this, getString(R.string.validation_empty_email));
            return false;
        }

        return true;
    }

    private void showSignUpDialog() {

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        // inflate the custom dialog view
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_signup, null);
        alertDialog=alertBuilder.create();
        alertDialog.setView(dialogView);
        alertDialog.setCancelable(true);
        alertDialog.show();
        alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, height - 400);
        findViewsById(dialogView);


    }

    private void findViewsById(View dialogView) {

        mEdtTxtName=(EditText)dialogView.findViewById(R.id.edt_txt_name);
        mEdtTxtEmail=(EditText)dialogView.findViewById(R.id.edt_txt_email);
       // mEdtTxtPassword=(EditText)dialogView.findViewById(R.id.edt_txt_password);

        Button btnSignUp=(Button)dialogView.findViewById(R.id.btn_signup);
        btnSignUp.setOnClickListener(viewClickListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            mProgressDialogHandler.dismissProgressDialog();
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }else if (FacebookSdk.isFacebookRequestCode(requestCode)) {
            facebookLoginHelper.onActivityResult(requestCode, resultCode, data);
        }else{

        }

    }

    private void handleSignInResult(GoogleSignInResult result) {

        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            isUserExists(true,acct.getEmail(),acct.getDisplayName());

        } else {
            // Signed out, show unauthenticated UI.

        }

    }

    public void isUserExists(final boolean socialFlag, final String email, final String displayName) {

        mProgressDialogHandler.showProgressDialog(mParams);
        mUserPresenter.checkUserValid
                (new UserData(email),
                        new Callback<SignUpInfo>() {
                            @Override
                            public void success(SignUpInfo signUpInfo, Response response) {
                                mProgressDialogHandler.dismissProgressDialog();
                                if (signUpInfo.getStatus()) {
                                    goToBranchSelection(socialFlag,email,displayName,signUpInfo.getSignUpData().isValid());
                                } else {
                                    CommonUtility.showErrorMessage(LoginActivity.this, signUpInfo.getResponseMessageList());
                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                mProgressDialogHandler.dismissProgressDialog();
                                CommonUtility.showRetrofitErrorMessage(error);
                            }
                        });

    }


    @Override
    public void onConnected(Bundle bundle) {
        mProgressDialogHandler.dismissProgressDialog();
        mShouldResolve = false;
        mIsResolving = false;
    }

    @Override
    public void onConnectionSuspended(int i) {
        mProgressDialogHandler.dismissProgressDialog();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (!mIsResolving && mShouldResolve) {
            if (connectionResult.hasResolution()) {
                try {
                    connectionResult.startResolutionForResult(this, RC_SIGN_IN);
                    mIsResolving = true;
                } catch (IntentSender.SendIntentException e) {
                    Log.e("", "Could not resolve ConnectionResult.", e);
                    mIsResolving = false;
                    mProgressDialogHandler.dismissProgressDialog();
                    mGoogleApiClient.connect();
                }
            }
        } else {
            mProgressDialogHandler.dismissProgressDialog();
        }

    }

    public void goToBranchSelection(boolean socialFlag,String email, String displayName, boolean userValid) {
            if(userValid) {
                registerAgain(email);
            }else {
                Intent intent = new Intent(LoginActivity.this, BranchSelectionActivity.class);
                intent.putExtra("NAME", displayName);
                intent.putExtra("EMAIL", email);
                intent.putExtra("SOCIAL",socialFlag);
                startActivity(intent);
            }
    }

    private void registerAgain(String email) {

        mProgressDialogHandler.showProgressDialog(mParams);

        mUserPresenter.registerData
                (new UserData("IT",email, "email", "name", null,true),
                        new Callback<SignUpInfo>() {
                            @Override
                            public void success(SignUpInfo signUpInfo, Response response) {
                                mProgressDialogHandler.dismissProgressDialog();
                                if (signUpInfo.getStatus()) {
                                    setNavigationAsResponse(signUpInfo);
                                } else {
                                    CommonUtility.showErrorMessage(LoginActivity.this, signUpInfo.getResponseMessageList());
                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                mProgressDialogHandler.dismissProgressDialog();
                                CommonUtility.showRetrofitErrorMessage(error);
                            }
                        });
    }

    private void setNavigationAsResponse(SignUpInfo signUpInfo) {

        PreferencesUtility.setSharedPrefStringData(LoginActivity.this,
                AppConstants.PREF_EMAIL, signUpInfo.getSignUpData().getEmail());

        PreferencesUtility.setSharedPrefIntData(LoginActivity.this,
                AppConstants.PREF_USR_ID, signUpInfo.getSignUpData().getId());

        PreferencesUtility.setSharedPrefStringData(LoginActivity.this,
                AppConstants.PREF_USER_NAME,getIntent().getStringExtra("NAME"));

        if(signUpInfo.getSignUpData().isMobileNumberVerified()){
            PreferencesUtility.setSharedPrefBooleanData(LoginActivity.this, AppConstants.PREF_MOBILE_VERIFIED, true);
        }else{
            PreferencesUtility.setSharedPrefBooleanData(LoginActivity.this,AppConstants.PREF_MOBILE_VERIFIED,false);
        }

        if (signUpInfo.getSignUpData().isActive()) {
            PreferencesUtility.setSharedPrefBooleanData(LoginActivity.this,
                    AppConstants.PREF_IS_ACTIVE, signUpInfo.getSignUpData().isActive());

            PreferencesUtility.setSharedPrefStringData(LoginActivity.this,
                    AppConstants.PREF_ACCESS_TOKEN, signUpInfo.getSignUpData().getAccessToken());

        } else {
            CommonUtility.showToast(this,
                    signUpInfo.getResponseMessageList().get(0).getMessage());
        }

        Intent intent = new Intent(LoginActivity.this, UserDashboardActivity.class);
        intent.putExtra("MAIL_ACTIVE",signUpInfo.getSignUpData().isActive());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        // EventBus.getDefault().postSticky(signUpInfo);
    }
}
