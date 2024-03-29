package booking1.rth.web.id;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import helper.DoubleClickListener;
import helper.OnSwipeListener;
import object.Keys;
import shared.UserData;

public class WelcomeActivity extends AppCompatActivity {

    final int WAITING_TIME = 2000; // 3seconds
    ImageView imageViewLogoRTH;

    boolean workAsClient = true;
    Handler handler = new Handler();

    int user_usage;

    final int MOVE_RIGHT = 1, MOVE_LEFT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // for future usage
        UserData.setPreference(this);

        MediaPlayer mPlayer2 = MediaPlayer.create(this, R.raw.audio_selamat_datang);
       // mPlayer2.start();

        imageViewLogoRTH = (ImageView) findViewById(R.id.imageViewLogoRTH);

        imageViewLogoRTH.setOnClickListener(new DoubleClickListener() {

         	@Override
 			public void onDoubleClick() {
         	    // turning off the delayed schedule
         	    workAsClient = false;
                //handler.removeCallbacksAndMessages(null);
                // delayed schedule turned off successfully

 			}
 		});

        applySwapListener();
        
        blinking();

        centerTitleApp();
        requestPermission();

    }

    private void blinking(){
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blinking);
        imageViewLogoRTH.startAnimation(animation);
    }

    private void applySwapListener(){
        imageViewLogoRTH .setOnTouchListener(new OnSwipeListener(WelcomeActivity.this) {
            public void onSwipeTop() {
            }
            public void onSwipeRight() {
                swapAnim(MOVE_RIGHT);
            }
            public void onSwipeLeft() {
                swapAnim(MOVE_LEFT);
            }
            public void onSwipeBottom() {
           }
        });
    }

    private void swapAnim(int area){

        Animation animation = null;

        if(area == MOVE_LEFT){
           animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.movement_left);
        }else {
            animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.movement_right);
        }

        imageViewLogoRTH.startAnimation(animation);

        // toggle the status
        workAsClient = false;
    }

    private void centerTitleApp(){
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
    }

    private boolean gotPermission(){
        return hasPermissions(this, PERMISSIONS);
    }

    private void proceed(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(gotPermission()){
                    gotoAnotherActivity();
                }else{
                    finish();
                }
            }
        }, WAITING_TIME);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_ALL) {
            proceed();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void gotoAnotherActivity(){

        if(workAsClient) {
            user_usage = Keys.ACCESS_CLIENT;
            UserData.savePreference(Keys.USER_USAGE, user_usage);


            Intent intent = new Intent(this, TutorialActivity.class);
            startActivity(intent);


        }else{
            // now we move on as management people
            user_usage = Keys.ACCESS_MANAGEMENT;
            UserData.savePreference(Keys.USER_USAGE, user_usage);

            gotoLoginActivity();

        }

        finish();
    }

    private void downloadNewPosterAds(){

        // we grab the data from the server
        

    }

    public void gotoLoginActivity(){

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            android.Manifest.permission.INTERNET,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.ACCESS_NETWORK_STATE
    };

    private void requestPermission(){

        if (!gotPermission()) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }else{
            proceed();
        }


    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }



}