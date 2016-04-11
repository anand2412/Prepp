package com.prepp.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.prepp.PreppApp;
import com.prepp.R;
import com.prepp.fragment.LeaderBoardDashboardFragment;
import com.prepp.fragment.QuizDashboardFragment;
import com.prepp.fragment.ResultsDashboardFragment;
import com.prepp.fragment.WaitFragment;
import com.prepp.model.CandidateResult;
import com.prepp.model.SignUpInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UserDashboardActivity extends BaseActivity {

    private CollapsingToolbarLayout mCollapsingToolbar;
    private TextView mTxtQuotes;
    private ViewPager mViewPagerQuotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        String [] quotesArray=getResources().getStringArray(R.array.quotes);

        /*mTxtQuotes=(TextView)findViewById(R.id.tv_quotes);
        Random random=new Random();
        int randomNumber=random.nextInt(7 - 0 + 1) + 0;
        mTxtQuotes.setText(quotesArray[randomNumber]);*/

        mViewPagerQuotes=(ViewPager)findViewById(R.id.viewpager_quotes);
        QuotesPagerAdapter quotesPagerAdapter=new QuotesPagerAdapter(quotesArray);
        mViewPagerQuotes.setAdapter(quotesPagerAdapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mCollapsingToolbar= (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);
        mCollapsingToolbar.setTitleEnabled(false);
        mCollapsingToolbar.setTitle("");
        setSupportActionBar(toolbar);


        //Remove the default title
        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.app_name);
        ab.setDisplayShowHomeEnabled(true); // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_actionbar);

        setupViewPager();


    }

   /* public void onEvent(SignUpInfo signUpInfo) {
        Log.e("######second",signUpInfo.getSignUpData().getEmail());
    }
*/
    private void setupViewPager() {
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        if(getIntent()!=null) {
            if (TextUtils.isEmpty(getIntent().getStringExtra("WAIT_TIME")))
                adapter.addFrag(new QuizDashboardFragment().newInstance(getIntent().getBooleanExtra("MAIL_ACTIVE", false)), getString(R.string.tab_first_title));
            else {
                adapter.addFrag(new WaitFragment().newInstance(getIntent().getStringExtra("WAIT_TIME")), getString(R.string.tab_first_title));
            }
        }else{
            adapter.addFrag(new QuizDashboardFragment().newInstance(getIntent().getBooleanExtra("MAIL_ACTIVE", false)), getString(R.string.tab_first_title));
        }

        adapter.addFrag(new ResultsDashboardFragment(),getString(R.string.tab_sec_title));
        adapter.addFrag(new LeaderBoardDashboardFragment(), getString(R.string.tab_third_title));
        viewPager.setAdapter(adapter);

        if(getIntent()!=null && getIntent().getIntExtra("showResult",0)==1){
            viewPager.setCurrentItem(1);
        }else{
            viewPager.setCurrentItem(0);
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_user_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            shareApp();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void shareApp() {

        Intent shareIntent =
                new Intent(android.content.Intent.ACTION_SEND);

//set the type
        shareIntent.setType("text/plain");

//add a subject
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                "Share Prepp");

//build the body of the message to be shared
        String shareMessage = "Share Prepp and earn and enjoy";

//add the message
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                shareMessage);

//start the chooser for sharing
        startActivity(Intent.createChooser(shareIntent,
                getString(R.string.app_name)));
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
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

    class QuotesPagerAdapter extends PagerAdapter {

        private  LayoutInflater inflater;
        private String [] quotes;
        public QuotesPagerAdapter (String [] quotes){
            this.quotes=quotes;
            inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return quotes.length;
        }


        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            View pagerLayout = inflater.inflate(R.layout.quote,
                    view, false);

            assert  pagerLayout!=null;

            TextView txtQuotes = (TextView) pagerLayout
                    .findViewById(R.id.tv_quotes);
            txtQuotes.setText(quotes[position]);
            view.addView(pagerLayout);
            return pagerLayout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
