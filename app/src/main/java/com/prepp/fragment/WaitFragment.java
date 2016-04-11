package com.prepp.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.prepp.R;
import com.prepp.utils.AppConstants;
import com.prepp.utils.PreferencesUtility;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class WaitFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";




    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public WaitFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_wait, container, false);
        initViews(rootView);
        return rootView;
    }

    private void initViews(View rootView) {

        String WAIT_UNTIL =getString(R.string.wait_until);
        String WAIT_TIME= getArguments().getString(ARG_PARAM1)+" hours";
        String NEXT_TURN=getString(R.string.for_next_text);

        LinearLayout lytRateUs=(LinearLayout)rootView.findViewById(R.id.lyt_rate);
        if(!PreferencesUtility.getSharedPrefBooleanData(getActivity(),AppConstants.PREF_RATE_US)){
            lytRateUs.setVisibility(View.VISIBLE);
        }else{
            lytRateUs.setVisibility(View.GONE);
        }

        TextView txtHelpUs=(TextView)rootView.findViewById(R.id.txt_help_us);
        txtHelpUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareApp();
            }
        });

        TextView txtWaitTime=(TextView)rootView.findViewById(R.id.txt_wait_time);
        String combindString=WAIT_UNTIL+WAIT_TIME+NEXT_TURN;
        SpannableString waitSpannableString = new SpannableString(combindString);
        waitSpannableString.setSpan(new ForegroundColorSpan(Color.RED),WAIT_UNTIL.length() , (WAIT_UNTIL+WAIT_TIME).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtWaitTime.setText(waitSpannableString);
    }

    private void shareApp() {
        PreferencesUtility.setSharedPrefBooleanData(getActivity(), AppConstants.PREF_RATE_US,true);
        Intent intentRate = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getActivity().getPackageName()));
        startActivity(intentRate);
    }



    public WaitFragment newInstance(String waitTime) {
        WaitFragment fragment = new WaitFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, waitTime);
        fragment.setArguments(args);
        return fragment;
    }

}
