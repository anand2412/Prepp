package com.prepp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.prepp.R;
import com.prepp.model.CandidateResult;

import java.util.HashMap;

import de.greenrobot.event.EventBus;

public class FinishQuizActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_quiz);

        Button btnViewResult=(Button)findViewById(R.id.btn_view_result);
        btnViewResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(FinishQuizActivity.this,UserDashboardActivity.class);
                intent.putExtra("showResult",getIntent().getIntExtra("showResult", 1));
                intent.putExtra("WAIT_TIME",getIntent().getStringExtra("WAIT_TIME"));
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        //Do not do any thing
    }
}
