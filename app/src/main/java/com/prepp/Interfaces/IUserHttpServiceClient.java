package com.prepp.Interfaces;

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
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;


/**
 * <p/>
 * Project: <b>blogmint-android</b><br/>
 * Created by: Anand K. Rai on 7/12/15.<br/>
 * Email id: anand.rai@tothenew.com<br/>
 * <p/>
 */
public interface IUserHttpServiceClient {

    @GET("/api/branches")
    void getBranchList(Callback<BranchListData> branchList);

    @POST("/api/users")
    void registerData(@Body UserData userData, Callback<SignUpInfo> callback);

    @POST("/api/users/{id}")
    void verifyAccount(@Path("id") String id, @Body VerificationData verificationData, Callback<SignUpInfo> signUp);

    @GET("/api/users/{id}/questions/{accessToken}")
    void getQuestionList(@Path("id") String id, @Path("accessToken") String accessToken, Callback<QuestionsOptions> callback);

    @POST("/api/users/{id}/questions/{accessToken}")
    void submitAnswer(@Path("id")String userId, @Path("accessToken")String accessToken, @Body SubmitAnswer submitAnswer, Callback<RecordSave> callback);

    @POST("/api/users/{id}/code/{accessToken}")
    void sendNumberToServer(@Path("id") String id, @Path("accessToken") String accessToken, @Body VerifyMobileNumber verifyMobileNumber, Callback<RecordSave> callback);

    @POST("/api/users/{id}/verifymobile/{accessToken}")
    void verifyMobile(@Path("id") String id, @Path("accessToken") String accessToken,@Body VerificationCode verifyMobileNumber, Callback<RecordSave> callback);

    @GET("/api/users/{id}/{accessToken}")
    void getWeeklyResult(@Path("id")String id, @Path("accessToken") String accessToken, Callback<TopResults> callback);

    @GET("/api/users/{id}/results/{accessToken}")
    void getMonthlyResult(@Path("id")String id, @Path("accessToken") String accessToken, Callback<TopResults> callback);

    @POST("/api/users/validate")
    void checkUserValid(@Body UserData userData, Callback<SignUpInfo> callback);
}