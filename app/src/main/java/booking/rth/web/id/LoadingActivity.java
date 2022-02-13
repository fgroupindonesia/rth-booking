package booking.rth.web.id;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import helper.ErrorLogger;

public class LoadingActivity extends AppCompatActivity {

    final int WAITING_TIME = 3000; // 3seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                gotoAnotherActivity();
            }
        }, WAITING_TIME);



    }

    private void gotoAnotherActivity(){
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);

        finish();
    }

}
