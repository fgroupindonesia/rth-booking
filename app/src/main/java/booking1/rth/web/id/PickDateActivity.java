package booking1.rth.web.id;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import object.Keys;
import shared.UserData;

public class PickDateActivity extends AppCompatActivity {

    TextView textViewBulanTahun;
    Spinner spinnerPickDate;
    ImageView imageViewUserProfile, imageViewProfesi;


    String dateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_date);

        // stored for future usage
        UserData.setPreference(this);

        spinnerPickDate = (Spinner) findViewById(R.id.spinnerPickDate);
        textViewBulanTahun = (TextView) findViewById(R.id.textViewBulanTahun);
        imageViewUserProfile = (ImageView) findViewById(R.id.imageViewUserProfile);

        imageViewProfesi = (ImageView) findViewById(R.id.imageViewProfesi);

        centerTitleApp();

        updateMonth();
        generate2DateForWeeksAhead();
        updateUserProfile();
    }

    private void centerTitleApp(){
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
    }

    int prof;

    private void updateUserProfile(){

        int  modeUser = UserData.getPreferenceInt(Keys.USER_GENDER);

            if(modeUser==Keys.MODE_IKHWAN){
                imageViewUserProfile.setImageResource(R.drawable.ikhwan_logo);
            }else if(modeUser==Keys.MODE_AKHWAT){
                imageViewUserProfile.setImageResource(R.drawable.akhwat_logo);
            }

        prof = UserData.getPreferenceInt(Keys.USER_PROFESSION);

        if(prof == Keys.PROFESI_UMUM){
            imageViewProfesi.setImageResource(R.drawable.umum_icon);
        }else if(prof == Keys.PROFESI_PELAJAR){
            imageViewProfesi.setImageResource(R.drawable.pelajar_icon);
        }

    }

    private void generate2DateForWeeksAhead(){

        List<String> spinnerArray =  new ArrayList<String>();
        String output = null;
        SimpleDateFormat sdf1 =  new SimpleDateFormat("EEEE dd-MMM-yyyy", new Locale("ID"));

        Calendar c = Calendar.getInstance();
        for(int x=0; x<=14; x++){
            c = Calendar.getInstance();
            c.add(Calendar.DATE, x);
            output = sdf1.format(c.getTime());

            spinnerArray.add(output);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPickDate.setAdapter(adapter);
        spinnerPickDate.setSelection(0);
    }

    private void updateMonth(){
        String dateText;

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", new Locale("ID"));
        dateText = dateFormat.format(Calendar.getInstance().getTime());

        textViewBulanTahun.setText(dateText);
    }

    public void openNext(View v){
        dateText = spinnerPickDate.getSelectedItem().toString();

        Intent i = new Intent(this, BookingScheduleActivity.class);
        i.putExtra(Keys.DATE_CHOSEN, dateText);

        startActivity(i);

    }

    public void chooseProfile(View v){

        Intent i = new Intent(this, ClientProfileActivity.class);
        startActivity(i);

        finish();

    }

}