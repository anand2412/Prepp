package com.prepp.fragment;

import android.app.ActivityManager;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.prepp.utils.AppConstants;
import com.prepp.activity.QuestionActivity;
import com.prepp.model.SignUpInfo;
import com.prepp.model.VerificationData;
import com.prepp.PreppApp;
import com.prepp.R;
import com.prepp.customviews.ProgressDialogHandler;
import com.prepp.helper.ProgressDialogParams;
import com.prepp.presenter.UserPresenter;
import com.prepp.services.AlarmService;
import com.prepp.utils.CommonUtility;
import com.prepp.utils.PreferencesUtility;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * <p/>
 * Project: <b>PREPP</b><br/>
 * Created by: Anand K. Rai on 13/1/16.<br/>
 * Email id: anand.rai@tothenew.com<br/>
 * <p/>
 */
public class QuizDashboardFragment extends Fragment {

    private EditText mEditTextVerification;
    private ImageView mImgQa;
    private TextView mTxtHeading;
    private UserPresenter mUserPresenter;
    private String mName;
    private Button mBtnSubmit;
    private ProgressDialogParams mParams;
    private ProgressDialogHandler mProgressHandler;
    private boolean isMailActive;
    private Button mBtnResendCode;

    public static QuizDashboardFragment newInstance(boolean isMailActive){
        QuizDashboardFragment quizDashboardFragment=new QuizDashboardFragment();
        Bundle bundle=new Bundle();
        bundle.putBoolean("MAIL_ACTIVE",isMailActive);
        quizDashboardFragment.setArguments(bundle);
        return quizDashboardFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.fragment_quiz_dashboard, container, false);
        initViews(rootView);
        return rootView;
    }

    private void initViews(View rootView) {

        mImgQa=(ImageView)rootView.findViewById(R.id.img_q_a);
        mEditTextVerification=(EditText)rootView.findViewById(R.id.edttxt_verification_code);
        mTxtHeading=(TextView)rootView.findViewById(R.id.txt_name);
        mBtnSubmit=(Button)rootView.findViewById(R.id.btn_start_quiz);
        mBtnResendCode=(Button)rootView.findViewById(R.id.btn_resend_code);
        mBtnSubmit.setOnClickListener(viewClickListener);
        mBtnResendCode.setOnClickListener(viewClickListener);
        isMailActive=getArguments().getBoolean("MAIL_ACTIVE");
        mName=PreferencesUtility.getSharedPrefStringData(getActivity(),AppConstants.PREF_USER_NAME);
        if(!getArguments().getBoolean("MAIL_ACTIVE")) {
            mTxtHeading.setText(R.string.account_verify_msg);
            mEditTextVerification.setVisibility(View.VISIBLE);
            mImgQa.setVisibility(View.GONE);
            mBtnSubmit.setText(R.string.submit);
        }else{
            mTxtHeading.setText(mName + getString(R.string.quiz_today_ready));
            mEditTextVerification.setVisibility(View.GONE);
            mImgQa.setVisibility(View.VISIBLE);
            mBtnSubmit.setText(R.string.start_quiz);
        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    View.OnClickListener viewClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()){
                case R.id.btn_start_quiz:
                    if(!isMailActive){
                        verifyAccount();
                    }else{
                        startAlarmService();
                        Intent intent=new Intent(getActivity(), QuestionActivity.class);
                        startActivity(intent);
                    }
                    break;

                case R.id.btn_resend_code:

                    break;
            }
        }
    };

    private void startAlarmService() {

        PreferencesUtility.setSharedPrefBooleanData(getActivity(),AppConstants.PREF_QUIZ_PLAYED,true);
        if(!isMyServiceRunning(AlarmService.class)) {
            getActivity().startService(new Intent(getActivity(), AlarmService.class));
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager)getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void verifyAccount() {

        if (!TextUtils.isEmpty(mEditTextVerification.getText().toString())) {

            mParams = new ProgressDialogParams();
            mParams.setMessage(getString(R.string.please_wait));
            mParams.setCancellable(false);
            mProgressHandler = new ProgressDialogHandler(getActivity());
            mProgressHandler.showProgressDialog(mParams);
            mUserPresenter = ((PreppApp) getActivity().getApplication()).getPresenterFactory().getUserPresenter();
            String email = PreferencesUtility.getSharedPrefStringData(getActivity(), AppConstants.PREF_EMAIL);
            int id = PreferencesUtility.getSharedPrefIntData(getActivity(), AppConstants.PREF_USR_ID);

            mUserPresenter.verifyEmailAccount(String.valueOf(id), new VerificationData(email, mEditTextVerification.getText().toString(), "email"),
                    new Callback<SignUpInfo>() {
                          @Override
                        public void success(SignUpInfo signUpInfo, Response response) {
                            mProgressHandler.dismissProgressDialog();
                                if(signUpInfo.getStatus()){
                                    isMailActive=true;
                                    PreferencesUtility.setSharedPrefBooleanData(getActivity(),
                                            AppConstants.PREF_IS_ACTIVE, signUpInfo.getSignUpData().isActive());
                                    mTxtHeading.setText(mName + getString(R.string.quiz_today_ready));
                                    mEditTextVerification.setVisibility(View.GONE);
                                    mImgQa.setVisibility(View.VISIBLE);
                                    mBtnSubmit.setText(R.string.start_quiz);
                                    PreferencesUtility.setSharedPrefStringData(getActivity(),AppConstants.PREF_ACCESS_TOKEN,signUpInfo.getSignUpData().getAccessToken());
                                    CommonUtility.showErrorMessage(getActivity(), signUpInfo.getResponseMessageList());
                                }else{
                                    CommonUtility.showErrorMessage(getActivity(),signUpInfo.getResponseMessageList());
                                }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            mProgressHandler.dismissProgressDialog();
                            CommonUtility.showRetrofitErrorMessage(error);
                        }
                    });
        } else {
            CommonUtility.showToast(getActivity(),getString(R.string.enter_verification_code_alert));
        }
    }
}
