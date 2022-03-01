package booking.rth.web.id;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import helper.UserProfile;
import object.Keys;

public class ClientProfileActivity extends AppCompatActivity {

    LinearLayout linearAkhwat, linearIkhwan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        linearAkhwat = (LinearLayout) findViewById(R.id.linearAkhwat);
        linearIkhwan = (LinearLayout) findViewById(R.id.linearIkhwan);


        centerTitleApp();
    }

    private void centerTitleApp(){
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
    }

    public void openBookingIkhwan(View v){
        openNext(v, Keys.MODE_IKHWAN);
    }

    public void openBookingAkhwat(View v){
        openNext(v, Keys.MODE_AKHWAT);
    }

    public void openNext(View v, int modeUser){

        Intent i = new Intent(this, PickDateActivity.class);
        UserProfile.USER_GENDER = modeUser;
        startActivity(i);

    }

}