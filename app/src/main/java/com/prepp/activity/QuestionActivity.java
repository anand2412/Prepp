package com.prepp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.prepp.utils.AppConstants;
import com.prepp.PreppApp;
import com.prepp.R;
import com.prepp.customviews.ProgressDialogHandler;
import com.prepp.helper.ProgressDialogParams;
import com.prepp.model.CandidateResult;
import com.prepp.model.Options;
import com.prepp.model.QuestionOptionData;
import com.prepp.model.QuestionsOptions;
import com.prepp.model.RecordSave;
import com.prepp.model.SubmitAnswer;
import com.prepp.presenter.UserPresenter;
import com.prepp.utils.CommonUtility;
import com.prepp.utils.PreferencesUtility;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class QuestionActivity extends AppCompatActivity implements View.OnClickListener {

    private UserPresenter mUserPresenter;
    private RadioGroup mRdgAnswers;
    private TextView mTvQuestionText;
    private TextView mTvQuestionHeading;
    private int mQuesNumber=1;
    private Button mBtnSubmit;
    private HashMap<Integer, CandidateResult> mCandidateMap;
    private List<Options> mOptionList;
    private Map.Entry<String, QuestionOptionData> mQuestionAnswerEntry;
    private String mUserId;
    private String mAccessToken;
    private QuestionsOptions mQuestionAnswer;
    private ImageView mImgProcessCircle;
    private ScrollView mScrollQuestionView;
    private RelativeLayout mLytWaitView;
    private TextView mTxtWaitTime;
    private TextView mTxtValidityTimer;
    private CountDownTimer timeCounter;
    private ProgressDialogParams mParams;
    private ProgressDialogHandler mProgressHandler;
    private ProgressBar mProgressBar;
    private ImageView mImgQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        initViews();

        getQuestionList();
    }

    private void initViews() {

        mProgressBar=(ProgressBar)findViewById(R.id.progressBar);
        mTxtValidityTimer=(TextView)findViewById(R.id.txt_validity_timer);
        mTxtWaitTime=(TextView)findViewById(R.id.txt_wait_time);
        mLytWaitView=(RelativeLayout)findViewById(R.id.lyt_next_wait);
        mScrollQuestionView=(ScrollView)findViewById(R.id.scroll_question);
        mImgProcessCircle=(ImageView)findViewById(R.id.img_progress_circle);
        mTvQuestionHeading=(TextView)findViewById(R.id.question_heading);
        mTvQuestionText=(TextView)findViewById(R.id.tv_question);
        mImgQuestion =(ImageView)findViewById(R.id.img_question);
        mRdgAnswers=(RadioGroup)findViewById(R.id.rdg_answers);
        mBtnSubmit=(Button)findViewById(R.id.btn_submit);
        mBtnSubmit.setOnClickListener(this);

    }

    private void getQuestionList() {
        mScrollQuestionView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        mUserPresenter = ((PreppApp)getApplication()).getPresenterFactory().getUserPresenter();
        mAccessToken=PreferencesUtility.getSharedPrefStringData(this, AppConstants.PREF_ACCESS_TOKEN);
        mUserId=String.valueOf(PreferencesUtility.getSharedPrefIntData(this, AppConstants.PREF_USR_ID));
        mUserPresenter.getQuestionList(mUserId, mAccessToken, new Callback<QuestionsOptions>() {
            @Override
            public void success(QuestionsOptions questionsOptions, Response response) {

                if (questionsOptions.getStatus()) {
                    mScrollQuestionView.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.GONE);
                    mQuestionAnswer = questionsOptions;
                    mCandidateMap = new HashMap<>();
                    setQuestionAndAnswer();
                } else {
                    CommonUtility.showErrorMessage(QuestionActivity.this, questionsOptions.getResponseMessageList());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                CommonUtility.showRetrofitErrorMessage(error);
            }
        });
    }

    private void setQuestionAndAnswer() {
        mScrollQuestionView.pageScroll(ScrollView.FOCUS_UP);
        if(mQuestionAnswer.getQuestionOptionDataMap().size()>0) {
            int index = 1;
            for (Map.Entry<String, QuestionOptionData> entry : mQuestionAnswer.getQuestionOptionDataMap().entrySet()) {
                if (index == mQuesNumber) {
                    mQuestionAnswerEntry = entry;
                    break;
                }
                index++;
            }
            startTimer();
            setProgressImage();


            mTvQuestionHeading.setText("Question " + mQuesNumber);
            if(mQuestionAnswerEntry.getValue().getQuestionImageUrl()!=null){
                CommonUtility.loadImageWithGlide(this,mQuestionAnswerEntry.getValue().getQuestionImageUrl(), mImgQuestion);
                mImgQuestion.setVisibility(View.VISIBLE);
            }else{
                mImgQuestion.setVisibility(View.GONE);
            }
            mTvQuestionText.setText(mQuestionAnswerEntry.getValue().getQuestion());
            mRdgAnswers.removeAllViews();
            mRdgAnswers.clearCheck();
            RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(20, 10, 20, 0);
            mOptionList = mQuestionAnswerEntry.getValue().getOptionsList();
            for (int i = 0; i < mOptionList.size(); i++) {
                RadioButton rdbtn = new RadioButton(this);
                rdbtn.setLayoutParams(layoutParams);
                rdbtn.setId(i);
                rdbtn.setText(mOptionList.get(i).getOptionValue());
                mRdgAnswers.addView(rdbtn);
            }
        }

    }

    private void setProgressImage() {

        switch (mQuesNumber){

            case 1:
                mImgProcessCircle.setImageResource(R.drawable.progress_1);
               break;
            case 2:
                mImgProcessCircle.setImageResource(R.drawable.progress_2);
                break;
            case 3:
                mImgProcessCircle.setImageResource(R.drawable.progress_3);
                break;
            case 4:
                mImgProcessCircle.setImageResource(R.drawable.progress_4);
                break;
            case 5:
                mImgProcessCircle.setImageResource(R.drawable.progress_5);
                break;
            case 6:
                mImgProcessCircle.setImageResource(R.drawable.progress_6);
                break;
        }
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btn_submit:
                int selectedId = mRdgAnswers.getCheckedRadioButtonId();
                if(selectedId==-1){
                    showAlertDialog(selectedId);
                }else {
                    timeCounter.cancel();
                    mTxtValidityTimer.setVisibility(View.INVISIBLE);
                    saveDataForResult(selectedId);
                    submitAnswer(selectedId);
                }
                break;

        }
    }

    private void showAlertDialog(final int selectedId) {

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.app_name))
                .setMessage(getString(R.string.dialog_no_answer))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        timeCounter.cancel();
                        mTxtValidityTimer.setVisibility(View.INVISIBLE);
                        saveDataForResult(selectedId);
                        submitAnswer(selectedId);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void showFinishAlert() {

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.app_name))
                .setMessage("Are you sure want to quit?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        timeCounter.cancel();
                        finish();
                        System.exit(0);

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private String covertTime(){

        Calendar calendaram = Calendar.getInstance();
        calendaram.set(Calendar.HOUR_OF_DAY, 23);
        calendaram.set(Calendar.MINUTE, 59);
        calendaram.set(Calendar.SECOND, 58);

        long millis=calendaram.getTimeInMillis()-System.currentTimeMillis();

        String hms = String.format("%2d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)));
        return hms;
    }

    @Override
    public void onBackPressed() {
        showFinishAlert();

    }

    private void showWaitView() {

        if(mQuesNumber>=6){
            EventBus.getDefault().postSticky(mCandidateMap);
            PreferencesUtility.setSharedPrefLongData(QuestionActivity.this, AppConstants.PREF_TIME, System.currentTimeMillis());
            Intent intent=new Intent(QuestionActivity.this,FinishQuizActivity.class);
            intent.putExtra("showResult",1);
            intent.putExtra("WAIT_TIME",covertTime());
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();

        }else {
            mScrollQuestionView.setVisibility(View.GONE);
            mBtnSubmit.setVisibility(View.GONE);
            mLytWaitView.setVisibility(View.VISIBLE);

            CountDownTimer waitTimeCounter = new CountDownTimer(4000, 1000) {
                public void onTick(long millisUntilFinished) {

                    String time = String.format("%2d", TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished));
                    mTxtWaitTime.setText(time);

                }

                public void onFinish() {
                    mScrollQuestionView.setVisibility(View.VISIBLE);
                    mBtnSubmit.setVisibility(View.VISIBLE);
                    mLytWaitView.setVisibility(View.GONE);
                    mQuesNumber = mQuesNumber + 1;
                    setQuestionAndAnswer();
                }
            };

            waitTimeCounter.start();
        }
    }

    private String covertTime(long millis){
        String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
        return hms;
    }

    private void startTimer() {

        timeCounter = new CountDownTimer(2*60*1000, 1000) {
            public void onTick(long millisUntilFinished) {

                String time=String.format("%2d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))
                );
                mTxtValidityTimer.setVisibility(View.VISIBLE);
                mTxtValidityTimer.setText(time);

            }

            public void onFinish() {
                int selectedId = mRdgAnswers.getCheckedRadioButtonId();
                timeCounter.cancel();
                mTxtValidityTimer.setVisibility(View.INVISIBLE);
                saveDataForResult(selectedId);
                submitAnswer(selectedId);
            }
        };

        timeCounter.start();
    }

    private void submitAnswer(int id) {

        mParams = new ProgressDialogParams();
        mParams.setMessage(getString(R.string.please_wait));
        mParams.setCancellable(false);
        mProgressHandler = new ProgressDialogHandler(this);
        mProgressHandler.showProgressDialog(mParams);
        int answerId;
        boolean isAttempted;
        if(id==-1) {
            answerId = 0;
            isAttempted=false;
        }else {
            answerId = mOptionList.get(id).getOptionId();
            isAttempted=true;
        }
        int quesId=mQuestionAnswerEntry.getValue().getQuestionId();

        mUserPresenter.submitAnswer(mUserId, mAccessToken,new SubmitAnswer(answerId,quesId,isAttempted), new Callback<RecordSave>() {
           @Override
            public void success(RecordSave recordSave, Response response) {
                mProgressHandler.dismissProgressDialog();
                if(recordSave.getStatus()){
                    showWaitView();
                }else{
                    CommonUtility.showErrorMessage(QuestionActivity.this,recordSave.getResponseMessageList());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                mProgressHandler.dismissProgressDialog();
                CommonUtility.showRetrofitErrorMessage(error);
            }
        });

    }

    private void saveDataForResult(int selectedId) {
        CandidateResult candidateResult=new CandidateResult();
        if(selectedId!=-1) {
            if (mOptionList.get(selectedId).isAnswer()) {
                candidateResult.setCorrect(1);
            } else if (!mOptionList.get(selectedId).isAnswer()) {
                candidateResult.setCorrect(0);
            }
        }else{
            candidateResult.setCorrect(-1);
        }
        for(int i=0 ;i<mOptionList.size(); i++){
            if(mOptionList.get(i).isAnswer()) {
                candidateResult.setCorrectAnswer(mOptionList.get(i).getOptionValue());
                candidateResult.setCorrectAnsNum(i + 1);
                break;
            }
        }
        candidateResult.setOptionsList(mOptionList);
        candidateResult.setQuestion(mQuestionAnswerEntry.getValue().getQuestion());
        candidateResult.setExplanation(mQuestionAnswerEntry.getValue().getExplanation());
        mCandidateMap.put(mQuestionAnswerEntry.getValue().getQuestionId(),candidateResult);
    }
}
