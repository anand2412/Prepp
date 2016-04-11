package com.prepp.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.prepp.utils.AppConstants;
import com.prepp.PreppApp;
import com.prepp.R;
import com.prepp.utils.ValidateInputFields;
import com.prepp.customviews.ProgressDialogHandler;
import com.prepp.helper.ProgressDialogParams;
import com.prepp.model.TopResults;
import com.prepp.model.VerificationCode;
import com.prepp.model.VerifyMobileNumber;
import com.prepp.model.RecordSave;
import com.prepp.presenter.UserPresenter;
import com.prepp.utils.CommonUtility;
import com.prepp.utils.PreferencesUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
public class LeaderBoardDashboardFragment extends Fragment implements View.OnClickListener {

    private LinearLayout mLytMobileVerification,mLytTopResults;
    private boolean isMobileVerified;
    private EditText mEdtCode;
    private EditText mEdtNumber;
    private Button mBtnSubmit,mBtnVerify;
    private CountDownTimer timeCounter;
    private String mMobileNumber;
    private ListView mLvWeekTopResult, mLvMonthResult;
    private UserPresenter mUserPresenter;
    private String mEmail;
    private String mAccessToken;
    private String mUserId;
    private ProgressDialogParams mParams;
    private ProgressDialogHandler mProgressHandler;
    private ProgressBar mPbMonthly;
    private ProgressBar mPbWeekly;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.fragment_leaderboard, container, false);
        initViews(rootView);
        return rootView;
    }

    private void initViews(View rootView) {

        mUserPresenter = ((PreppApp)getActivity().getApplication()).getPresenterFactory().getUserPresenter();
        mLytMobileVerification=(LinearLayout)rootView.findViewById(R.id.lyt_mobile_verification);
        mLytTopResults=(LinearLayout)rootView.findViewById(R.id.lyt_top_result);
        mEdtNumber=(EditText)rootView.findViewById(R.id.edt_number);
        mEdtCode=(EditText)rootView.findViewById(R.id.edttxt_verification_code);
        mBtnSubmit=(Button)rootView.findViewById(R.id.btn_submit);
        mBtnVerify=(Button)rootView.findViewById(R.id.btn_verify);
        mLvWeekTopResult=(ListView)rootView.findViewById(R.id.lv_name_list);
        mLvMonthResult=(ListView)rootView.findViewById(R.id.lv_monthly_name_list);
        mPbWeekly=(ProgressBar)rootView.findViewById(R.id.progressBar_weekly);
        mPbMonthly=(ProgressBar)rootView.findViewById(R.id.progressBar_monthly);
        mBtnSubmit.setOnClickListener(this);
        mBtnVerify.setOnClickListener(this);

        mEmail=PreferencesUtility.getSharedPrefStringData(getActivity(),AppConstants.PREF_EMAIL);
        mUserId= String.valueOf(PreferencesUtility.getSharedPrefIntData(getActivity(), AppConstants.PREF_USR_ID));
        mAccessToken=PreferencesUtility.getSharedPrefStringData(getActivity(),AppConstants.PREF_ACCESS_TOKEN);
        isMobileVerified=PreferencesUtility.getSharedPrefBooleanData(getActivity(), AppConstants.PREF_MOBILE_VERIFIED);

        if(!isMobileVerified){
            mLytTopResults.setVisibility(View.GONE);
            mLytMobileVerification.setVisibility(View.VISIBLE);

        }else{
            getTopResults();
            mLytTopResults.setVisibility(View.VISIBLE);
            mLytMobileVerification.setVisibility(View.GONE);
        }
    }

    private void getTopResults() {

        mPbWeekly.setVisibility(View.VISIBLE);
        mPbMonthly.setVisibility(View.VISIBLE);
        mLvMonthResult.setVisibility(View.GONE);
        mLvWeekTopResult.setVisibility(View.GONE);
        getWeeklyResult();
        getMonthlyResult();

    }

    private void getWeeklyResult() {

        mUserPresenter.getWeeklyResult(mUserId, mAccessToken, new Callback<TopResults>() {
            @Override
             public void success(TopResults topResults, Response response) {
                if (topResults.getStatus()) {
                    mPbWeekly.setVisibility(View.GONE);
                    showWeeklyResultList(topResults.getTopResultData());
                } else {
                    CommonUtility.showErrorMessage(getActivity(), topResults.getResponseMessageList());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                CommonUtility.showRetrofitErrorMessage(error);
            }
        });
    }

    private void showWeeklyResultList(HashMap<String, String> topResultData) {
        mLvWeekTopResult.setVisibility(View.VISIBLE);
        if(topResultData.size()>0) {
            List<String> candidateList = new ArrayList<>();
            int i = 0;
            for (Map.Entry<String, String> entry : topResultData.entrySet()) {
                i++;
                candidateList.add(i + ") " + entry.getKey());
            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter(
                    getActivity(),
                    android.R.layout.simple_list_item_1,
                    candidateList);

            mLvWeekTopResult.setAdapter(arrayAdapter);
        }
    }

    private void getMonthlyResult() {

        mUserPresenter.getMonthlyResult(mUserId, mAccessToken, new Callback<TopResults>() {
          @Override
            public void success(TopResults topResults, Response response) {
                if (topResults.getStatus()) {
                    mPbWeekly.setVisibility(View.GONE);
                    showMonthlyResultList(topResults.getTopResultData());
                } else {
                    CommonUtility.showErrorMessage(getActivity(), topResults.getResponseMessageList());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                CommonUtility.showRetrofitErrorMessage(error);
            }
        });
    }

    private void showMonthlyResultList(HashMap<String, String> topResultData) {
        mLvMonthResult.setVisibility(View.VISIBLE);
        if(topResultData.size()>0) {
            List<String> candidateList = new ArrayList<>();
            int i = 0;
            for (Map.Entry<String, String> entry : topResultData.entrySet()) {
                i++;
                candidateList.add(i + ") " + entry.getKey());
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter(
                    getActivity(),
                    android.R.layout.simple_list_item_1,
                    candidateList);

            mLvMonthResult.setAdapter(arrayAdapter);
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btn_submit:
                sendVerificationCode();
                break;

            case R.id.btn_verify:
                verifyMobileNumber();
                break;
        }
    }

    private void verifyMobileNumber() {

        if(ValidateInputFields.isMobileNumberValid(mEdtNumber.getText().toString().trim())){
            if(!TextUtils.isEmpty(mEdtCode.getText().toString())){
                verifyNumber(mEdtCode.getText().toString().trim());
            }else{
                CommonUtility.showToast(getActivity(), "Enter activation code");
            }
        }else{
            CommonUtility.showToast(getActivity(), getString(R.string.invalid_number));
        }

    }

    private void verifyNumber(String activationCode) {
        mParams = new ProgressDialogParams();
        mParams.setMessage(getString(R.string.please_wait));
        mParams.setCancellable(false);
        mProgressHandler = new ProgressDialogHandler(getActivity());
        mProgressHandler.showProgressDialog(mParams);

        mUserPresenter.verifyMobile(mUserId, mAccessToken,new VerificationCode(activationCode, mEmail), new Callback<RecordSave>() {
            @Override
            public void success(RecordSave recordSave, Response response) {
                mProgressHandler.dismissProgressDialog();
                if (!recordSave.getStatus()) {
                    CommonUtility.showErrorMessage(getActivity(), recordSave.getResponseMessageList());
                } else {
                    mLytTopResults.setVisibility(View.VISIBLE);
                    mLytMobileVerification.setVisibility(View.GONE);
                    getTopResults();
                    PreferencesUtility.setSharedPrefBooleanData(getActivity(),AppConstants.PREF_MOBILE_VERIFIED,recordSave.getData().isMobileNumberVerified());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                mProgressHandler.dismissProgressDialog();
                CommonUtility.showRetrofitErrorMessage(error);
            }
        });


    }

    private void startTimer() {

        timeCounter = new CountDownTimer(2*60*1000, 1000) {
            public void onTick(long millisUntilFinished) {

                String time=String.format("%2d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))
                );
                mBtnSubmit.setText(time);
                mBtnSubmit.setEnabled(false);
                mEdtNumber.setEnabled(false);

            }

            public void onFinish() {
                timeCounter.cancel();
                mEdtNumber.setEnabled(true);
                mBtnSubmit.setEnabled(true);
                mBtnSubmit.setText("RESEND");

            }
        };

        timeCounter.start();
    }

    private void sendVerificationCode() {

            if(ValidateInputFields.isMobileNumberValid(mEdtNumber.getText().toString().trim())){
                mMobileNumber=mEdtNumber.getText().toString().trim();
                sendNumberToServer();
            }else{
                CommonUtility.showToast(getActivity(), getString(R.string.invalid_number));
            }

    }

    private void sendNumberToServer() {

        mParams = new ProgressDialogParams();
        mParams.setMessage("Sending OTP...");
        mParams.setCancellable(false);
        mProgressHandler = new ProgressDialogHandler(getActivity());
        mProgressHandler.showProgressDialog(mParams);

        mUserPresenter.sendNumberToServer(mUserId, mAccessToken,new VerifyMobileNumber(mMobileNumber,mEmail), new Callback<RecordSave>() {
           @Override
            public void success(RecordSave recordSave, Response response) {
                mProgressHandler.dismissProgressDialog();
                if(!recordSave.getStatus()){
                    CommonUtility.showErrorMessage(getActivity(),recordSave.getResponseMessageList());
                }else{
                    startTimer();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                mProgressHandler.dismissProgressDialog();
                CommonUtility.showRetrofitErrorMessage(error);
            }
        });

    }
}
