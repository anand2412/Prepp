package com.prepp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.prepp.PreppApp;
import com.prepp.R;
import com.prepp.model.CandidateResult;
import com.prepp.model.Options;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.greenrobot.event.EventBus;

public class ExplanationActivity extends AppCompatActivity {

    private TextView mTvQuestion;
    private TextView mTvExplanation;
    private RadioGroup mRdgAnswers;
    private RelativeLayout mLytBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explaination);

        mTvQuestion= (TextView)findViewById(R.id.tv_question);
        mTvExplanation= (TextView)findViewById(R.id.tv_explanation);
        mRdgAnswers=(RadioGroup)findViewById(R.id.rdg_answers);
        mLytBack=(RelativeLayout)findViewById(R.id.lyt_back);
        mLytBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
        EventBus.getDefault().registerSticky(this);
    }


    public void onEventMainThread(CandidateResult candidateResult) {

        mTvQuestion.setText(candidateResult.getQuestion());
        mTvExplanation.setText(candidateResult.getExplanation());
        mRdgAnswers.removeAllViews();
        mRdgAnswers.clearCheck();
        RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(20, 10, 20, 0);
        List<Options> optionList = candidateResult.getOptionsList();
        for (int i = 0; i < optionList.size(); i++) {
            RadioButton rdbtn = new RadioButton(this);
            rdbtn.setLayoutParams(layoutParams);
            rdbtn.setId(i);
            rdbtn.setText(optionList.get(i).getOptionValue());
            rdbtn.setClickable(false);
            if(candidateResult.getCorrectAnswer().equals(optionList.get(i).getOptionValue())) {
                rdbtn.setChecked(true);
            }
            mRdgAnswers.addView(rdbtn);
        }

    }
}
