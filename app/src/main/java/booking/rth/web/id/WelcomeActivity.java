package booking.rth.web.id;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import helper.DoubleClickListener;
import helper.UserProfile;
import object.Keys;

public class WelcomeActivity extends AppCompatActivity {

    final int WAITING_TIME = 3000; // 3seconds
    ImageView imageViewLogoRTH;

    boolean workAsClient = true;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        MediaPlayer mPlayer2 = MediaPlayer.create(this, R.raw.audio_selamat_datang);
        //mPlayer2.start();

        imageViewLogoRTH = (ImageView) findViewById(R.id.imageViewLogoRTH);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                gotoAnotherActivity();
            }
        }, WAITING_TIME);

        imageViewLogoRTH.setOnClickListener(new DoubleClickListener() {

         	@Override
 			public void onDoubleClick() {
         	    // turning off the delayed schedule
         	    workAsClient = false;
                //handler.removeCallbacksAndMessages(null);
                // delayed schedule turned off successfully

 			}
 		});

        requestPermission();
    }

    private void gotoAnotherActivity(){

        if(workAsClient) {
            UserProfile.USAGE = Keys.CLIENT;
            Intent intent = new Intent(this, TutorialActivity.class);
            startActivity(intent);


        }else{
            // now we move on as management people
            UserProfile.USAGE = Keys.MANAGEMENT;
            gotoLoginActivity();

        }

        finish();
    }

    public void gotoLoginActivity(){

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void requestPermission(){
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                android.Manifest.permission.INTERNET,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.ACCESS_NETWORK_STATE
        };

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
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