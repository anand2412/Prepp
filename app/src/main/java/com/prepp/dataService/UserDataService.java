package com.prepp.dataService;


import com.prepp.Interfaces.IUserHttpServiceClient;
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

import retrofit.Callback;


/**
 * Created by Mohit Sharma on 25/8/15.
 */
public class UserDataService {

    private IUserHttpServiceClient networkClient;

    public UserDataService(IUserHttpServiceClient httpServiceClient){
            networkClient = httpServiceClient;
    }

    public void getBranchList(Callback<BranchListData> branchList) {
        networkClient.getBranchList(branchList);
    }

    public void registerUser(UserData userData, Callback<SignUpInfo> callback) {
        networkClient.registerData(userData, callback);
    }

    public void verifyEmailAccount(String id, VerificationData verificationData, Callback<SignUpInfo> signUp) {
        networkClient.verifyAccount(id, verificationData, signUp);
    }

    public void getQuestionList(String id, String accessToken, Callback<QuestionsOptions> callback) {
        networkClient.getQuestionList(id, accessToken, callback);
    }

    public void submitAnswer(String mUserId, String mAccessToken, SubmitAnswer submitAnswer, Callback<RecordSave> callback) {
        networkClient.submitAnswer(mUserId, mAccessToken, submitAnswer, callback);
    }

    public void sendNumberToServer(String id, String accessToken, VerifyMobileNumber verifyMobileNumber, Callback<RecordSave> callback) {
        networkClient.sendNumberToServer(id, accessToken, verifyMobileNumber, callback);
    }

    public void verifyMobile(String id, String accessToken, VerificationCode verifyMobileNumber, Callback<RecordSave> callback) {
        networkClient.verifyMobile(id, accessToken, verifyMobileNumber, callback);
    }

    public void getWeeklyResult(String id, String accessToken, Callback<TopResults> callback) {
        networkClient.getWeeklyResult(id, accessToken, callback);
    }

    public void getMonthlyResult(String id, String accessToken, Callback<TopResults> callback) {
        networkClient.getMonthlyResult(id,accessToken,callback);
    }

    public void checkUserValid(UserData userData, Callback<SignUpInfo> callback) {
        networkClient.checkUserValid(userData,callback);
    }
}




