package com.prepp.presenter;


import com.prepp.model.BranchListData;
import com.prepp.model.TopResults;
import com.prepp.model.VerificationCode;
import com.prepp.model.VerifyMobileNumber;
import com.prepp.model.QuestionsOptions;
import com.prepp.model.RecordSave;
import com.prepp.model.SignUpInfo;
import com.prepp.model.SubmitAnswer;
import com.prepp.model.UserData;
import com.prepp.model.VerificationData;
import com.prepp.dataService.UserDataService;

import retrofit.Callback;


/**
 * Created by Mohit Sharma on 26/8/15.
 */
public class UserPresenter  {

    private UserDataService mDataService;


    public UserPresenter(UserDataService userDataService) {
        mDataService = userDataService;
    }

    public void getBranchList(Callback<BranchListData> branchList) {
        mDataService.getBranchList(branchList);
    }


    public void registerData(UserData userData, Callback<SignUpInfo> callback) {
        mDataService.registerUser(userData, callback);

    }

    public void verifyEmailAccount(String id, VerificationData verificationData, Callback<SignUpInfo> signUp) {
        mDataService.verifyEmailAccount(id, verificationData, signUp);
    }

    public void getQuestionList(String id, String accessToken, Callback<QuestionsOptions> callback) {
            mDataService.getQuestionList(id, accessToken, callback);
    }

    public void submitAnswer(String mUserId, String mAccessToken, SubmitAnswer submitAnswer, Callback<RecordSave> callback) {
        mDataService.submitAnswer(mUserId, mAccessToken, submitAnswer, callback);
    }

    public void sendNumberToServer(String id, String accessToken, VerifyMobileNumber verifyMobileNumber, Callback<RecordSave> callback) {
        mDataService.sendNumberToServer(id, accessToken, verifyMobileNumber, callback);
    }

    public void verifyMobile(String id, String accessToken, VerificationCode verifyMobileNumber, Callback<RecordSave> callback) {
        mDataService.verifyMobile(id, accessToken, verifyMobileNumber, callback);
    }

    public void getWeeklyResult(String id, String accessToken, Callback<TopResults> callback) {
        mDataService.getWeeklyResult(id, accessToken, callback);
    }

    public void getMonthlyResult(String id, String accessToken, Callback<TopResults> callback) {
        mDataService.getMonthlyResult(id,accessToken,callback);
    }

    public void checkUserValid(UserData userData, Callback<SignUpInfo> callback) {
        mDataService.checkUserValid(userData,callback);
    }
}
