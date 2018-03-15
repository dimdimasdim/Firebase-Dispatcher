package com.learn.firebasejobdispatcher;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnSetScheduler, btnCancelScheduler;

    private String DISPATCHER_TAG = "mydispatcher";

    private String CITY = "Jakarta";

    private FirebaseJobDispatcher mDispatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));

        btnCancelScheduler = findViewById(R.id.btn_cancel_scheduler);
        btnSetScheduler = findViewById(R.id.btn_set_scheduler);

        btnSetScheduler.setOnClickListener(this);
        btnCancelScheduler.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_set_scheduler:
                startDispatcher();
                break;
            case R.id.btn_cancel_scheduler:
                cancelDispatcher();
                break;
        }
    }


    public void startDispatcher(){
        Bundle mBundle = new Bundle();
        mBundle.putString(MyJobDispactherService.EXTRAS_CITY, CITY);

        Job mJob = mDispatcher.newJobBuilder()
            .setService(MyJobDispactherService.class)
            .setTag(DISPATCHER_TAG)
            .setRecurring(true)
            .setLifetime(Lifetime.FOREVER)
            .setTrigger(Trigger.executionWindow(0, 10))
            .setReplaceCurrent(true)
            .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
            .setConstraints(Constraint.ON_ANY_NETWORK)
            .setExtras(mBundle)
            .build();
        mDispatcher.mustSchedule(mJob);

    }

    public void cancelDispatcher() {
        mDispatcher.cancel(DISPATCHER_TAG);
    }
}
