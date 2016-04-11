package com.prepp.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.prepp.R;
import com.prepp.activity.ExplanationActivity;
import com.prepp.model.CandidateResult;

import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * <p/>
 * Project: <b>PREPP</b><br/>
 * Created by: Anand K. Rai on 13/1/16.<br/>
 * Email id: anand.rai@tothenew.com<br/>
 * <p/>
 */
public class ResultsDashboardFragment extends Fragment {

    private LinearLayout mLytResult;
    private TextView mTxtIncorrAnsw;
    private TextView mTxtCorrAnsw;
    private TextView mTxtNumQuesAnswerd;
    private int mNumQuesAnswe, mNumCorrAnsw, mNumIncorrAnsw, mTotalPoint;
    private TextView mTxtTotalPoint;
    private CardView mCardResult;
    private CardView mCardScore;
    private TextView mTxtMessage;
    private TextView mTxtGotIt;
    private CardView mCardGotIt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView=inflater.inflate(R.layout.fragment_my_result,container,false);
        mLytResult=(LinearLayout)rootView.findViewById(R.id.lyt_result);
        mTxtNumQuesAnswerd=(TextView)rootView.findViewById(R.id.txt_ques_ansd_value);
        mTxtCorrAnsw=(TextView)rootView.findViewById(R.id.txt_corr_answd_value);
        mTxtIncorrAnsw=(TextView)rootView.findViewById(R.id.txt_incrr_ansd_value);
        mTxtTotalPoint=(TextView)rootView.findViewById(R.id.txt_point_value);
        mCardScore=(CardView)rootView.findViewById(R.id.card_scores);
        mCardResult=(CardView)rootView.findViewById(R.id.card_result);
        mTxtMessage=(TextView)rootView.findViewById(R.id.txt_msg);
        mTxtGotIt=(TextView)rootView.findViewById(R.id.txt_got_it);
        mCardGotIt=(CardView)rootView.findViewById(R.id.card_view_got_it);
        mTxtMessage.setVisibility(View.VISIBLE);
        mCardResult.setVisibility(View.GONE);
        mCardScore.setVisibility(View.GONE);
        EventBus.getDefault().registerSticky(this);
        mTxtGotIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCardGotIt.setVisibility(View.GONE);
            }
        });


        return rootView;
    }

    public void onEventMainThread(final HashMap<Integer, CandidateResult> candidateMap){
        mLytResult.removeAllViews();
        int i=0;

        if(candidateMap.size()>0) {

            mTxtMessage.setVisibility(View.GONE);
            mCardResult.setVisibility(View.VISIBLE);
            mCardScore.setVisibility(View.VISIBLE);

            for (Map.Entry<Integer, CandidateResult> candidateResultMap : candidateMap.entrySet()) {
                int quesId = candidateResultMap.getKey();
                i++;
                CandidateResult candidateResult = candidateResultMap.getValue();
                View view = getActivity().getLayoutInflater().inflate(R.layout.row_answer_result, null, false);
                TextView txtQuestionHeading = (TextView) view.findViewById(R.id.tv_question);
                TextView txtAnswerType = (TextView) view.findViewById(R.id.tv_answer_type);
                TextView txtCorrectAnswer = (TextView) view.findViewById(R.id.tv_correct_answer);
                TextView txtPoints = (TextView) view.findViewById(R.id.tv_points);

                txtQuestionHeading.setText("Question " + i);
                txtCorrectAnswer.setText(candidateResult.getCorrectAnswer());
                if (candidateResult.getCorrect() == 1) {
                    txtAnswerType.setText("Answered Correctly");
                    txtAnswerType.setTextColor(getResources().getColor(R.color.green));
                    txtPoints.setText("+1 pt");
                    mNumCorrAnsw = mNumCorrAnsw + 1;
                    mNumQuesAnswe = mNumQuesAnswe + 1;
                    mTotalPoint = mTotalPoint + 1;
                } else if (candidateResult.getCorrect() == -1) {
                    txtAnswerType.setText("Not Answered");
                    txtAnswerType.setTextColor(getResources().getColor(R.color.search));
                    txtPoints.setText("0 pt");
                } else {
                    mNumIncorrAnsw = mNumIncorrAnsw + 1;
                    mNumQuesAnswe = mNumQuesAnswe + 1;
                    txtPoints.setText("0 pt");
                    txtAnswerType.setText("Answered Incorrectly");
                    txtAnswerType.setTextColor(getResources().getColor(R.color.red));
                }
                view.setTag(quesId);
                mLytResult.addView(view);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CandidateResult viewCandidateResult = candidateMap.get(view.getTag());
                        EventBus.getDefault().postSticky(viewCandidateResult);
                        Intent intent = new Intent(getActivity(), ExplanationActivity.class);
                        startActivity(intent);
                    }
                });
            }
            setPointResult();
        }
    }

    private void setPointResult() {

        mTxtNumQuesAnswerd.setText(String.valueOf(mNumQuesAnswe));
        mTxtCorrAnsw.setText(String.valueOf(mNumCorrAnsw));
        mTxtTotalPoint.setText(String.valueOf(mTotalPoint));
        mTxtIncorrAnsw.setText(String.valueOf(mNumIncorrAnsw));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
