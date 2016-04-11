package com.prepp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.prepp.utils.AppConstants;
import com.prepp.model.BranchDetail;
import com.prepp.model.BranchListData;
import com.prepp.model.ResponseMessage;
import com.prepp.model.SignUpInfo;
import com.prepp.model.UserData;
import com.prepp.PreppApp;
import com.prepp.R;
import com.prepp.adapter.BranchListAdapter;
import com.prepp.customviews.ProgressDialogHandler;
import com.prepp.helper.ProgressDialogParams;
import com.prepp.presenter.UserPresenter;
import com.prepp.utils.CommonUtility;
import com.prepp.utils.PreferencesUtility;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class BranchSelectionActivity extends BaseActivity {

    private Button mBtnContinue;
    private ListView mLvBranch;
    private BranchListAdapter mBranchListAdapter;
    private UserPresenter mUserPresenter;
    private BranchDetail mSelectedBranchDetail;
    private List<BranchDetail> mBranchListData;
    private ProgressBar mProgressBar;
    private ProgressDialogParams mParams;
    private ProgressDialogHandler mProgressHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_selection);


        mLvBranch=(ListView)findViewById(R.id.lv_branch);
        mBtnContinue=(Button)findViewById(R.id.btn_continue);

        mProgressBar=(ProgressBar)findViewById(R.id.progressBar);

        mLvBranch.setTextFilterEnabled(true);
        mBtnContinue.setOnClickListener(viewClickListener);
        mUserPresenter=((PreppApp)getApplication()).getPresenterFactory().getUserPresenter();
        getBranchList();

       mLvBranch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

               mSelectedBranchDetail = (BranchDetail) mLvBranch.getItemAtPosition(position);
               mBranchListAdapter.mSelectedPosition=position;
               mBranchListAdapter.notifyDataSetChanged();
           }
       });

    }

    View.OnClickListener viewClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()){
                case R.id.btn_continue:
                          registerData();
                    break;

            }
        }
    };


    private void registerData() {

        if(mBranchListAdapter.mSelectedPosition!=-1) {

            mParams = new ProgressDialogParams();
            mParams.setMessage(getString(R.string.please_wait));
            mParams.setCancellable(false);
            mProgressHandler = new ProgressDialogHandler(this);
            mProgressHandler.showProgressDialog(mParams);

            mUserPresenter.registerData
                    (new UserData(mSelectedBranchDetail.getCode(), getIntent().getStringExtra("EMAIL"), "email", getIntent().getStringExtra("NAME"), null,
                                    getIntent().getBooleanExtra("SOCIAL", false)),
                            new Callback<SignUpInfo>() {
                                @Override
                                public void success(SignUpInfo signUpInfo, Response response) {
                                    mProgressHandler.dismissProgressDialog();
                                    if (signUpInfo.getStatus()) {
                                        setNavigationAsResponse(signUpInfo);
                                    } else {
                                        CommonUtility.showErrorMessage(BranchSelectionActivity.this, signUpInfo.getResponseMessageList());
                                    }
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    mProgressHandler.dismissProgressDialog();
                                    CommonUtility.showRetrofitErrorMessage(error);
                                }
                            });
        }else{
            ResponseMessage responseMessage=new ResponseMessage();
            responseMessage.setMessage(getString(R.string.select_brach_alert));
            List<ResponseMessage> responseMessageList=new ArrayList<>();
            responseMessageList.add(responseMessage);
            CommonUtility.showErrorMessage(BranchSelectionActivity.this,responseMessageList);
        }
    }

    private void setNavigationAsResponse(SignUpInfo signUpInfo) {

        PreferencesUtility.setSharedPrefStringData(BranchSelectionActivity.this,
                AppConstants.PREF_EMAIL, signUpInfo.getSignUpData().getEmail());

        PreferencesUtility.setSharedPrefIntData(BranchSelectionActivity.this,
                AppConstants.PREF_USR_ID, signUpInfo.getSignUpData().getId());

        PreferencesUtility.setSharedPrefStringData(BranchSelectionActivity.this,
                AppConstants.PREF_USER_NAME,getIntent().getStringExtra("NAME"));

        if(signUpInfo.getSignUpData().isMobileNumberVerified()){
            PreferencesUtility.setSharedPrefBooleanData(BranchSelectionActivity.this, AppConstants.PREF_MOBILE_VERIFIED, true);
        }else{
            PreferencesUtility.setSharedPrefBooleanData(BranchSelectionActivity.this,AppConstants.PREF_MOBILE_VERIFIED,false);
        }

        if (signUpInfo.getSignUpData().isActive()) {
            PreferencesUtility.setSharedPrefBooleanData(BranchSelectionActivity.this,
                    AppConstants.PREF_IS_ACTIVE, signUpInfo.getSignUpData().isActive());

            PreferencesUtility.setSharedPrefStringData(BranchSelectionActivity.this,
                    AppConstants.PREF_ACCESS_TOKEN, signUpInfo.getSignUpData().getAccessToken());

        } else {
                CommonUtility.showToast(this,
                        signUpInfo.getResponseMessageList().get(0).getMessage());
        }

        Intent intent = new Intent(BranchSelectionActivity.this, UserDashboardActivity.class);
        intent.putExtra("MAIL_ACTIVE",signUpInfo.getSignUpData().isActive());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
       // EventBus.getDefault().postSticky(signUpInfo);
    }


    private void getBranchList() {

        mProgressBar.setVisibility(View.VISIBLE);
        mLvBranch.setVisibility(View.GONE);
        mUserPresenter.getBranchList(new Callback<BranchListData>() {
            @Override
            public void success(BranchListData branchListData, Response response) {

                if (branchListData.getStatus()) {
                    mProgressBar.setVisibility(View.GONE);
                    mLvBranch.setVisibility(View.VISIBLE);
                    if (branchListData.getBranchData().getBranchDetailList() != null && branchListData.getBranchData().getBranchDetailList().size() > 0) {
                        mBranchListData=branchListData.getBranchData().getBranchDetailList();
                        setDataInList();
                    }
                } else {
                    CommonUtility.showErrorMessage(BranchSelectionActivity.this, branchListData.getResponseMessageList());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                CommonUtility.showRetrofitErrorMessage(error);
            }
        });
    }

    private void setDataInList() {
        mBranchListAdapter=new BranchListAdapter(this,mBranchListData);
        mLvBranch.setAdapter(mBranchListAdapter);
    }

}
