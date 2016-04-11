package com.prepp.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.prepp.R;
import com.prepp.fragment.IntroFirstFragment;
import com.prepp.fragment.IntroFourthFragment;
import com.prepp.fragment.IntroSecondFragment;
import com.prepp.fragment.IntroThirdFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class IntroActivity extends AppCompatActivity {

    private static final String FIRST_TAG="first";
    private static final String SECOND_TAG="second";
    private static final String THIRD_TAG="third";
    private static final String FOURTH_TAG="fourth";

    private ViewPager mVpIntro;
    private IntroPagerAdapter mIntroPagerAdapter;
    private ImageView mImgProgressCircle;
    private  Timer timer;
    private int page = 0;
    private Button mBtnNext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_first);

        mBtnNext=(Button)findViewById(R.id.btn_lets_start);
        mBtnNext.setOnClickListener(viewClickListener);

        mImgProgressCircle=(ImageView)findViewById(R.id.img_progress_circle);

        mVpIntro=(ViewPager)findViewById(R.id.vp_intro_item);
        mIntroPagerAdapter = new IntroPagerAdapter(getFragmentManager());
        mIntroPagerAdapter.addFrag(new IntroFirstFragment(),FIRST_TAG);
        mIntroPagerAdapter.addFrag(new IntroSecondFragment(), SECOND_TAG);
        mIntroPagerAdapter.addFrag(new IntroThirdFragment(), THIRD_TAG);
        mIntroPagerAdapter.addFrag(new IntroFourthFragment(), FOURTH_TAG);
        mVpIntro.setAdapter(mIntroPagerAdapter);
        mVpIntro.setCurrentItem(0,true);
        //pageSwitcher(2);
        mImgProgressCircle.setImageResource(R.drawable.progress_circles);

        mVpIntro.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if(page<i){
                    page++;
                }else{
                    page--;
                }
                switch (i){
                    case 0:
                        mImgProgressCircle.setImageResource(R.drawable.progress_circles);
                        mBtnNext.setText(R.string.next);
                        break;
                    case 1:
                        mImgProgressCircle.setImageResource(R.drawable.progress_circles_second);
                        mBtnNext.setText(R.string.next);
                        break;
                    case 2:
                        mImgProgressCircle.setImageResource(R.drawable.progress_circles_third);
                        mBtnNext.setText(R.string.next);
                        break;
                    case 3:
                        mImgProgressCircle.setImageResource(R.drawable.progress_circles_fourth);
                        mBtnNext.setText(R.string.lets_get_started);
                        break;
                    default:

                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void pageSwitcher(int seconds) {
        timer = new Timer(); // At this line a new Thread will be created
        timer.scheduleAtFixedRate(new RemindTask(), 0, seconds * 1000); // delay
        // in
        // milliseconds
    }

    // this is an inner class...
    class RemindTask extends TimerTask {

        @Override
        public void run() {

            // As the TimerTask run on a seprate thread from UI thread we have
            // to call runOnUiThread to do work on UI thread.
            runOnUiThread(new Runnable() {
                public void run() {

                    if (page > 4) { // In my case the number of pages are 5
                        timer.cancel();

                    } else {
                        mVpIntro.setCurrentItem(page++,true);
                    }
                }
            });

        }
    }

    View.OnClickListener viewClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()){

                case R.id.btn_lets_start:
                    switch (mVpIntro.getCurrentItem()){

                        case 0:
                            mVpIntro.setCurrentItem(1);
                            break;
                        case 1:
                            mVpIntro.setCurrentItem(2);
                            break;
                        case 2:
                            mVpIntro.setCurrentItem(3);
                            break;
                        case 3:
                            Intent intent=new Intent(IntroActivity.this, LoginActivity.class);
                            startActivity(intent);
                            break;
                    }


            }
        }
    };


    class IntroPagerAdapter extends FragmentPagerAdapter {
        List<String> mFragmentTitleList = new ArrayList<>();
        List<Fragment> mFragmentList = new ArrayList<>();

        public IntroPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {

            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }


    }
}
